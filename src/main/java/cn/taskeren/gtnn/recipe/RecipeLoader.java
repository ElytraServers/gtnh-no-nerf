package cn.taskeren.gtnn.recipe;

import bartworks.system.material.WerkstoffLoader;
import cn.taskeren.gtnn.mod.gtPlusPlus.GenericChemExt;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.*;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RecipeLoader {

    public static final Logger logger = LogManager.getLogger();

    public static void loadAll() {
        loadChemicalSkips();
    }

    private static void loadChemicalSkips() {
        logger.info("Adding waterline skip recipes");

        // limpid water catalyst
        GTValues.RA.stdBuilder()
                .itemInputs(
                        GTUtility.getIntegratedCircuit(10),
                        CI.getEmptyCatalyst(1),
                        WerkstoffLoader.Hedenbergit.get(OrePrefixes.lens, 1),
                        GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1, false),
                        ItemList.Circuit_Silicon_Wafer6.get(64)
                )
                .itemOutputs(ItemUtils.getSimpleStack(GenericChemExt.mLimpidWaterCatalyst, 1))
                .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(9216))
                .duration(60 * GTRecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_UEV)
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
                .itemOutputs(ItemUtils.getSimpleStack(GenericChemExt.mFlawlessWaterCatalyst, 1))
                .fluidInputs(GGMaterial.shirabon.getMolten(92160))
                .duration(60 * GTRecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_UMV)
                .addTo(RecipeMaps.assemblerRecipes);

        // waterline t1-t4
        GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getSimpleStack(GenericChemExt.mLimpidWaterCatalyst, 0))
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
                .addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
        // waterline t5-t8
        GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getSimpleStack(GenericChemExt.mFlawlessWaterCatalyst, 0))
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
                .addTo(GTPPRecipeMaps.quantumForceTransformerRecipes);
    }

}
