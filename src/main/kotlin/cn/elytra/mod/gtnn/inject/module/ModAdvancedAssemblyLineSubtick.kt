package cn.elytra.mod.gtnn.inject.module

import cn.elytra.mod.gtnn.inject.IMixinModule
import cn.elytra.mod.gtnn.inject.TargetMod
import net.minecraftforge.common.config.Configuration

object ModAdvancedAssemblyLineSubtick : IMixinModule {

	override var enabled: Boolean = true
	override val mixins: Set<String> = setOf("ggfab.AALSubTick_MTEAdvAssLine_Mixin")

	override fun readConfig(config: Configuration) {
		enabled = config.getBoolean("enabled", "aal-subtick", enabled, "")
	}

	override fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean {
		return TargetMod.GregTech in loadedMods
	}
}
