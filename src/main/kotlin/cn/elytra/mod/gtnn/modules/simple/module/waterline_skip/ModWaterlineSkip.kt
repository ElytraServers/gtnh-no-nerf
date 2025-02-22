package cn.elytra.mod.gtnn.modules.simple.module.waterline_skip

import bartworks.system.material.WerkstoffLoader
import cn.elytra.mod.gtnn.modules.simple.IModule
import goodgenerator.items.GGMaterial
import gregtech.api.enums.*
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipeBuilder
import gregtech.api.util.GTRecipeConstants
import gregtech.api.util.GTUtility
import gtPlusPlus.api.recipe.GTPPRecipeMaps
import gtPlusPlus.core.item.chemistry.GenericChem
import gtPlusPlus.core.material.MaterialsElements
import gtPlusPlus.core.recipe.common.CI
import gtPlusPlus.core.util.minecraft.ItemUtils
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration

object ModWaterlineSkip : IModule {

	override var enabled: Boolean = false

	private var limpidWaterCatalystMetaValue = 29
	private var flawlessWaterCatalystMetaValue = 30

	lateinit var LimpidWaterCatalyst: ItemStack
	lateinit var FlawlessWaterCatalyst: ItemStack

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean("enabled", "qtf_waterline_skip", enabled, "use waterline skip")
		limpidWaterCatalystMetaValue = configuration.getInt(
			"limpid_water_catalyst_meta",
			"qtf_waterline_skip",
			limpidWaterCatalystMetaValue,
			0,
			Int.MAX_VALUE,
			"the meta value of limpid water catalyst",
		)
		flawlessWaterCatalystMetaValue = configuration.getInt(
			"flawless_water_catalyst_meta",
			"qtf_waterline_skip",
			flawlessWaterCatalystMetaValue,
			0,
			Int.MAX_VALUE,
			"the meta value of flawless water catalyst",
		)
	}

	override fun registerGregTechItems() {
		LimpidWaterCatalyst = ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, limpidWaterCatalystMetaValue, 1)
		FlawlessWaterCatalyst =
			ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, flawlessWaterCatalystMetaValue, 1)
		CatalystInject.registerAddedCatalyst(LimpidWaterCatalyst)
		CatalystInject.registerAddedCatalyst(FlawlessWaterCatalyst)
		ItemUtils.addItemToOreDictionary(LimpidWaterCatalyst, "catalystLimpidWater")
		ItemUtils.addItemToOreDictionary(FlawlessWaterCatalyst, "catalystFlawlessWater")
	}

	override fun registerRecipes() {
		// limpid water catalyst
		GTValues.RA.stdBuilder()
			.itemInputs(
				GTUtility.getIntegratedCircuit(10),
				CI.getEmptyCatalyst(1),
				WerkstoffLoader.Hedenbergit.get(OrePrefixes.lens, 1),
				GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1, false),
				ItemList.Circuit_Silicon_Wafer6.get(64)
			)
			.itemOutputs(ItemUtils.getSimpleStack(LimpidWaterCatalyst, 1))
			.fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
			.duration(60 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UEV)
			.setNEIDesc("Added by GTNN: Waterline Skip")
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
			.itemOutputs(ItemUtils.getSimpleStack(FlawlessWaterCatalyst, 1))
			.fluidInputs(GGMaterial.shirabon.getMolten(92160))
			.duration(60 * GTRecipeBuilder.SECONDS)
			.eut(TierEU.RECIPE_UMV)
			.setNEIDesc("Added by GTNN: Waterline Skip")
			.addTo(RecipeMaps.assemblerRecipes);

		// waterline t1-t4
		GTValues.RA.stdBuilder()
			.itemInputs(ItemUtils.getSimpleStack(LimpidWaterCatalyst, 0))
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
			.setNEIDesc("Added by GTNN: Waterline Skip")
			.addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
		// waterline t5-t8
		GTValues.RA.stdBuilder()
			.itemInputs(ItemUtils.getSimpleStack(FlawlessWaterCatalyst, 0))
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
			.setNEIDesc("Added by GTNN: Waterline Skip")
			.addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
	}
}
