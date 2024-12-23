package cn.elytra.mod.gtnn.inject.module

import cn.elytra.mod.gtnn.inject.IMixinModule
import cn.elytra.mod.gtnn.inject.TargetMod
import net.minecraftforge.common.config.Configuration

object ModLargeEssentiaGeneratorAllowLaser : IMixinModule {

	override var enabled: Boolean = true
	override val mixins: Set<String> = setOf(
		"goodgenerator.LEGAllowLaser_MTELargeEssentiaGenerator_Mixin"
	)

	override fun readConfig(config: Configuration) {
		enabled = config.getBoolean("enabled", "large-essentia-generator-allow-laser", enabled, "")
	}

	override fun isTargetModsLoaded(loadedMods: List<TargetMod>): Boolean {
		return TargetMod.GoodGenerator in loadedMods
	}
}
