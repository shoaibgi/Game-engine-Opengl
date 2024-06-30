package modules.fft;

import core.pipeline.GLShaderProgram;
import core.utils.ResourceLoader;

public class FFTInversionShader extends GLShaderProgram{

	private static FFTInversionShader instance = null;
	
	public static FFTInversionShader getInstance() 
	{
	    if(instance == null) 
	    {
	    	instance = new FFTInversionShader();
	    }
	    return instance;
	}
		
	protected FFTInversionShader()
	{
		super();
			
		addComputeShader(ResourceLoader.loadShader("shaders/fft/inversion.comp"));
		compileShader();
			
		addUniform("pingpong");
		addUniform("N");
	}
		
	public void updateUniforms(int N, int pingpong)
	{
		setUniformi("N", N);
		setUniformi("pingpong", pingpong);
	}
}