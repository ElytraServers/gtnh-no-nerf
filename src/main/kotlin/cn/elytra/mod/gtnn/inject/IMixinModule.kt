package cn.elytra.mod.gtnn.inject

import net.minecraftforge.common.config.Configuration

interface IMixinModule {

	val enabled: Boolean
	val mixins: Set<String>

	fun readConfig(config: Configuration)

	fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean

}
