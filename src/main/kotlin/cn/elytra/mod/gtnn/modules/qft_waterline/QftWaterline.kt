package cn.elytra.mod.gtnn.modules.qft_waterline

import bartworks.system.material.WerkstoffLoader
import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import goodgenerator.items.GGMaterial
import gregtech.api.enums.*
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipeBuilder
import gregtech.api.util.GTRecipeConstants
import gregtech.api.util.GTUtility
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.material.MaterialsElements
import gtPlusPlus.core.recipe.common.CI
import gtPlusPlus.core.util.minecraft.ItemUtils
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

object QftWaterline : ModuleDefinitionBase("qft_waterline") {

	private const val LIMPID_METADATA = 29
	private const val FLAWLESS_METADATA = 30

	private var limpidWaterCatalyst: ItemStack? = null
	private var flawlessWaterCatalyst: ItemStack? = null

	private const val NEI_DESC = "Added by GTNN"

	override fun onFMLPostInit(e: FMLPostInitializationEvent) {
		val chemItem = GregtechItemList.RedMetalCatalyst.item

		limpidWaterCatalyst = ItemStack(chemItem, LIMPID_METADATA, 1)
		flawlessWaterCatalyst = ItemStack(chemItem, FLAWLESS_METADATA, 1)

		OreDictionary.registerOre("catalystLimpidWater", limpidWaterCatalyst)
		OreDictionary.registerOre("catalystFlawlessWater", flawlessWaterCatalyst)

		// limpid water catalyst
		GTValues.RA.stdBuilder()
			.itemInputs(
				GTUtility.getIntegratedCircuit(10),
				CI.getEmptyCatalyst(1),
				WerkstoffLoader.Hedenbergit.get(OrePrefixes.lens, 1),
				GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1, false),
				ItemList.Circuit_Silicon_Wafer6.get(64)
			)
			.itemOutputs(GTUtility.copyAmount(1, limpidWaterCatalyst))
			.fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
			.duration(60 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UEV)
			.setNEIDesc(NEI_DESC)
			.addTo(RecipeMaps.assemblerRecipes);
		// flawless water catalyst
		GTValues.RA.stdBuilder()
			.itemInputs(
				GTUtility.getIntegratedCircuit(10),
				CI.getEmptyCatalyst(1),
				WerkstoffLoader.Hedenbergit.get(OrePrefixes.lens, 64),
				GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 64, false),
				ItemList.Circuit_Silicon_Wafer6.get(64)
			)
			.itemOutputs(GTUtility.copyAmount(1, flawlessWaterCatalyst))
			.fluidInputs(GGMaterial.shirabon.getMolten(92160))
			.duration(60 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UMV)
			.setNEIDesc(NEI_DESC)
			.addTo(RecipeMaps.assemblerRecipes);

		// waterline t1-t4
		GTValues.RA.stdBuilder()
			.fluidInputs(Materials.Water.getFluid(40_000L))
			.fluidOutputs(
				Materials.Grade1PurifiedWater.getFluid(20_000L),
				Materials.Grade2PurifiedWater.getFluid(10_000L),
				Materials.Grade3PurifiedWater.getFluid(5_000L),
				Materials.Grade4PurifiedWater.getFluid(1_000L)
			)
			.duration(20 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UHV)
			.metadata(GTRecipeConstants.QFT_FOCUS_TIER, 2)
			.metadata(GTRecipeConstants.QFT_CATALYST, GTUtility.copyAmount(1, limpidWaterCatalyst))
			.setNEIDesc(NEI_DESC)
			.addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
		// waterline t5-t8
		GTValues.RA.stdBuilder()
			.fluidInputs(Materials.Water.getFluid(40_000L))
			.fluidOutputs(
				Materials.Grade5PurifiedWater.getFluid(20_000L),
				Materials.Grade6PurifiedWater.getFluid(10_000L),
				Materials.Grade7PurifiedWater.getFluid(5_000L),
				Materials.Grade8PurifiedWater.getFluid(1_000L)
			)
			.duration(20 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UIV)
			.metadata(GTRecipeConstants.QFT_FOCUS_TIER, 4)
			.metadata(GTRecipeConstants.QFT_CATALYST, GTUtility.copyAmount(1, flawlessWaterCatalyst))
			.setNEIDesc(NEI_DESC)
			.addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
	}

}
