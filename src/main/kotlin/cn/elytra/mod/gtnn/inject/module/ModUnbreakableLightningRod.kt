package cn.elytra.mod.gtnn.inject.module

import cn.elytra.mod.gtnn.inject.IMixinModule
import cn.elytra.mod.gtnn.inject.TargetMod
import net.minecraftforge.common.config.Configuration

object ModUnbreakableLightningRod : IMixinModule {

	override var enabled: Boolean = true
	override val mixins: Set<String> = setOf(
		"gt5u.UnbreakableLightningRod_MTELightningRod_Mixin"
	)

	override fun readConfig(config: Configuration) {
		enabled = config.getBoolean("enabled", "unbreakable-lightning-rod", enabled, "")
	}

	override fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean {
		return TargetMod.GregTech in loadedMods
	}
}
