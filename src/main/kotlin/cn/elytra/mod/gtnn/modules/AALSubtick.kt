package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cn.elytra.mod.gtnn.mod_v2.util.gte

object AALSubtick : ModuleDefinitionBase("aal_subtick") {

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			// check version because #4158 broke the injection
			addIf("ggfab.AALSubTick_MTEAdvAssLine_Mixin") { isModVersionValid("gregtech") { it gte "5.09.51.233" } }
		}
	}
}
