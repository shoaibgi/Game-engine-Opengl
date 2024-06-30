package modules.water;

import core.math.Vec4f;
import core.utils.Constants;
import modules.Water;
import modules.WaterShader;
import modules.WaterWireframeShader;

public class Ocean extends Water{

	public Ocean() {
		
		super(128, WaterShader.getInstance(), WaterWireframeShader.getInstance());
		
		getWorldTransform().setScaling(Constants.ZFAR*1.95f,1,Constants.ZFAR*1.95f);
		getWorldTransform().setTranslation(-Constants.ZFAR*1.95f/2,0,-Constants.ZFAR*1.95f/2);
		
		setClip_offset(4);
		setClipplane(new Vec4f(0,-1,0,getWorldTransform().getTranslation().getY() + 20));
		
		initShaderBuffer();
	}

	
}
