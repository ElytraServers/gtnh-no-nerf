package cn.elytra.mod.gtnn.mod_v2

import cpw.mods.fml.common.versioning.ComparableVersion

/**
 * The mixin class gather for a module.
 *
 * Works like [buildSet], but it only provides some customized adding functions.
 */
class MixinClassGather(
	private val module: ModuleDefinition,
) {

	private val moduleName get() = module.name

	private val mixinClasses: MutableSet<String> = mutableSetOf()

	fun add(element: String): Boolean = mixinClasses.add(element)
	fun addAll(elements: Collection<String>): Boolean = mixinClasses.addAll(elements)

	operator fun plusAssign(element: String) {
		if(add(element)) {
			ModuleManager.log.info("[+] Added mixin \"{}\" by module {}", element, moduleName)
		}
	}

	operator fun plusAssign(elements: Collection<String>) {
		if(addAll(elements)) {
			ModuleManager.log.info(
				"[+] Added mixins \"{}\" by module {}",
				elements.joinToString(", ", "[", "]"),
				moduleName
			)
		}
	}

	/**
	 * Add mixin classes if [predicate] is true.
	 */
	fun addIf(vararg elements: String, disableModuleIfFailed: Boolean = false, predicate: () -> Boolean) {
		if(predicate()) {
			this += elements.toList()
		} else {
			ModuleManager.log.info(
				"[-] Ignored mixins \"{}\" by module {}",
				elements.joinToString(", ", "[", "]"),
				moduleName
			)
			if(disableModuleIfFailed) {
				module.disable("Required mixins don't meet the requirements!")
			}
		}
	}

	/**
	 * Check if the mod is loaded.
	 *
	 * Forge and LiteLoader mods are both supported.
	 */
	fun isModLoaded(modId: String): Boolean {
		return modId in ModuleManager.loadedMods
	}

	/**
	 * Check if the mod version is valid.
	 *
	 * Only Forge mods are supported.
	 * If the mod is not a valid Forge mod, it always returns `false`.
	 * If the mod is not loaded, it also always returns `false`.
	 *
	 * May need to set the version source preference by [preferMetadataVersion] if the mod provides incorrect version
	 * in the ModContainer (or the annotation).
	 *
	 * @return `true` if the mod is loaded and the version is valid.
	 */
	fun isModVersionValid(modId: String, block: (ComparableVersion) -> Boolean): Boolean {
		if(!isModLoaded(modId)) {
			return false
		}
		val modVersion = getModVersion(modId)
		return modVersion?.let(block) ?: false
	}

	fun build(): Set<String> {
		return if(module.enabled) {
			mixinClasses.toSet()
		} else {
			emptySet()
		}
	}

	companion object {

		/**
		 * A map used to mark the mod version source preference.
		 *
		 * Mod version is obtained from ModContainer by default, but some of them are incorrect, and you need to get
		 * the correct one from metadata.
		 *
		 * Add the modid, and its version will be obtained from its metadata.
		 */
		private val MODS_PREFER_METADATA_VERSION = mutableSetOf<String>()

		private val MOD_VERSION_CACHE = mutableMapOf<String, ComparableVersion?>()
		private val CLASS_EXISTENCE_CACHE = mutableMapOf<String, Boolean>()

		init {
			MODS_PREFER_METADATA_VERSION += "gregtech"
		}

		/**
		 * Called by ModuleManager at FMLLoadComplete.
		 */
		fun clearCaches() {
			MOD_VERSION_CACHE.clear()
			CLASS_EXISTENCE_CACHE.clear()
		}

		fun preferMetadataVersion(modId: String) {
			MODS_PREFER_METADATA_VERSION += modId
		}

		/**
		 * Get the mod version from preferred source (from ModContainer or its metadata).
		 *
		 * @return the comparable version or `null` if the mod is not listed in the Loader (either missing or not a forge mod).
		 * @see MODS_PREFER_METADATA_VERSION
		 */
		fun getModVersion(modId: String): ComparableVersion? {
			return MOD_VERSION_CACHE.getOrPut(modId) {
				if(modId in MODS_PREFER_METADATA_VERSION) { // from metadata
					ModuleManager.getModMetadataVersion(modId)
				} else {
					ModuleManager.getModVersion(modId)
				}.also {
					if(it == null) {
						// log the un-found mod
						ModuleManager.log.info("Failed to find mod dependency [{}]", modId)
					} else {
						// log the mod version for better debugging
						ModuleManager.log.info("Found mod dependecy [{}] with version [{}]", modId, it)
					}
				}
			}
		}
	}

}
