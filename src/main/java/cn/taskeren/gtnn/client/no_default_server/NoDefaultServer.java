package cn.taskeren.gtnn.client.no_default_server;

import cn.taskeren.gtnn.GTNN;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public class NoDefaultServer {

	private static final String PACKAGE_DEFAULT_SERVER_LIST = "glowredman.defaultserverlist.";

	private static final String DEFAULT_SERVER_LIST_CONFIG_CLASS_NAME = PACKAGE_DEFAULT_SERVER_LIST + "Config";

	@Nullable
	private static final Class<?> DEFAULT_SERVER_LIST_CONFIG;

	static {
		Class<?> clazz;

		try {
			clazz = Class.forName(DEFAULT_SERVER_LIST_CONFIG_CLASS_NAME);
		} catch(ClassNotFoundException ex) {
			clazz = null;
			GTNN.logger.info("DefaultServerList is not found. Yay!");
		}

		DEFAULT_SERVER_LIST_CONFIG = clazz;
	}

	public static boolean isDefaultServerListInstalled() {
		return DEFAULT_SERVER_LIST_CONFIG != null;
	}

	public static void clearDefaultServerList() throws Exception {
		if(!isDefaultServerListInstalled()) {
			return;
		}

		var log = GTNN.logger;

		log.debug("Attempt to get 'config' field in Config class");
		var configField = DEFAULT_SERVER_LIST_CONFIG.getDeclaredField("config");
		log.debug("Attempt to get 'config' instance in Config class");
		var configObject = configField.get(null);
		log.debug("Attempt to get 'servers' field in ConfigObj class");
		var serversMapField = configObject.getClass().getDeclaredField("servers");
		log.debug("Attempt to get 'servers' instance in ConfigObj class");
		var serversMap = (LinkedHashMap<String, String>) serversMapField.get(configObject);
		log.debug("Nice, everything is done! Clearing list!");
		serversMap.clear();
	}

}
