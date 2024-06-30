package core.context;

import modules.water.WaterConfig;
import core.framebuffer.GLFrameBufferObject;
import core.memory.GLUniformBuffer;
import core.texturing.GLTexture;


public class GLResources {
	
	private GLFrameBufferObject primaryFbo;
	private GLTexture sceneDepthMap;
	
	private GLTexture underwaterDudvMap;
	private GLTexture underwaterCausticsMap;
	
	private WaterConfig waterConfig;
	private GLUniformBuffer GlobalShaderParameters;
	
	
	public WaterConfig getWaterConfig() {
		return waterConfig;
	}
	public void setWaterConfig(WaterConfig waterConfig) {
		this.waterConfig = waterConfig;
	}
	public GLUniformBuffer getGlobalShaderParameters() {
		return GlobalShaderParameters;
	}
	public void setGlobalShaderParameters(GLUniformBuffer globalShaderParameters) {
		GlobalShaderParameters = globalShaderParameters;
	}
	public GLTexture getUnderwaterCausticsMap() {
		return underwaterCausticsMap;
	}
	public void setUnderwaterCausticsMap(GLTexture underwaterCausticsMap) {
		this.underwaterCausticsMap = underwaterCausticsMap;
	}
	public GLTexture getSceneDepthMap() {
		return sceneDepthMap;
	}
	public void setSceneDepthMap(GLTexture sceneDepthMap) {
		this.sceneDepthMap = sceneDepthMap;
	}
	public GLFrameBufferObject getPrimaryFbo() {
		return primaryFbo;
	}
	public void setPrimaryFbo(GLFrameBufferObject primaryFbo) {
		this.primaryFbo = primaryFbo;
	}
	public GLTexture getUnderwaterDudvMap() {
		return underwaterDudvMap;
	}
	public void setUnderwaterDudvMap(GLTexture underwaterDudvMap) {
		this.underwaterDudvMap = underwaterDudvMap;
	}
}
