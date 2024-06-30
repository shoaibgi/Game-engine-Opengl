package core.wrapper.texture;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import core.pipeline.RenderParameter;

public class AlphaBlending implements RenderParameter{
	
	private int srcBlendFactor;
	private int dstBlendFactor;
	
	public AlphaBlending(int srcAlphaBlendFactor,
			int dstAlphaBlendFactor){
		
		srcBlendFactor = srcAlphaBlendFactor;
		dstBlendFactor = dstAlphaBlendFactor;
	}
	
	public void enable(){
		glEnable(GL_BLEND);	
		glBlendFunc(srcBlendFactor, dstBlendFactor);
	}
	
	public void disable(){
		glDisable(GL_BLEND);
	}
}
