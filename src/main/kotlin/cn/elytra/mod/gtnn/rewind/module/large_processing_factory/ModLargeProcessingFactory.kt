package cn.elytra.mod.gtnn.rewind.module.large_processing_factory

import cn.elytra.mod.gtnn.rewind.IModule
import cn.elytra.mod.gtnn.util.copyOf
import gregtech.api.enums.ItemList
import gregtech.api.util.GTModHandler
import gtPlusPlus.core.block.ModBlocks
import gtPlusPlus.core.material.MaterialsAlloy
import gtPlusPlus.core.util.minecraft.ItemUtils
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration

// see https://github.com/GTNewHorizons/GT5-Unofficial/pull/3086
object ModLargeProcessingFactory : IModule {

	override var enabled = true

	private var useGtnnVersion: Boolean = false
	private var largeProcessingFactoryId = 680
	private var useDeprecatedRecipe: Boolean = true

	lateinit var LargeProcessingFactory: ItemStack

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean(
			"enabled",
			"large_processing_factory",
			enabled,
			"enable gtnn large processing factory module"
		)
		useGtnnVersion = configuration.getBoolean(
			"use-gtnn-version",
			"large_processing_factory",
			useGtnnVersion,
			"enable gtnn version of Large Processing Factory"
		)
		largeProcessingFactoryId = configuration.getInt(
			"id",
			"large_processing_factory",
			largeProcessingFactoryId,
			0,
			32767,
			"the mte id of gtnn large processing factory"
		)
		useDeprecatedRecipe = configuration.getBoolean(
			"use-deprecated-recipe",
			"large_processing_factory",
			useDeprecatedRecipe,
			"add the recipe for the deprecated one before its removal"
		)
	}

	override fun registerGregTechItems() {
		if(useGtnnVersion) {
			LargeProcessingFactory = MTELargeProcessingFactory(
				10680,
				"industrialmultimachine.controller.tier.single",
				"Large Processing Factory",
			).getStackForm(1)
		}
	}

	override fun registerRecipes() {
		if(useGtnnVersion) {
			GTModHandler.addShapelessCraftingRecipe(
				LargeProcessingFactory.copyOf(),
				IModule.DefaultMachineRecipeMask,
				arrayOf(GregtechItemList.Industrial_MultiMachine.get(1))
			)
		}

		if(useDeprecatedRecipe) {
			GTModHandler.addCraftingRecipe(
				GregtechItemList.Industrial_MultiMachine.get(1), // TODO: replace this with my LPF
				arrayOf(
					"abc", "ded", "fgh",
					'a', ItemList.Machine_IV_Compressor.get(1),
					'b', ItemList.Machine_IV_Lathe.get(1),
					'c', ItemList.Machine_IV_Polarizer.get(1),
					'd', MaterialsAlloy.STABALLOY.getPlate(1),
					'e', ItemUtils.getSimpleStack(ModBlocks.blockProjectTable),
					'f', ItemList.Machine_IV_Fermenter.get(1),
					'g', ItemList.Machine_IV_FluidExtractor.get(1),
					'h', ItemList.Machine_IV_Extractor.get(1),
				)
			)
		}
	}
}
