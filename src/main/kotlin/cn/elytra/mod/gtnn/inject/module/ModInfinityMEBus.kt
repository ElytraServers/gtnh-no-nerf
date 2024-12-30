package cn.elytra.mod.gtnn.inject.module

import cn.elytra.mod.gtnn.inject.IMixinModule
import cn.elytra.mod.gtnn.inject.TargetMod
import net.minecraftforge.common.config.Configuration

object ModInfinityMEBus : IMixinModule {

	override var enabled: Boolean = true
	override val mixins: Set<String> = setOf(
		"gt5u.InfinityMEBus_MTEHatchOutputME_Mixin",
		"gt5u.InfinityMEBus_MTEHatchOutputBusME_Mixin",
	)

	override fun readConfig(config: Configuration) {
		enabled = config.getBoolean("enabled", "infinity-me-bus", enabled, "")
	}

	override fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean {
		return TargetMod.GregTech in loadedMods
	}
}
