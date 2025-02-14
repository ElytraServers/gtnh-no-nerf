package cn.elytra.mod.gtnn.inject

import cn.elytra.mod.gtnn.inject.module.*
import com.falsepattern.gasstation.MinecraftURLClassPath
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files

object MixinLoader {

	private val LOG = LogManager.getLogger("GTNN-Mixin")

	private val MixinModules = arrayOf<IMixinModule>(
		ModUnbreakableLightningRod,
		ModDisassemblerReversedRecipes,
		ModLargeEssentiaGeneratorAllowLaser,
		ModAddCatalyst,
		ModInfinityMEBus,
		ModAdvancedAssemblyLineSubtick,
	)

	private val _loadedMixinModules = mutableListOf<IMixinModule>()

	val loadedMixinModules: List<String> get() = _loadedMixinModules.map { it::class.java.simpleName }

	@JvmStatic
	fun readConfig() {
		val config = Configuration(File(Launch.minecraftHome, "config/gtnn-mixin.cfg"))
		MixinModules.forEach {
			it.readConfig(config)
		}
		config.save()
	}

	@JvmStatic
	fun getMixins(): List<String> {
		val isDev = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean

		val loadedMods = TargetMod.entries.filter { isDev && it.shouldLoadInDev || loadJarOf(it) }

		return buildList<String> {
			MixinModules.forEach { mixinMod ->
				if(mixinMod.enabled) {
					if(mixinMod.isTargetModsLoaded(loadedMods)) {
						mixinMod.mixins.forEach { mixin ->
							add(mixin)
							LOG.info("Loaded Mixin $mixin for $mixinMod")
						}
						_loadedMixinModules += mixinMod
					} else {
						LOG.info("Skipped Mixin Module $mixinMod, required Mods are not loaded")
					}
				}
			}
		}
	}

	private fun loadJarOf(mod: TargetMod): Boolean {
		try {
			val jar = Files.walk(File(Launch.minecraftHome, "mods/").toPath())
				.filter { mod.isMatchingJar(it) }
				.map { it.toFile() }
				.findFirst()
				.orElse(null)
			if(jar == null) {
				LOG.info("Jar not found for $mod")
				return false
			}

			LOG.info("Attempting to add $jar to the URL Class Path")

			if(!jar.exists()) throw FileNotFoundException(jar.toString())
			MinecraftURLClassPath.addJar(jar)

			return true
		} catch(e: Throwable) {
			LOG.error("Failed to load $mod", e)
			return false
		}
	}

}
