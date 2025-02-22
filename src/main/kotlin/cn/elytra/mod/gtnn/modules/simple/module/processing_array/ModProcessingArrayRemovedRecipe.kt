package cn.elytra.mod.gtnn.modules.simple.module.processing_array

import cn.elytra.mod.gtnn.modules.simple.IModule
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import net.minecraftforge.common.config.Configuration

object ModProcessingArrayRemovedRecipe : IModule {

	override var enabled: Boolean = true

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean(
			"add-removed-recipe",
			"processing-array",
			enabled,
			"true to add removed recipe of Processing Array"
		)
	}

	override fun registerRecipes() {
		GTModHandler.addCraftingRecipe(
			ItemList.Processing_Array.get(1L), // TODO: replace this with my PA
			IModule.Companion.DefaultMachineRecipeMask,
			arrayOf(
				"CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B',
				OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'F',
				ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb
			)
		)
	}
}
