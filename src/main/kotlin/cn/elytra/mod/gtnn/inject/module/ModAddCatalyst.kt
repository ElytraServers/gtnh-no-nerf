package cn.elytra.mod.gtnn.inject.module

import cn.elytra.mod.gtnn.inject.IMixinModule
import cn.elytra.mod.gtnn.inject.TargetMod
import net.minecraftforge.common.config.Configuration

object ModAddCatalyst : IMixinModule {

	override val enabled: Boolean = true
	override val mixins: Set<String> = setOf(
		"gtpp.AddCatalyst_ItemUtils_Mixin"
	)

	override fun readConfig(config: Configuration) {
	}

	override fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean {
		return TargetMod.GregTech in loadedMods
	}
}
