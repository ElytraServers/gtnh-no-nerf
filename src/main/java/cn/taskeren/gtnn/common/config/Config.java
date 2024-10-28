package cn.taskeren.gtnn.common.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * The common mod configurations.
 * <p>
 * If you are looking for the Mixin configurations, go to {@link cn.taskeren.gtnn.mixinplugin.MixinConfig MixinConfig}.
 */
public class Config {

	private static final Configuration CONF;

	public static boolean removeDefaultServerList;

	static {
		CONF = new Configuration(new File("config/gtnn.cfg"));

		removeDefaultServerList = CONF.getBoolean("remove-default-server-list", "client", true, "Whether remove the Default Server List.");

		CONF.save();
	}

}
