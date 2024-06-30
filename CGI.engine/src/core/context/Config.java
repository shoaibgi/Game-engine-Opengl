package core.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import core.math.*;
import core.utils.Constants;

public class Config {
	
	// screen settings
	private int frameWidth;
	private int frameHeight;

	// window settings
	private String displayTitle;
	private int windowWidth;
	private int windowHeight;
	
	// glfw opengl vsync
	private boolean glfwGLVSync;
	
	// anitaliasing
	private final int multisampling_sampleCount;
	private boolean fxaaEnabled;
	
	// shadows settings
	private boolean shadowsEnable;
	private int shadowMapResolution;
	private int shadowsQuality;
	
	// post processing effects
	private boolean ssaoEnabled;
	private boolean bloomEnabled;
	private boolean depthOfFieldBlurEnabled;
	private boolean motionBlurEnabled;
	private boolean lightScatteringEnabled;
	private boolean lensFlareEnabled;
	
	// dynamic render settings
	private boolean renderWireframe;
	private boolean renderUnderwater;
	private boolean renderReflection;
	private boolean renderRefraction;
	private Vec4f clipplane;
	
	// Vulkan Validation
	private boolean vkValidation;
	
	// Atmosphere parameters
	private float sunRadius;
	private Vec3f sunPosition;
	private Vec3f sunColor;
	private float sunIntensity;
	private float ambient;
	private boolean AtmosphericScatteringEnable;
	private float atmosphereBloomFactor;
	private Vec3f fogColor;
	private float horizonVerticalShift;
	private float horizonReflectionVerticalShift;
	private float sightRange;
	
	// postprocessing parameters
	private int lightscatteringSampleCount;
	private float lightscatteringDecay;
	private float motionblurSampleCount;
	private int motionblurBlurfactor;
	private int bloomKernels;
	private int bloomSigma;
	
	public Config(){
		
		Properties properties = new Properties();
		try {
			InputStream vInputStream = Config.class.getClassLoader().getResourceAsStream("oe-config.properties");
			properties.load(vInputStream);
			vInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setWindowWidth(Integer.valueOf(properties.getProperty("window.width")));
		setWindowHeight(Integer.valueOf(properties.getProperty("window.height")));
		setDisplayTitle(properties.getProperty("display.title"));
		frameWidth = Integer.valueOf(properties.getProperty("frame.width"));
		frameHeight = Integer.valueOf(properties.getProperty("frame.height"));
		multisampling_sampleCount = Integer.valueOf(properties.getProperty("multisampling.sample.count"));
		setFxaaEnabled(Integer.valueOf(properties.getProperty("fxaa.enable")) == 1 ? true : false);
		setShadowsEnable(Integer.valueOf(properties.getProperty("shadows.enable")) == 1 ? true : false);
		setShadowMapResolution(Integer.valueOf(properties.getProperty("shadows.map.resolution")));
		setShadowsQuality(Integer.valueOf(properties.getProperty("shadows.quality")));
		setBloomEnabled(Integer.valueOf(properties.getProperty("bloom.enable")) == 1 ? true : false);
		setSsaoEnabled(Integer.valueOf(properties.getProperty("ssao.enable")) == 1 ? true : false);
		setMotionBlurEnabled(Integer.valueOf(properties.getProperty("motionBlur.enable")) == 1 ? true : false);
		setLightScatteringEnabled(Integer.valueOf(properties.getProperty("lightScattering.enable")) == 1 ? true : false);
		setDepthOfFieldBlurEnabled(Integer.valueOf(properties.getProperty("depthOfFieldBlur.enable")) == 1 ? true : false);
		setLensFlareEnabled(Integer.valueOf(properties.getProperty("lensFlare.enable")) == 1 ? true : false);
		
		if (properties.getProperty("validation.enable") != null){
			setVkValidation(Integer.valueOf(properties.getProperty("validation.enable")) == 1 ? true : false);
		}
		
		if (properties.getProperty("glfw.vsync") != null){
			setGlfwGLVSync(Integer.valueOf(properties.getProperty("glfw.vsync")) == 1 ? true : false);
		}
		
		setRenderWireframe(false);
		setRenderUnderwater(false);
		setRenderReflection(false);
		setRenderRefraction(false);
		setClipplane(Constants.ZEROPLANE);
		
		
		try {
			InputStream vInputStream = Config.class.getClassLoader().getResourceAsStream("atmosphere-config.properties");
			if (vInputStream != null){
				properties.load(vInputStream);
				vInputStream.close();
				
				setSunRadius(Float.valueOf(properties.getProperty("sun.radius")));
				setSunPosition(new Vec3f(
						Float.valueOf(properties.getProperty("sun.position.x")),
						Float.valueOf(properties.getProperty("sun.position.y")),
						Float.valueOf(properties.getProperty("sun.position.z"))).normalize());
				setSunColor(new Vec3f(
						Float.valueOf(properties.getProperty("sun.color.r")),
						Float.valueOf(properties.getProperty("sun.color.g")),
						Float.valueOf(properties.getProperty("sun.color.b"))));
				setSunIntensity(Float.valueOf(properties.getProperty("sun.intensity")));
				setAmbient(Float.valueOf(properties.getProperty("ambient")));
				setAtmosphericScatteringEnable(Integer.valueOf(properties.getProperty("atmosphere.scattering.enable")) == 1 ? true : false);
				setHorizonVerticalShift(Float.valueOf(properties.getProperty("horizon.verticalShift")));
				setHorizonReflectionVerticalShift(Float.valueOf(properties.getProperty("horizon.reflection.verticalShift")));
				setAtmosphereBloomFactor(Float.valueOf(properties.getProperty("atmosphere.bloom.factor")));
				setSightRange(Float.valueOf(properties.getProperty("sightRange")));
				fogColor = new Vec3f(Float.valueOf(properties.getProperty("fog.color.r")),
						Float.valueOf(properties.getProperty("fog.color.g")),
						Float.valueOf(properties.getProperty("fog.color.b")));
				float fogBrightness = Float.valueOf(properties.getProperty("fog.brightness"));
				
				fogColor = fogColor.mul(fogBrightness);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			InputStream vInputStream = Config.class.getClassLoader().getResourceAsStream("postprocessing-config.properties");
			if (vInputStream != null){
				properties.load(vInputStream);
				vInputStream.close();
				
				setLightscatteringSampleCount(Integer.valueOf(properties.getProperty("lightscattering.samples.count")));
				setLightscatteringDecay(Float.valueOf(properties.getProperty("lightscattering.decay")));
				setMotionblurBlurfactor(Integer.valueOf(properties.getProperty("motionblur.blurfactor")));
				setMotionblurSampleCount(Integer.valueOf(properties.getProperty("motionblur.samples.count")));
				setBloomKernels(Integer.valueOf(properties.getProperty("bloom.kernels")));
				setBloomSigma(Integer.valueOf(properties.getProperty("bloom.sigma")));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}
	
	public int getFrameHeight() {
		return frameHeight;
	}

	public boolean isFxaaEnabled() {
		return fxaaEnabled;
	}

	public void setFxaaEnabled(boolean fxaaEnabled) {
		this.fxaaEnabled = fxaaEnabled;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public String getDisplayTitle() {
		return displayTitle;
	}

	public void setDisplayTitle(String displayTitle) {
		this.displayTitle = displayTitle;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public boolean isShadowsEnable() {
		return shadowsEnable;
	}

	public void setShadowsEnable(boolean shadowsEnable) {
		this.shadowsEnable = shadowsEnable;
	}

	public int getShadowMapResolution() {
		return shadowMapResolution;
	}

	public void setShadowMapResolution(int shadowMapResolution) {
		this.shadowMapResolution = shadowMapResolution;
	}

	public int getShadowsQuality() {
		return shadowsQuality;
	}

	public void setShadowsQuality(int shadowsQuality) {
		this.shadowsQuality = shadowsQuality;
	}

	public boolean isGlfwGLVSync() {
		return glfwGLVSync;
	}

	public void setGlfwGLVSync(boolean glfwGLVSync) {
		this.glfwGLVSync = glfwGLVSync;
	}

	public int getMultisampling_sampleCount() {
		return multisampling_sampleCount;
	}

	public boolean isSsaoEnabled() {
		return ssaoEnabled;
	}

	public void setSsaoEnabled(boolean ssaoEnabled) {
		this.ssaoEnabled = ssaoEnabled;
	}

	public boolean isBloomEnabled() {
		return bloomEnabled;
	}

	public void setBloomEnabled(boolean bloomEnabled) {
		this.bloomEnabled = bloomEnabled;
	}

	public boolean isDepthOfFieldBlurEnabled() {
		return depthOfFieldBlurEnabled;
	}

	public void setDepthOfFieldBlurEnabled(boolean depthOfFieldBlurEnabled) {
		this.depthOfFieldBlurEnabled = depthOfFieldBlurEnabled;
	}

	public boolean isMotionBlurEnabled() {
		return motionBlurEnabled;
	}

	public void setMotionBlurEnabled(boolean motionBlurEnabled) {
		this.motionBlurEnabled = motionBlurEnabled;
	}

	public boolean isLightScatteringEnabled() {
		return lightScatteringEnabled;
	}

	public void setLightScatteringEnabled(boolean lightScatteringEnabled) {
		this.lightScatteringEnabled = lightScatteringEnabled;
	}

	public boolean isLensFlareEnabled() {
		return lensFlareEnabled;
	}

	public void setLensFlareEnabled(boolean lensFlareEnabled) {
		this.lensFlareEnabled = lensFlareEnabled;
	}

	public boolean isRenderWireframe() {
		return renderWireframe;
	}

	public void setRenderWireframe(boolean renderWireframe) {
		this.renderWireframe = renderWireframe;
	}

	public boolean isRenderUnderwater() {
		return renderUnderwater;
	}

	public void setRenderUnderwater(boolean renderUnderwater) {
		this.renderUnderwater = renderUnderwater;
	}

	public boolean isRenderReflection() {
		return renderReflection;
	}

	public void setRenderReflection(boolean renderReflection) {
		this.renderReflection = renderReflection;
	}

	public boolean isRenderRefraction() {
		return renderRefraction;
	}

	public void setRenderRefraction(boolean renderRefraction) {
		this.renderRefraction = renderRefraction;
	}

	public Vec4f getClipplane() {
		return clipplane;
	}

	public void setClipplane(Vec4f clipplane) {
		this.clipplane = clipplane;
	}

	public boolean isVkValidation() {
		return vkValidation;
	}

	public void setVkValidation(boolean vkValidation) {
		this.vkValidation = vkValidation;
	}

	public float getSunRadius() {
		return sunRadius;
	}

	public void setSunRadius(float sunRadius) {
		this.sunRadius = sunRadius;
	}

	public Vec3f getSunPosition() {
		return sunPosition;
	}

	public void setSunPosition(Vec3f sunPosition) {
		this.sunPosition = sunPosition;
	}

	public Vec3f getSunColor() {
		return sunColor;
	}

	public void setSunColor(Vec3f sunColor) {
		this.sunColor = sunColor;
	}

	public float getSunIntensity() {
		return sunIntensity;
	}

	public void setSunIntensity(float sunIntensity) {
		this.sunIntensity = sunIntensity;
	}

	public float getAmbient() {
		return ambient;
	}

	public void setAmbient(float ambient) {
		this.ambient = ambient;
	}

	public boolean isAtmosphericScatteringEnable() {
		return AtmosphericScatteringEnable;
	}

	public void setAtmosphericScatteringEnable(boolean atmosphericScatteringEnable) {
		AtmosphericScatteringEnable = atmosphericScatteringEnable;
	}

	public float getAtmosphereBloomFactor() {
		return atmosphereBloomFactor;
	}

	public void setAtmosphereBloomFactor(float atmosphereBloomFactor) {
		this.atmosphereBloomFactor = atmosphereBloomFactor;
	}

	public float getHorizonVerticalShift() {
		return horizonVerticalShift;
	}

	public void setHorizonVerticalShift(float horizonVerticalShift) {
		this.horizonVerticalShift = horizonVerticalShift;
	}

	public float getHorizonReflectionVerticalShift() {
		return horizonReflectionVerticalShift;
	}

	public void setHorizonReflectionVerticalShift(float horizonReflectionVerticalShift) {
		this.horizonReflectionVerticalShift = horizonReflectionVerticalShift;
	}

	public float getSightRange() {
		return sightRange;
	}

	public void setSightRange(float sightRange) {
		this.sightRange = sightRange;
	}

	public int getLightscatteringSampleCount() {
		return lightscatteringSampleCount;
	}

	public void setLightscatteringSampleCount(int lightscatteringSampleCount) {
		this.lightscatteringSampleCount = lightscatteringSampleCount;
	}

	public float getLightscatteringDecay() {
		return lightscatteringDecay;
	}

	public void setLightscatteringDecay(float lightscatteringDecay) {
		this.lightscatteringDecay = lightscatteringDecay;
	}

	public int getMotionblurBlurfactor() {
		return motionblurBlurfactor;
	}

	public void setMotionblurBlurfactor(int motionblurBlurfactor) {
		this.motionblurBlurfactor = motionblurBlurfactor;
	}

	public float getMotionblurSampleCount() {
		return motionblurSampleCount;
	}

	public void setMotionblurSampleCount(float motionblurSampleCount) {
		this.motionblurSampleCount = motionblurSampleCount;
	}

	public int getBloomKernels() {
		return bloomKernels;
	}

	public void setBloomKernels(int bloomKernels) {
		this.bloomKernels = bloomKernels;
	}

	public int getBloomSigma() {
		return bloomSigma;
	}

	public void setBloomSigma(int bloomSigma) {
		this.bloomSigma = bloomSigma;
	}
}
