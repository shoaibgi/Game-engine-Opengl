package core.scenegraph;

import core.buffers.VBO;
import core.pipeline.GLShaderProgram;
import core.pipeline.RenderParameter;
import core.scenegraph.NodeComponent;

public class GLRenderInfo extends NodeComponent{
	
	private GLShaderProgram shader;
	private RenderParameter config;
	private VBO vbo;
	
	@Override
	public void render(){
		
		config.enable();
		shader.bind();			
		shader.updateUniforms(getParent());
		vbo.draw();
		config.disable();
	}

}
