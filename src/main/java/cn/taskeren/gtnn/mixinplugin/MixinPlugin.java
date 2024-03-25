package cn.taskeren.gtnn.mixinplugin;

import com.gtnewhorizon.gtnhmixins.MinecraftURLClassPath;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.nio.file.Files.walk;

public class MixinPlugin implements IMixinConfigPlugin {

	private static final Logger LOG = LogManager.getLogger("GTNNMixins");
	private static final Path MODS_DIRECTORY_PATH = new File(Launch.minecraftHome, "mods/").toPath();

	@Override
	public void onLoad(String mixinPackage) {
		MixinConfig.init();
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	// This method return a List<String> of mixins. Every mixins in this list will be loaded.
	@Override
	public List<String> getMixins() {
		boolean isInDev = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");

		List<TargetedMod> loadedMods = Arrays.stream(TargetedMod.values())
			.filter(x -> (isInDev && x.shouldLoadInDev) || loadJarOf(x))
			.collect(Collectors.toList());

		for(TargetedMod mod : TargetedMod.values()) {
			if(loadedMods.contains(mod)) {
				LOG.info("Found " + mod.modName + "! Integrating now...");
			} else {
				LOG.info("Could not find " + mod.modName + "! Skipping integration....");
			}
		}

		List<String> mixins = new ArrayList<>();

		for(Feature feature : Feature.values()) { // iterate all features
			if(!feature.isEnabled()) {
				LOG.info("Feature " + feature.name() + " is disabled!");
				continue;
			}
			if(!feature.isTargetedModsLoad(loadedMods)) {
				LOG.warn("Feature " + feature.name() + " doesn't find all of its dependency mods!");
				continue;
			}
			for(Mixin mixin : feature.mixins) {
				mixins.add(mixin.mixinClass);
				LOG.debug("Loading mixin class: " + mixin.mixinClass);
			}
			LOG.info("Feature " + feature.name() + " is loaded.");
		}
		return mixins;
	}

	private boolean loadJarOf(final TargetedMod mod) {
		try {
			File jar = findJarOf(mod);
			if(jar == null) {
				LOG.info("Jar not found for " + mod);
				return false;
			}

			LOG.info("Attempting to add " + jar + " to the URL Class Path");
			if(!jar.exists()) {
				throw new FileNotFoundException(jar.toString());
			}
			MinecraftURLClassPath.addJar(jar);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static File findJarOf(final TargetedMod mod) {
		try(var stream = walk(MODS_DIRECTORY_PATH)) {
			return stream.filter(mod::isMatchingJar).map(Path::toFile).findFirst().orElse(null);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}
