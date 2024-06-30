
package core.context;

import core.kernel.*;
import core.platform.*;
import core.kernel.Window;
import core.scenegraph.Camera;


public abstract class BaseContext {


	protected static Config config;

	protected static GLFWInput input;
	
	protected static Camera camera;

	protected static Window window;

	protected static CoreEngine coreEngine;

	protected static RenderingEngine renderEngine;
	
	public static void init() {
		
		config = new Config();
		input = new GLFWInput();
		coreEngine = new CoreEngine();
	}

}
