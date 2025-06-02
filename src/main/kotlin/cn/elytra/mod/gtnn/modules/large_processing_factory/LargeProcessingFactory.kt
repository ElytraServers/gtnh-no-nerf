package cn.elytra.mod.gtnn.modules.large_processing_factory

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cn.elytra.mod.gtnn.util.DefaultMachineRecipeMask
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import gregtech.api.enums.ItemList
import gregtech.api.util.GTModHandler
import gtPlusPlus.core.block.ModBlocks
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.util.minecraft.ItemUtils
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialMultiMachine
import net.minecraft.item.ItemStack

object LargeProcessingFactory : ModuleDefinitionBase("processing_factory") {

	private const val MTE_ID = 860
	private var NNLargeProcessingFactory: ItemStack? = null

	override fun onFMLPostInit(e: FMLPostInitializationEvent) {
		NNLargeProcessingFactory = MTEIndustrialMultiMachine(
			MTE_ID,
			"industrialmultimachine.controller.tier.single",
			"Large Processing Factory"
		).getStackForm(1)

		GTModHandler.addCraftingRecipe(
			NNLargeProcessingFactory,
			DefaultMachineRecipeMask,
			arrayOf(
				"ABC", "DED", "FGH",
				'A', ItemList.Machine_IV_Compressor.get(1),
				'B', ItemList.Machine_IV_Lathe.get(1),
				'C', ItemList.Machine_IV_Polarizer.get(1),
				'D', MaterialsAlloy.STABALLOY.getPlate(1),
				'E', ItemUtils.getSimpleStack(ModBlocks.blockProjectTable),
				'F', ItemList.Machine_IV_Fermenter.get(1),
				'G', ItemList.Machine_IV_FluidExtractor.get(1),
				'H', ItemList.Machine_IV_Extractor.get(1),
			)
		)
	}

}
