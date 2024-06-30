package modules.water;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.GL_READ_ONLY;
import static org.lwjgl.opengl.GL15.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL30.GL_RGBA16F;
import static org.lwjgl.opengl.GL42.glBindImageTexture;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import core.context.BaseContext;
import core.context.GLContext;
import core.texturing.GLTexture;
import core.wrapper.texture.TextureImage2D;
import core.image.Image.ImageFormat;
import core.image.Image.SamplerFilter;
import core.image.Image.TextureWrapMode;

public class UnderWaterRenderer {
	

	private GLTexture underwaterSceneTexture;
	private UnderWaterShader underWaterShader;
	
	private GLTexture dudvMap;
	private GLTexture causticsMap;
	
	public UnderWaterRenderer() {
		underWaterShader = UnderWaterShader.getInstance();
		
		underwaterSceneTexture = new TextureImage2D(BaseContext.getConfig().getFrameWidth(),
				BaseContext.getConfig().getFrameHeight(),
				ImageFormat.RGBA16FLOAT, SamplerFilter.Bilinear, TextureWrapMode.ClampToEdge);
		
		dudvMap = new TextureImage2D("textures/water/dudv/dudv1.jpg", SamplerFilter.Trilinear);
		causticsMap = new TextureImage2D("textures/water/caustics/caustics.jpg", SamplerFilter.Trilinear);
		
		GLContext.getResources().setUnderwaterCausticsMap(causticsMap);
		GLContext.getResources().setUnderwaterDudvMap(dudvMap);
	}
	
	public void render(GLTexture sceneTexture, GLTexture sceneDepthMap) {
		
		underWaterShader.bind();
		glBindImageTexture(0, sceneTexture.getHandle(), 0, false, 0, GL_READ_ONLY, GL_RGBA16F);
		glBindImageTexture(1, underwaterSceneTexture.getHandle(), 0, false, 0, GL_WRITE_ONLY, GL_RGBA16F);
		underWaterShader.updateUniforms(sceneDepthMap);
		glDispatchCompute(BaseContext.getConfig().getFrameWidth()/8, BaseContext.getConfig().getFrameHeight()/8, 1);	
		glFinish();
	}

}
