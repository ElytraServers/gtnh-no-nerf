package cn.taskeren.gtnn.client.rawinput;

import cn.taskeren.gtnn.GTNN;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("ALL")
public class RawInput {

	private static Class<?> getLwjglDisplayClass() {
		try {
			return Class.forName("org.lwjglx.opengl.Display");
		} catch(ClassNotFoundException ex) {
			throw new RuntimeException("Cannot find class \"org.lwjglx.opengl.Display\" in the classpath");
		}
	}

	private static MethodHandle DISPLAY_WINDOW_HANDLE;
	private static MethodHandle GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE;
	private static MethodHandle GLFW_SET_INPUT_MODE;
	private static int GLFW_RAW_MOUSE_MOTION;

	static {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			DISPLAY_WINDOW_HANDLE = lookup.findStatic(
				getLwjglDisplayClass(),
				"getWindow",
				MethodType.methodType(Long.TYPE)
			);
			GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE = lookup.findStatic(
				GLFWUtils.getGlfwClass(),
				"glfwRawMouseMotionSupported",
				MethodType.methodType(Boolean.TYPE)
			);
			GLFW_SET_INPUT_MODE = lookup.findStatic(
				GLFWUtils.getGlfwClass(),
				"glfwSetInputMode",
				MethodType.methodType(Void.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE)
			);
			GLFW_RAW_MOUSE_MOTION = (int) lookup.findStaticGetter(
				GLFWUtils.getGlfwClass(),
				"GLFW_RAW_MOUSE_MOTION",
				Integer.TYPE
			).invokeExact();
		} catch(Throwable ex) {
			throw new RuntimeException("Exception occurred when loading GLFW", ex);
		}
	}

	public static long getWindowHandle() {
		try {
			return (long) DISPLAY_WINDOW_HANDLE.invokeExact();
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isRawMouseMotionSupported() {
		try {
			return (boolean) GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE.invokeExact();
		} catch(Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	public static void glfwSetInputMode(long window, int type, int value) {
		try {
			GLFW_SET_INPUT_MODE.invokeExact(window, type, value);
		} catch(Throwable ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void setRawInput(boolean enable) {
		GTNN.logger.info("Raw Input Switching: " + enable);
		glfwSetInputMode(getWindowHandle(), GLFW_RAW_MOUSE_MOTION, enable ? 1 : 0);
	}

}
