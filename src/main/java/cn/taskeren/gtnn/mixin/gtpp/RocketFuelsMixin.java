package cn.taskeren.gtnn.mixin.gtpp;

import cn.taskeren.gtnn.GTNN;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static gtPlusPlus.core.item.chemistry.RocketFuels.*;

/**
 * Requested by #7, to revert rocket fuel recipe changes in 2.1.2.3.
 */
@Mixin(value = RocketFuels.class, remap = false)
public class RocketFuelsMixin {

	@Unique
	private static final Marker GTNN$MARKER = MarkerManager.getMarker("RocketFuelsMixin");

	@Inject(method = "createNitrogenTetroxide", at = @At("HEAD"))
	private static void gtnn$createNitrogenTetroxide(CallbackInfo ci) {
		CORE.RA.addChemicalPlantRecipe(
			new ItemStack[]{
				ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 12),
				ItemUtils.getSimpleStack(GenericChem.mOrangeCatalyst, 0),},
			new FluidStack[]{
				FluidUtils.getFluidStack("nitricacid", 4000)
			},
			new ItemStack[]{
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 1)},
			new FluidStack[]{
				FluidUtils.getFluidStack(Nitrogen_Tetroxide, 3000),
			},
			new int[]{100, 100, 50, 50},
			20 * 30,
			MaterialUtils.getVoltageForTier(3),
			3
		);
	}

	@Redirect(method = "createNitrogenTetroxide", at = @At(value = "INVOKE", target = "LgtPlusPlus/xmod/gregtech/api/interfaces/internal/IGregtech_RecipeAdder;addChemicalPlantRecipe([Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;[Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;[IIJI)Z"))
	private static boolean gtnn$createNitrogenTetroxide(IGregtech_RecipeAdder instance, ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs, FluidStack[] aFluidOutputs, int[] aChances, int time, long eu, int aTier) {
		return false;
	}

	@Inject(method = "createHydrazine", at = @At("HEAD"))
	private static void gtnn$createHydrazine(CallbackInfo ci) {
		CORE.RA.addChemicalPlantRecipe(
			new ItemStack[]{
				CI.getNumberedCircuit(21)
			},
			new FluidStack[]{
				FluidUtils.getFluidStack("fluid.hydrogenperoxide", 2000),
				FluidUtils.getFluidStack("ammonia", 2000),
			},
			new ItemStack[]{
			},
			new FluidStack[]{
				FluidUtils.getFluidStack(Hydrazine, 4000),
			},
			20 * 30,
			MaterialUtils.getVoltageForTier(2),
			1
		);

		FluidStack aBartWorksHydrogenPeroxide = FluidUtils.getWildcardFluidStack("Hydrogen Peroxide", 2000);
		if(aBartWorksHydrogenPeroxide != null) {
			GTNN.logger.info(GTNN$MARKER, "Found BW Hydrogen Peroxide, adding compat recipe.");
			CORE.RA.addChemicalPlantRecipe(
				new ItemStack[]{
					CI.getNumberedCircuit(22)
				},
				new FluidStack[]{
					aBartWorksHydrogenPeroxide,
					FluidUtils.getFluidStack("ammonia", 2000),
				},
				new ItemStack[]{
				},
				new FluidStack[]{
					FluidUtils.getFluidStack(Hydrazine, 4000),
				},
				20 * 30,
				MaterialUtils.getVoltageForTier(2),
				1
			);
		}
	}

	@Redirect(method = "createHydrazine", at = @At(value = "INVOKE", target = "LgtPlusPlus/xmod/gregtech/api/interfaces/internal/IGregtech_RecipeAdder;addChemicalPlantRecipe([Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;[Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;IJI)Z"))
	private static boolean gtnn$createHydrazine(IGregtech_RecipeAdder instance, ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs, FluidStack[] aFluidOutputs, int time, long eu, int aTier) {
		return false;
	}

	@Inject(method = "createMonomethylhydrazine", at = @At("HEAD"))
	private static void gtnn$createMonomethylhydrazine(CallbackInfo ci) {
		CORE.RA.addChemicalPlantRecipe(
			new ItemStack[]{
				CI.getNumberedCircuit(21),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 2)
			},
			new FluidStack[]{
				FluidUtils.getFluidStack("hydrogen", 2000),
				FluidUtils.getFluidStack(Hydrazine, 2000),
			},
			new ItemStack[]{
			},
			new FluidStack[]{
				FluidUtils.getFluidStack(Monomethylhydrazine, 4000),
			},
			20 * 48,
			240,
			2
		);
	}

	@Redirect(method = "createMonomethylhydrazine", at = @At(value = "INVOKE", target = "LgtPlusPlus/xmod/gregtech/api/interfaces/internal/IGregtech_RecipeAdder;addChemicalPlantRecipe([Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;[Lnet/minecraft/item/ItemStack;[Lnet/minecraftforge/fluids/FluidStack;IJI)Z"))
	private static boolean gtnn$createMonomethylhydrazine(IGregtech_RecipeAdder instance, ItemStack[] aInputs, FluidStack[] aInputFluids, ItemStack[] aOutputs, FluidStack[] aFluidOutputs, int time, long eu, int aTier) {
		return false;
	}

}
