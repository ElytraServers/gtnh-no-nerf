package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cn.elytra.mod.gtnn.mod_v2.util.gte

object BBFAutomation : ModuleDefinitionBase("bbf_automation") {

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			addIf("gt5u.BasicBlastFurnace_Automation") { isModVersionValid("gregtech") { it gte "5.09.51.205" } }
		}
	}

}
