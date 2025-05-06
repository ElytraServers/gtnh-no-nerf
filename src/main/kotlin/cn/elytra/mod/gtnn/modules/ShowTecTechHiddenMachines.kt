package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase

object ShowTecTechHiddenMachines : ModuleDefinitionBase("show-tec-tech-hidden-machines") {

	override fun gatherMixinsToApply(): Set<String> {
		return setOf("tectech.NoHide_MachineLoader_Mixin")
	}

}
