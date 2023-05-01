package cn.taskeren.gtnn.client.rawinput;

public class GLFWUtils {

	private static boolean initialized = false;
	private static boolean isGFLWProvided = false;

	private static Class<?> GLFW_CLASS;

	public static boolean isGFLWProvided() {
		if(!initialized) {
			try {
				GLFW_CLASS = Class.forName("org.lwjgl.glfw.GLFW");
				isGFLWProvided = true;
			} catch(Exception ex) {
				isGFLWProvided = false;
			}
			initialized = true;
		}

		return isGFLWProvided;
	}

	public static Class<?> getGlfwClass() {
		if(isGFLWProvided()) {
			return GLFW_CLASS;
		} else {
			throw new IllegalStateException("GLFW is not provided in this environment!");
		}
	}

}
