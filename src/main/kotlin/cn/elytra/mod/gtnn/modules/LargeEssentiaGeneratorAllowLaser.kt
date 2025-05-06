package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase

object LargeEssentiaGeneratorAllowLaser : ModuleDefinitionBase("large_essentia_generator_allow_laser") {

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			add("goodgenerator.LEGAllowLaser_MTELargeEssentiaGenerator_Mixin")
		}
	}

}
