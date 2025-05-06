package cn.elytra.mod.gtnn.modules.processing_array

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cn.elytra.mod.gtnn.util.DefaultMachineRecipeMask
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import gregtech.api.GregTechAPI
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack

object ProcessingArray : ModuleDefinitionBase("processing_array") {

	private const val MTE_ID = 1199
	private var NNProcessingArray: ItemStack? = null

	override fun onFMLPostInit(e: FMLPostInitializationEvent) {
		if(GregTechAPI.METATILEENTITIES[MTE_ID] == null) {
			log.info("Processing Array is fully removed! Registering NN-customized Processing Array.")
			NNProcessingArray = MTEProcessingArray(
				MTE_ID,
				"industrialmultimachine.controller.tier.single",
				"Processing Array"
			).getStackForm(1)
		}

		GTModHandler.addCraftingRecipe(
			NNProcessingArray ?: ItemList.Processing_Array.get(1L),
			DefaultMachineRecipeMask,
			arrayOf(
				"CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B',
				OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C', OrePrefixes.circuit.get(Materials.IV), 'F',
				ItemList.Robot_Arm_EV, 'T', ItemList.Energy_LapotronicOrb
			)
		)
	}

}
