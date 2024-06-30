package modules.water;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import core.math.Vec2f;
import core.math.Vec3f;

public class WaterConfig {

	private int N;
	private int L;
	private float amplitude;
	private Vec2f windDirection;
	private float windSpeed;
	private float alignment;
	private float capillarWavesSupression;
	private float displacementScale;
	private float choppiness;
	private int tessellationFactor;
	private float tessellationShift;
	private float tessellationSlope;
	private int highDetailRange;
	private int uvScale;
	private float specularFactor;
	private float specularAmplifier;
	private boolean diffuse;
	private float emission;
	private float kReflection;
	private float kRefraction;
	private float distortion;
	private float fresnelFactor;
	private float waveMotion;
	private float normalStrength;
	private float t_delta;
	private boolean choppy;
	private Vec3f baseColor;
	private float reflectionBlendFactor;
	private float capillarStrength;
	private float capillarDownsampling;
	private float dudvDownsampling;
	private float underwaterBlur;
	
	public void loadFile(String file)
	{
		Properties properties = new Properties();
		try {
			InputStream stream = WaterConfig.class.getClassLoader().getResourceAsStream(file);
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setN(Integer.valueOf(properties.getProperty("fft.resolution")));
		setL(Integer.valueOf(properties.getProperty("fft.L")));
		setAmplitude(Float.valueOf(properties.getProperty("fft.amplitude")));
		setWindDirection(new Vec2f(Float.valueOf(properties.getProperty("wind.x")),
				Float.valueOf(properties.getProperty("wind.y"))).normalize());
		setWindSpeed(Float.valueOf(properties.getProperty("wind.speed")));
		setAlignment(Float.valueOf(properties.getProperty("alignment")));
		setCapillarWavesSupression(Float.valueOf(properties.getProperty("fft.capillarwavesSuppression")));
		setDisplacementScale(Float.valueOf(properties.getProperty("displacementScale")));
		setChoppiness(Float.valueOf(properties.getProperty("choppiness")));
		setDistortion(Float.valueOf(properties.getProperty("distortion_delta")));
		setWaveMotion(Float.valueOf(properties.getProperty("wavemotion")));
		setUvScale(Integer.valueOf(properties.getProperty("uvScale")));
		setTessellationFactor(Integer.valueOf(properties.getProperty("tessellationFactor")));
		setTessellationSlope(Float.valueOf(properties.getProperty("tessellationSlope")));
		setTessellationShift(Float.valueOf(properties.getProperty("tessellationShift")));
		setSpecularFactor(Float.valueOf(properties.getProperty("specular.factor")));
		setSpecularAmplifier(Float.valueOf(properties.getProperty("specular.amplifier")));
		setEmission(Float.valueOf(properties.getProperty("emission.factor")));
		setkReflection(Float.valueOf(properties.getProperty("kReflection")));
		setkRefraction(Float.valueOf(properties.getProperty("kRefraction")));
		setNormalStrength(Float.valueOf(properties.getProperty("normalStrength")));
		setHighDetailRange(Integer.valueOf(properties.getProperty("highDetailRange")));
		setT_delta(Float.valueOf(properties.getProperty("t_delta")));
		setChoppy(Boolean.valueOf(properties.getProperty("choppy")));
		setFresnelFactor(Float.valueOf(properties.getProperty("fresnel.factor")));
		setReflectionBlendFactor(Float.valueOf(properties.getProperty("reflection.blendfactor")));
		setBaseColor(new Vec3f(Float.valueOf(properties.getProperty("water.basecolor.x")),
				Float.valueOf(properties.getProperty("water.basecolor.y")),
				Float.valueOf(properties.getProperty("water.basecolor.z"))));
		setCapillarStrength(Float.valueOf(properties.getProperty("capillar.strength")));
		setCapillarDownsampling(Float.valueOf(properties.getProperty("capillar.downsampling")));
		setDudvDownsampling(Float.valueOf(properties.getProperty("dudv.downsampling")));
		setUnderwaterBlur(Float.valueOf(properties.getProperty("underwater.blurfactor")));
		setDiffuse(Integer.valueOf(properties.getProperty("diffuse.enable")) == 0 ? false : true);
	}

	public float getkReflection() {
		return kReflection;
	}

	public void setkReflection(float kReflection) {
		this.kReflection = kReflection;
	}

	public float getkRefraction() {
		return kRefraction;
	}

	public void setkRefraction(float kRefraction) {
		this.kRefraction = kRefraction;
	}

	public float getCapillarWavesSupression() {
		return capillarWavesSupression;
	}

	public void setCapillarWavesSupression(float capillarWavesSupression) {
		this.capillarWavesSupression = capillarWavesSupression;
	}

	public int getUvScale() {
		return uvScale;
	}

	public void setUvScale(int uvScale) {
		this.uvScale = uvScale;
	}

	public float getDistortion() {
		return distortion;
	}

	public void setDistortion(float distortion) {
		this.distortion = distortion;
	}

	public float getUnderwaterBlur() {
		return underwaterBlur;
	}

	public void setUnderwaterBlur(float underwaterBlur) {
		this.underwaterBlur = underwaterBlur;
	}

	public float getDudvDownsampling() {
		return dudvDownsampling;
	}

	public void setDudvDownsampling(float dudvDownsampling) {
		this.dudvDownsampling = dudvDownsampling;
	}

	public float getCapillarDownsampling() {
		return capillarDownsampling;
	}

	public void setCapillarDownsampling(float capillarDownsampling) {
		this.capillarDownsampling = capillarDownsampling;
	}

	public float getCapillarStrength() {
		return capillarStrength;
	}

	public void setCapillarStrength(float capillarStrength) {
		this.capillarStrength = capillarStrength;
	}

	public float getReflectionBlendFactor() {
		return reflectionBlendFactor;
	}

	public void setReflectionBlendFactor(float reflectionBlendFactor) {
		this.reflectionBlendFactor = reflectionBlendFactor;
	}

	public float getNormalStrength() {
		return normalStrength;
	}

	public void setNormalStrength(float normalStrength) {
		this.normalStrength = normalStrength;
	}

	public boolean isChoppy() {
		return choppy;
	}

	public void setChoppy(boolean choppy) {
		this.choppy = choppy;
	}

	public Vec3f getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Vec3f baseColor) {
		this.baseColor = baseColor;
	}

	public float getT_delta() {
		return t_delta;
	}

	public void setT_delta(float t_delta) {
		this.t_delta = t_delta;
	}

	public float getWaveMotion() {
		return waveMotion;
	}

	public void setWaveMotion(float waveMotion) {
		this.waveMotion = waveMotion;
	}

	public float getFresnelFactor() {
		return fresnelFactor;
	}

	public void setFresnelFactor(float fresnelFactor) {
		this.fresnelFactor = fresnelFactor;
	}

	public float getEmission() {
		return emission;
	}

	public void setEmission(float emission) {
		this.emission = emission;
	}

	public float getSpecularAmplifier() {
		return specularAmplifier;
	}

	public void setSpecularAmplifier(float specularAmplifier) {
		this.specularAmplifier = specularAmplifier;
	}

	public int getHighDetailRange() {
		return highDetailRange;
	}

	public void setHighDetailRange(int highDetailRange) {
		this.highDetailRange = highDetailRange;
	}

	public float getSpecularFactor() {
		return specularFactor;
	}

	public void setSpecularFactor(float specularFactor) {
		this.specularFactor = specularFactor;
	}

	public boolean isDiffuse() {
		return diffuse;
	}

	public void setDiffuse(boolean diffuse) {
		this.diffuse = diffuse;
	}

	public float getTessellationSlope() {
		return tessellationSlope;
	}

	public void setTessellationSlope(float tessellationSlope) {
		this.tessellationSlope = tessellationSlope;
	}

	public int getTessellationFactor() {
		return tessellationFactor;
	}

	public void setTessellationFactor(int tessellationFactor) {
		this.tessellationFactor = tessellationFactor;
	}

	public float getTessellationShift() {
		return tessellationShift;
	}

	public void setTessellationShift(float tessellationShift) {
		this.tessellationShift = tessellationShift;
	}

	public float getDisplacementScale() {
		return displacementScale;
	}

	public void setDisplacementScale(float displacementScale) {
		this.displacementScale = displacementScale;
	}

	public float getChoppiness() {
		return choppiness;
	}

	public void setChoppiness(float choppiness) {
		this.choppiness = choppiness;
	}

	public Vec2f getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(Vec2f windDirection) {
		this.windDirection = windDirection;
	}

	public float getAlignment() {
		return alignment;
	}

	public void setAlignment(float alignment) {
		this.alignment = alignment;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getL() {
		return L;
	}

	public void setL(int l) {
		L = l;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}
}
