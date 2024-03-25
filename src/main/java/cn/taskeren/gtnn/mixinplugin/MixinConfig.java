package cn.taskeren.gtnn.mixinplugin;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

public class MixinConfig {

	private static final Configuration CONF = new Configuration(new File("gtnn.cfg"));
	private static final String CATEGORY_MIXIN = "mixin";

	private static final Map<Feature, Boolean> VALUES = Maps.newHashMap();

	public static void init() {
		CONF.load();
		VALUES.clear();

		for(var feature : Feature.values()) {
			var value = CONF.getBoolean("Enable" + feature.name(), CATEGORY_MIXIN, true, feature.desc);
			VALUES.put(feature, value);
		}

		CONF.save();
	}

	public static boolean isFeatureEnabled(Feature feature) {
		return VALUES.getOrDefault(feature, true);
	}

}
