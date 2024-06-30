package core.wrapper.texture;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import core.pipeline.RenderParameter;

public class CullFaceDisable implements RenderParameter{
	
	public void enable(){
		glDisable(GL_CULL_FACE);
	}
	
	public void disable(){
		glEnable(GL_CULL_FACE);
	}		
}