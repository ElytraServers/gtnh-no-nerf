package cn.elytra.mod.gtnn.mod_v2

import cpw.mods.fml.common.event.*

interface ModuleDefinition {

	val name: String

	var enabled: Boolean

	fun reloadConfig() {
	}

	fun getMixinsToApply(): Set<String> {
		return emptySet()
	}

	fun onFMLPreInit(e: FMLPreInitializationEvent) {
	}

	fun onFMLInit(e: FMLInitializationEvent) {
	}

	fun onFMLPostInit(e: FMLPostInitializationEvent) {
	}

	fun onFMLLoadComplete(e: FMLLoadCompleteEvent) {
	}

	fun onFMLServerStarting(e: FMLServerStartingEvent) {
	}

	fun onFMLServerStarted(e: FMLServerStartedEvent) {
	}

	fun onFMLServerStopping(e: FMLServerStoppingEvent) {
	}

	fun onFMLServerStopped(e: FMLServerStoppedEvent) {
	}

	fun disable(reason: String) {
		enabled = false
		ModuleManager.log.warn("[!] Module $name is disabled: $reason")
	}
}
