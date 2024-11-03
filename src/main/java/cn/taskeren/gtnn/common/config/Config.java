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

	public static int getMachineId(String machineId, int defaultId, boolean saveAfterAccess) {
		int value = CONF.getInt("machine-id-"+machineId, "machine", defaultId, -1, Short.MAX_VALUE, "The MetaTileEntity Id for " + machineId + ". Don't change unless there is a id conflict!");
		if(saveAfterAccess) {
			CONF.save();
		}
		return value;
	}

}
