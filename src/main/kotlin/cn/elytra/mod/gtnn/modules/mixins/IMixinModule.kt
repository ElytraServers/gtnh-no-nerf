package cn.elytra.mod.gtnn.modules.mixins

import net.minecraftforge.common.config.Configuration

interface IMixinModule {

	val id: String

	val mixins: Set<String>

	fun isEnabled(): Boolean

	/**
	 * Read the configuration to update the settings for this mixin.
	 */
	fun readConfig(config: Configuration)

	/**
	 * @return `true` means the requirements are all met, like the injecting mods are loaded.
	 */
	fun canApplyMixins(): Boolean

}
