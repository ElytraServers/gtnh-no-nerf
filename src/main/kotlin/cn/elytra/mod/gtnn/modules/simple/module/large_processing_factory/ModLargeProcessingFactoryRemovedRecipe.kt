package cn.elytra.mod.gtnn.modules.simple.module.large_processing_factory

import cn.elytra.mod.gtnn.modules.simple.IModule
import gregtech.api.enums.ItemList
import gregtech.api.util.GTModHandler
import gtPlusPlus.core.block.ModBlocks
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.util.minecraft.ItemUtils
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import net.minecraftforge.common.config.Configuration

object ModLargeProcessingFactoryRemovedRecipe : IModule {

	override var enabled: Boolean = true

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean(
			"add-removed-recipe",
			"large-processing-factory",
			enabled,
			"true to add removed recipe of Large Processing Factory"
		)
	}

	override fun registerRecipes() {
		GTModHandler.addCraftingRecipe(
			GregtechItemList.Industrial_MultiMachine.get(1), // TODO: replace this with my LPF
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
