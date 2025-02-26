package cn.elytra.mod.gtnn.modules.simple.module.indium_comb_proc

import cn.elytra.mod.gtnn.GTNN
import cn.elytra.mod.gtnn.modules.simple.IModule
import gregtech.api.enums.Materials
import gregtech.api.enums.Mods
import gregtech.common.items.CombType
import gregtech.loaders.misc.GTBees
import net.minecraftforge.common.config.Configuration

object ModIndiumCombProc : IModule {

	override var enabled: Boolean = false

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean("enabled", "indium_comb_proc", enabled, "use indium comb proc")
	}

	override fun registerRecipes() {
		if(Mods.Forestry.isModLoaded) {
			GTBees.combs.addProcessGT(CombType.INDIUM, arrayOf(Materials.Indium), CombType.INDIUM.voltage)
		} else {
			GTNN.logger.warn("ForestryMC is not loaded, Indium Comb Proc is not working")
		}
	}

}
