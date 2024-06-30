package modules.gpgpu;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import core.shaders.Shader;
import core.texturing.Texture2D;
import core.utils.ResourceLoader;

public class NormalMapShader extends Shader{

	private static NormalMapShader instance = null;

	public static NormalMapShader getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new NormalMapShader();
	    }
	      return instance;
	}
	
	public NormalMapShader()
	{
		super();
		
		addComputeShader(ResourceLoader.loadShader("shaders/gpgpu/NormalMap.glsl"));
		compileShader();
	
		addUniform("displacementmap");
		addUniform("N");
		addUniform("normalStrength");
	}
	
	public void updateUniforms(Texture2D heightmap, int N, float strength)
	{
		glActiveTexture(GL_TEXTURE0);
		heightmap.bind();
		setUniformi("displacementmap", 0);
		setUniformi("N", N);
		setUniformf("normalStrength", strength);
	}

	public static void render() {
		// TODO Auto-generated method stub
		
	}
}
