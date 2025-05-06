package cn.elytra.mod.gtnn.mod_v2

import com.google.common.reflect.ClassPath
import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.ModContainer
import cpw.mods.fml.common.event.*
import cpw.mods.fml.common.versioning.ComparableVersion
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import java.io.File

object ModuleManager {

	internal val log = LogManager.getLogger("GTNN Module Manager V2")

	val definitionMap = HashMap<String, ModuleDefinition>()

	/**
	 * The ids of loaded mods. Initialized by the late mixin plugin.
	 */
	internal var loadedMods: Set<String> = emptySet()

	/**
	 * The general configuration for modules.
	 */
	internal val generalConfiguration: Configuration = Configuration(File("config/gtnh-no-nerf_v2.cfg"))

	/**
	 * Check if [discoverAndLoad] has been called.
	 *
	 * It will be called if not when accessing this property, and the value is always true.
	 */
	private val discoveredOnce by lazy { discoverAndLoad(); true }

	@Suppress("UnstableApiUsage")
	internal fun discoverAndLoad() {
		val classloader = Thread.currentThread().contextClassLoader
		val cp = ClassPath.from(classloader)
		val classListInPackage = cp.getTopLevelClassesRecursive("cn.elytra.mod.gtnn.modules")
			.map { Class.forName(it.name, false, classloader) }
		classListInPackage
			.filter { ModuleDefinition::class.java.isAssignableFrom(it) }
			.forEach { cl ->
			try {
				val kcl = cl.kotlin
				// get the instance by the object class instance
				val inst = (kcl.objectInstance ?: let {
					// get the instance by no-arg ctor
					kcl.constructors.firstOrNull { it.parameters.isEmpty() }?.call()
						?: throw NoSuchMethodException("No no-arg constructor found for module class $cl")
				}) as ModuleDefinition
				definitionMap.put(inst.name, inst)
				log.info("Loaded module {} defined by {}", inst.name, cl.canonicalName)
			} catch(e: Throwable) {
				log.error("Failed to load module at {}", cl.canonicalName, e)
			}
		}
	}

	fun gatherMixinsToApply(loadedMods: Set<String>): Set<String> {
		require(discoveredOnce)

		this.loadedMods = loadedMods

		reloadConfig()

		return definitionMap.values
			.flatMap { it.getMixinsToApply() }
			.toSet().also { generalConfiguration.save() }
	}

	fun getModVersion(modId: String): ComparableVersion? {
		return getModContainer(modId)?.version?.let { ComparableVersion(it) }
	}

	fun getModMetadataVersion(modId: String): ComparableVersion? {
		return getModContainer(modId)?.metadata?.version?.let { ComparableVersion(it) }
	}

	fun getModContainer(modId: String): ModContainer? {
		return Loader.instance().modList.firstOrNull { it.modId == modId }
	}

	fun reloadConfig() {
		definitionMap.values.forEach { it.reloadConfig() }
	}

	fun onFMLPreInit(e: FMLPreInitializationEvent) {
		definitionMap.forEach {
			it.value.onFMLPreInit(e)
		}
	}

	fun onFMLInit(e: FMLInitializationEvent) {
		definitionMap.forEach {
			it.value.onFMLInit(e)
		}
	}

	fun onFMLPostInit(e: FMLPostInitializationEvent) {
		definitionMap.forEach {
			it.value.onFMLPostInit(e)
		}
	}

	fun onFMLLoadComplete(e: FMLLoadCompleteEvent) {
		definitionMap.forEach {
			it.value.onFMLLoadComplete(e)
		}

		MixinClassGather.clearCaches()
	}

	fun onFMLServerStarting(e: FMLServerStartingEvent) {
		definitionMap.forEach {
			it.value.onFMLServerStarting(e)
		}
	}

	fun onFMLServerStarted(e: FMLServerStartedEvent) {
		definitionMap.forEach {
			it.value.onFMLServerStarted(e)
		}
	}

	fun onFMLServerStopping(e: FMLServerStoppingEvent) {
		definitionMap.forEach {
			it.value.onFMLServerStopping(e)
		}
	}

	fun onFMLServerStopped(e: FMLServerStoppedEvent) {
		definitionMap.forEach {
			it.value.onFMLServerStopped(e)
		}

		// silently save the configuration
		runCatching { generalConfiguration.save() }
	}

}
