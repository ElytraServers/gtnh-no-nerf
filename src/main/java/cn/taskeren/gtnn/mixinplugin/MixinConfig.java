package cn.taskeren.gtnn.mixinplugin;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

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
			var value = getProperty(feature).getBoolean();
			VALUES.put(feature, value);
		}

		CONF.save();
	}

	private static Property getProperty(Feature feature) {
		return CONF.get(CATEGORY_MIXIN, "Enable" + feature.name(), feature.isDefaultEnabled(), feature.desc);
	}

	public static boolean isFeatureEnabled(Feature feature) {
		return VALUES.getOrDefault(feature, true);
	}

	/**
	 * Set the enable/disable value of the feature.
	 * <p>
	 * It requires a restart to take effect.
	 */
	public static void updateValue(Feature feature, boolean value) {
		getProperty(feature).set(value);
		CONF.save();
	}

}
