package cn.taskeren.gtnn.client.rawinput;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.util.Lazy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("ALL")
public class RawInput {

	private static Lazy<Class<?>> GLFW_CLASS = new Lazy<>(() -> Class.forName("org.lwjgl.glfw.GLFW"));
	private static Lazy<Class<?>> LWJGL_DISPLAY_CLASS = new Lazy<>(() -> Class.forName("org.lwjglx.opengl.Display"));

	private static boolean successInit = false;

	private static MethodHandle DISPLAY_WINDOW_HANDLE;
	private static MethodHandle GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE;
	private static MethodHandle GLFW_SET_INPUT_MODE;
	private static int GLFW_RAW_MOUSE_MOTION;

	static {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			DISPLAY_WINDOW_HANDLE = lookup.findStatic(
				LWJGL_DISPLAY_CLASS.get(),
				"getWindow",
				MethodType.methodType(Long.TYPE)
			);
			GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE = lookup.findStatic(
				GLFW_CLASS.get(),
				"glfwRawMouseMotionSupported",
				MethodType.methodType(Boolean.TYPE)
			);
			GLFW_SET_INPUT_MODE = lookup.findStatic(
				GLFW_CLASS.get(),
				"glfwSetInputMode",
				MethodType.methodType(Void.TYPE, Long.TYPE, Integer.TYPE, Integer.TYPE)
			);
			GLFW_RAW_MOUSE_MOTION = (int) lookup.findStaticGetter(
				GLFW_CLASS.get(),
				"GLFW_RAW_MOUSE_MOTION",
				Integer.TYPE
			).invokeExact();

			successInit = true;
		} catch(Throwable ex) {
			// dont throw. this is not so important. :)
			GTNN.logger.error("Failed to initialize GLFW reflections.", ex);
		}
	}

	private static long getWindowHandle() {
		try {
			return (long) DISPLAY_WINDOW_HANDLE.invokeExact();
		} catch(Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static void glfwSetInputMode(long window, int type, int value) {
		try {
			GLFW_SET_INPUT_MODE.invokeExact(window, type, value);
		} catch(Throwable ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean isSuccessInit() {
		return successInit;
	}

	/**
	 * Test if Raw Mouse Motion is supported.
	 */
	public static boolean isRawMouseMotionSupported() {
		try {
			return (boolean) GLFW_RAW_MOUSE_MOTION_SUPPORTED_HANDLE.invokeExact();
		} catch(Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

	/**
	 * Set Raw Mouse Motion enabled or disabled.
	 */
	public static void setRawMouseMotion(boolean enable) {
		GTNN.logger.info("Raw Input Switching: " + enable);
		glfwSetInputMode(getWindowHandle(), GLFW_RAW_MOUSE_MOTION, enable ? 1 : 0);
	}

}
