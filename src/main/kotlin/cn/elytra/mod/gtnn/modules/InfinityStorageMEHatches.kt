package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase

object InfinityStorageMEHatches : ModuleDefinitionBase("infinity_storage_me_hatches") {

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			add("gt5u.InfinityMEBus_MTEHatchOutputME_Mixin")
			add("gt5u.InfinityMEBus_MTEHatchOutputBusME_Mixin")
		}
	}

}
