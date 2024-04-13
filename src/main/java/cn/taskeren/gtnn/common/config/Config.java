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

	public static boolean useRawMouseMotion;

	static {
		CONF = new Configuration(new File("config/gtnn.cfg"));

		useRawMouseMotion = CONF.getBoolean("use-raw-mouse-motion", "client", true, "Whether enable Raw Mouse Motion or not.");

		CONF.save();
	}

}
