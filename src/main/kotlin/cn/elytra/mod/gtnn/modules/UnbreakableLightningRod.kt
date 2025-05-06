package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase

object UnbreakableLightningRod : ModuleDefinitionBase("unbreakable_lightning_rod") {

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			add("gt5u.UnbreakableLightningRod_MTELightningRod_Mixin")
		}
	}

}
