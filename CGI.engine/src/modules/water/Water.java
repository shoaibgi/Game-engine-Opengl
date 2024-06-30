package modules.water;

import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE6;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.system.MemoryUtil.memAlloc;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import modules.water.WaterConfig;
import core.context.BaseContext;
import core.context.GLContext;
import core.framebuffer.GLFramebuffer;
import core.memory.GLPatchVBO;
import core.memory.GLShaderStorageBuffer;
import core.pipeline.GLShaderProgram;
import core.scenegraph.GLRenderInfo;
import core.texturing.GLTexture;
import core.wrapper.texture.WaterRenderParameter;
import core.texturing.Texture2D;
import core.image.Image.ImageFormat;
import core.image.Image.SamplerFilter;
import core.math.Vec4f;
import core.scenegraph.NodeComponentType;
import core.scenegraph.RenderList;
import core.scenegraph.Renderable;
import core.scenegraph.Scenegraph;
import core.utils.BufferUtil;
import core.utils.Constants;
import core.utils.MeshGenerator;
import  modules.fft.FFT;
import modules.gpgpu.*;


public class Water extends Renderable{
	

	private Vec4f clipplane;

	private int clip_offset;
	
	private float motion;
	private GLTexture dudv;
	
	private GLFramebuffer reflection_fbo;
	private GLFramebuffer refraction_fbo;


	private GLTexture reflection_texture;
	private GLTexture refraction_texture;
	private RenderList reflectionRenderList;
	private RenderList refractionRenderList;
	
	private FFT fft;
	
	private NormalRenderer normalmapRenderer;
	private boolean isCameraUnderwater;
	
	private WaterRenderParameter renderConfig;
	
	
	private WaterConfig config;
	
	private GLShaderStorageBuffer ssbo;
	

	private float t_motion;

	private float t_distortion;
	private long systemTime = System.currentTimeMillis();

	public Water(int patches, GLShaderProgram shader, GLShaderProgram wireframeShader)
	{		
		config = new WaterConfig();
		config.loadFile("water-config.properties");
		GLContext.getResources().setWaterConfig(config);
		
		GLPatchVBO meshBuffer = new GLPatchVBO();
		meshBuffer.addData(MeshGenerator.generatePatch2D4x4(patches),16);
		
		renderConfig = new WaterRenderParameter();
		
		GLRenderInfo renderInfo = new GLRenderInfo(shader, renderConfig, meshBuffer);
		GLRenderInfo wireframeRenderInfo = new GLRenderInfo(wireframeShader, renderConfig, meshBuffer);
		
		dudv = new TextureImage2D("textures/water/dudv/dudv1.jpg", SamplerFilter.Trilinear);
		
		addComponent(NodeComponentType.MAIN_RENDERINFO, renderInfo);
		addComponent(NodeComponentType.WIREFRAME_RENDERINFO, wireframeRenderInfo);

		fft = new FFT(config.getN(), config.getL(),
				config.getAmplitude(), config.getWindDirection(),
				config.getAlignment(), config.getWindSpeed(),
				config.getCapillarWavesSupression());
		fft.setT_delta(config.getT_delta());
		fft.setChoppy(config.isChoppy());
		fft.init();
		
		normalmapRenderer = new NormalRenderer(config.getN());
		getNormalmapRenderer().setStrength(config.getNormalStrength());
		
		reflection_texture = new TextureImage2D(BaseContext.getConfig().getFrameWidth()/2,
				BaseContext.getConfig().getFrameHeight()/2,
				ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		
		IntBuffer drawBuffers = BufferUtil.createIntBuffer(1);
		drawBuffers.put(GL_COLOR_ATTACHMENT0);
		drawBuffers.flip();
		
		reflection_fbo = new GLFramebuffer();
		reflection_fbo.bind();
		reflection_fbo.createColorTextureAttachment(reflection_texture.getHandle(),0);
		reflection_fbo.createDepthBufferAttachment(BaseContext.getConfig().getFrameWidth()/2,
				BaseContext.getConfig().getFrameHeight()/2);
		reflection_fbo.setDrawBuffers(drawBuffers);
		reflection_fbo.checkStatus();
		reflection_fbo.unbind();
		
		refraction_texture = new TextureImage2D(BaseContext.getConfig().getFrameWidth()/2,
				BaseContext.getConfig().getFrameHeight()/2,
				ImageFormat.RGBA16FLOAT, SamplerFilter.Nearest);
		
		refraction_fbo = new GLFramebuffer();
		refraction_fbo.bind();
		refraction_fbo.createColorTextureAttachment(refraction_texture.getHandle(),0);
		refraction_fbo.createDepthBufferAttachment(BaseContext.getConfig().getFrameWidth()/2,
				BaseContext.getConfig().getFrameHeight()/2);
		refraction_fbo.setDrawBuffers(drawBuffers);
		refraction_fbo.checkStatus();
		refraction_fbo.unbind();
		
		refractionRenderList = new RenderList();
		reflectionRenderList = new RenderList();
	}	
	
	public void update()
	{
		isCameraUnderwater = BaseContext.getCamera().getPosition().getY() < (getWorldTransform().getTranslation().getY());
		t_motion += (System.currentTimeMillis() - systemTime) * config.getWaveMotion();
		t_distortion += (System.currentTimeMillis() - systemTime) * config.getDistortion();
		systemTime = System.currentTimeMillis();
	}
	
	public void renderWireframe(){
		
		fft.render();
		
		ssbo.bindBufferBase(1);
		
		super.renderWireframe();
		
		glFinish();
	}
	
	public void render()
	{
		if (!isCameraUnderwater){
			glEnable(GL_CLIP_DISTANCE6);
			BaseContext.getConfig().setRenderUnderwater(false);
		}
		else {
			BaseContext.getConfig().setRenderUnderwater(true);
		}
		
		Scenegraph scenegraph = ((Scenegraph) getParentNode());
		
		BaseContext.getConfig().setClipplane(clipplane);
			
		//-----------------------------------//
		//     mirror scene to clipplane     //
		//-----------------------------------//
		
		scenegraph.getWorldTransform().setScaling(1,-1,1);
			
		if (scenegraph.hasTerrain()){
				
			GLTerrain.getConfig().setVerticalScaling(
					GLTerrain.getConfig().getVerticalScaling() * -1f);
			GLTerrain.getConfig().setReflectionOffset(
					clip_offset * 2);
		}
		scenegraph.update();
		
		//-----------------------------------//
		//    render reflection to texture   //
		//-----------------------------------//

		int tempScreenResolutionX = BaseContext.getConfig().getFrameWidth(); 
		int tempScreenResolutionY = BaseContext.getConfig().getFrameHeight(); 
		BaseContext.getConfig().setFrameWidth(tempScreenResolutionX/2);
		BaseContext.getConfig().setFrameHeight(tempScreenResolutionY/2);
		glViewport(0,0,tempScreenResolutionX/2, tempScreenResolutionY/2);
		
		BaseContext.getConfig().setRenderReflection(true);
		
		reflection_fbo.bind();
		renderConfig.clearScreenDeepOcean();
		glFrontFace(GL_CCW);
		
		if (!isCameraUnderwater){
			
			scenegraph.record(reflectionRenderList);
			
			reflectionRenderList.remove(this.id);
			
			reflectionRenderList.getValues().forEach(object ->
			{
				object.render();
			});
		}
		
		// glFinish() important, to prevent conflicts with following compute shaders
		glFinish(); 
		glFrontFace(GL_CW);
		reflection_fbo.unbind();
		
		BaseContext.getConfig().setRenderReflection(false);
		
		//-----------------------------------//
		//   antimirror scene to clipplane   //
		//-----------------------------------//
	
		scenegraph.getWorldTransform().setScaling(1,1,1);

		if (scenegraph.hasTerrain()){
			GLTerrain.getConfig().setVerticalScaling(
					GLTerrain.getConfig().getVerticalScaling() / -1f);
			GLTerrain.getConfig().setReflectionOffset(0);
		}

		scenegraph.update();
		
		//-----------------------------------//
		//    render refraction to texture   //
		//-----------------------------------//
		
		BaseContext.getConfig().setRenderRefraction(true);
		
		refraction_fbo.bind();
		renderConfig.clearScreenDeepOcean();
	
		scenegraph.record(refractionRenderList);
		
		refractionRenderList.remove(this.id);
		
		refractionRenderList.getValues().forEach(object ->
		{
			object.render();
		});
		
		// glFinish() important, to prevent conflicts with following compute shaders
		glFinish();
		refraction_fbo.unbind();
		
		//-----------------------------------//
		//     reset rendering settings      //
		//-----------------------------------//
		
		BaseContext.getConfig().setRenderRefraction(false);
		
		glDisable(GL_CLIP_DISTANCE6);
		BaseContext.getConfig().setClipplane(Constants.ZEROPLANE);	
	
		glViewport(0,0,tempScreenResolutionX, tempScreenResolutionY);
		BaseContext.getConfig().setFrameWidth(tempScreenResolutionX);
		BaseContext.getConfig().setFrameHeight(tempScreenResolutionY);
		
		GLContext.getResources().getPrimaryFbo().bind();
		
		//-----------------------------------//
		//            render FFT'S           //
		//-----------------------------------//
		fft.render();
		normalmapRenderer.render(fft.getDy());

		ssbo.bindBufferBase(1);
		
		super.render();
		
		// glFinish() important, to prevent conflicts with following compute shaders
		glFinish();
	}
	
	public void initShaderBuffer() {
		
		ssbo = new GLShaderStorageBuffer();
		ByteBuffer byteBuffer = memAlloc(Float.BYTES * 33 + Integer.BYTES * 6);
		byteBuffer.put(BufferUtil.createByteBuffer().getWorldTransform().getWorldMatrix());
		byteBuffer.putInt(config.getUvScale());
		byteBuffer.putInt(config.getTessellationFactor());
		byteBuffer.putFloat(config.getTessellationSlope());
		byteBuffer.putFloat(config.getTessellationShift());
		byteBuffer.putFloat(config.getDisplacementScale());
		byteBuffer.putInt(config.getHighDetailRange());
		byteBuffer.putFloat(config.getChoppiness());
		byteBuffer.putFloat(config.getKReflection());
		byteBuffer.putFloat(config.getKRefraction());
		byteBuffer.putInt(BaseContext.getConfig().getFrameWidth());
		byteBuffer.putInt(BaseContext.getConfig().getFrameHeight());
		byteBuffer.putInt(config.isDiffuse() ? 1 : 0);
		byteBuffer.putFloat(config.getEmission());
		byteBuffer.putFloat(config.getSpecularFactor());
		byteBuffer.putFloat(config.getSpecularAmplifier());
		byteBuffer.putFloat(config.getReflectionBlendFactor());
		byteBuffer.putFloat(config.getBaseColor().getX());
		byteBuffer.putFloat(config.getBaseColor().getY());
		byteBuffer.putFloat(config.getBaseColor().getZ());
		byteBuffer.putFloat(config.getFresnelFactor());
		byteBuffer.putFloat(config.getCapillarStrength());
		byteBuffer.putFloat(config.getCapillarDownsampling());
		byteBuffer.putFloat(config.getDudvDownsampling());
		byteBuffer.flip();
		ssbo.addData(byteBuffer);
	}

}
