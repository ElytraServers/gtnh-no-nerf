package cn.elytra.mod.gtnn.modules

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.common.items.CombType
import gregtech.loaders.misc.GTBees

object IndiumCombProcessing : ModuleDefinitionBase("indium_comb_processing") {

	override fun onFMLPostInit(e: FMLPostInitializationEvent) {
		if(Mods.Forestry.isModLoaded) {
			GTBees.combs.addProcessGT(CombType.INDIUM, arrayOf(Materials.Indium), CombType.INDIUM.voltage)
		} else {
			disable("Forestry is missing")
		}
	}

}
