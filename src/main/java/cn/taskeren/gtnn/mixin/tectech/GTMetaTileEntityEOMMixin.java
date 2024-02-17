package cn.taskeren.gtnn.mixin.tectech;

import cn.taskeren.gtnn.mod.tectech.EOHHelper;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_EyeOfHarmony;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.FluidStackLong;
import com.github.technus.tectech.util.ItemStackLong;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static java.lang.Math.pow;

@Mixin(value = GT_MetaTileEntity_EM_EyeOfHarmony.class, remap = false)
public abstract class GTMetaTileEntityEOMMixin extends GT_MetaTileEntity_MultiblockBase_EM {

	// region SHADOW VARIABLES
	@Shadow
	private EyeOfHarmonyRecipe currentRecipe;

	@Shadow
	private int timeAccelerationFieldMetadata;

	@Shadow
	@Final
	private static double TIME_ACCEL_DECREASE_CHANCE_PER_TIER;

	@Shadow
	private int stabilisationFieldMetadata;

	@Shadow
	@Final
	private static double STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;

	@Shadow
	private double hydrogenOverflowProbabilityAdjustment;

	@Shadow
	private double heliumOverflowProbabilityAdjustment;

	@Shadow
	private long currentCircuitMultiplier;

	@Shadow
	private long astralArrayAmount;

	@Shadow
	private long parallelAmount;

	@Shadow
	@Final
	public static boolean EOH_DEBUG_MODE;

	@Shadow
	protected abstract long getStellarPlasmaStored();

	@Shadow
	@Final
	private static double LOG_CONSTANT;

	@Shadow
	protected abstract long getHydrogenStored();

	@Shadow
	protected abstract long getHeliumStored();

	@Shadow
	private int spacetimeCompressionFieldMetadata;

	@Shadow
	private long startEU;

	@Shadow
	private BigInteger outputEU_BigInt;

	@Shadow
	@Final
	private static int TOTAL_CASING_TIERS_WITH_POWER_PENALTY;

	@Shadow
	private BigInteger usedEU;

	@Shadow
	protected abstract int recipeProcessTimeCalculator(long recipeTime, long recipeSpacetimeCasingRequired);

	@Shadow
	protected abstract void calculateInputFluidExcessValues(long hydrogenRecipeRequirement, long heliumRecipeRequirement);

	@Shadow
	private String userUUID;

	@Shadow
	private double stellarPlasmaOverflowProbabilityAdjustment;

	@Shadow
	private double successChance;

	@Shadow
	protected abstract double recipeChanceCalculator();

	@Shadow
	private long currentRecipeRocketTier;

	@Shadow
	@Final
	private Map<Fluid, Long> validFluidMap;

	@Shadow
	protected abstract double recipeYieldCalculator();

	@Shadow
	private List<FluidStackLong> outputFluids;

	@Shadow
	private List<ItemStackLong> outputItems;

	@Shadow
	private long successfulParallelAmount;

	@Shadow
	private boolean animationsEnabled;

	@Shadow
	protected abstract void createRenderBlock(EyeOfHarmonyRecipe currentRecipe);

	@Shadow
	private boolean recipeRunning;

	@Shadow
	@Final
	private static long MOLTEN_SPACETIME_PER_FAILURE_TIER;

	@Shadow
	@Final
	private static double SPACETIME_FAILURE_BASE;
	// endregion

	@Shadow
	protected abstract void outputFluidToAENetwork(FluidStack fluid, long amount);

	// region FAKE CTOR
	protected GTMetaTileEntityEOMMixin(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	protected GTMetaTileEntityEOMMixin(String aName) {
		super(aName);
	}
	// endregion

	@Inject(method = "recipeChanceCalculator", at = @At("RETURN"), cancellable = true)
	private void nn$recipeChanceCalculator(CallbackInfoReturnable<Double> cir) {
		double chance = currentRecipe.getBaseRecipeSuccessChance()
			- timeAccelerationFieldMetadata * TIME_ACCEL_DECREASE_CHANCE_PER_TIER
			+ stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER
			- hydrogenOverflowProbabilityAdjustment
			- heliumOverflowProbabilityAdjustment;

		cir.setReturnValue(MathHelper.clamp_double(chance, 0.0, 1.0));
	}

	@Inject(method = "recipeYieldCalculator", at = @At("RETURN"), cancellable = true)
	private void nn$recipeYieldCalculator(CallbackInfoReturnable<Double> cir) {
		var yield = 1.0 - hydrogenOverflowProbabilityAdjustment
			- heliumOverflowProbabilityAdjustment
			- stabilisationFieldMetadata * STABILITY_INCREASE_PROBABILITY_DECREASE_YIELD_PER_TIER;

		cir.setReturnValue(MathHelper.clamp_double(yield, 0.0, 1.0));
	}

	/**
	 * @author Taskeren
	 * @reason to replace the new logic that used above 2.5.0
	 */
	@Overwrite
	public CheckRecipeResult processRecipe(EyeOfHarmonyRecipe recipeObject) {

		// Get circuit damage, clamp it and then use it later for overclocking.
		for (ItemStack itemStack : mInputBusses.get(0).getRealInventory()) {
			if (GT_Utility.isAnyIntegratedCircuit(itemStack)) {
				currentCircuitMultiplier = MathHelper.clamp_int(itemStack.getItemDamage(), 0, 24);
				break;
			}
		}

		// Debug mode, overwrites the required fluids to initiate the recipe to 100L of each.
		if ((EOH_DEBUG_MODE && getHydrogenStored() < 100)
			|| (getHydrogenStored() < currentRecipe.getHydrogenRequirement())) {
			return SimpleCheckRecipeResult.ofFailure("no_hydrogen");
		}
		if ((EOH_DEBUG_MODE && getHeliumStored() < 100) || (getHeliumStored() < currentRecipe.getHeliumRequirement())) {
			return SimpleCheckRecipeResult.ofFailure("no_helium");
		}

		if (spacetimeCompressionFieldMetadata == -1) {
			return CheckRecipeResultRegistry
				.insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
		}

		// Check tier of spacetime compression blocks is high enough.
		if ((spacetimeCompressionFieldMetadata + 1) < recipeObject.getSpacetimeCasingTierRequired()) {
			return CheckRecipeResultRegistry
				.insufficientMachineTier((int) recipeObject.getSpacetimeCasingTierRequired());
		}

		startEU = recipeObject.getEUStartCost();

		// Remove EU from the users network.
		long usedEU = (long) (-startEU * (Math.log(currentCircuitMultiplier + 1) / EOHHelper.LOG_BASE_CONSTANT + 1)
			* pow(0.77, currentCircuitMultiplier));
		if (!addEUToGlobalEnergyMap(userUUID, usedEU)) {
			return CheckRecipeResultRegistry.insufficientPower(usedEU);
		}

		mMaxProgresstime = recipeProcessTimeCalculator(
			recipeObject.getRecipeTimeInTicks(),
			recipeObject.getSpacetimeCasingTierRequired());

		calculateInputFluidExcessValues(
			recipeObject.getHydrogenRequirement(),
			recipeObject.getHeliumRequirement());

		if (EOH_DEBUG_MODE) {
			hydrogenOverflowProbabilityAdjustment = 0;
			heliumOverflowProbabilityAdjustment = 0;
		}

		successChance = recipeChanceCalculator();
		currentRecipeRocketTier = currentRecipe.getRocketTier();

		// Determine EU recipe output.
		// outputEU = recipeObject.euOutput * pow(0.77, currentCircuitMultiplier)
		outputEU_BigInt = BigInteger.valueOf(recipeObject.getEUOutput()).multiply(
			BigDecimal.valueOf(0.77).pow((int) currentCircuitMultiplier).toBigInteger()
		);

		// Reduce internal storage by hydrogen and helium quantity required for recipe.
		validFluidMap.put(Materials.Hydrogen.mGas, 0L);
		validFluidMap.put(Materials.Helium.mGas, 0L);

		double yield = recipeYieldCalculator();
		if (EOH_DEBUG_MODE) {
			successChance = 1; // Debug recipes, sets them to 100% output chance.
		}

		// Return copies of the output objects.
		outputFluids = recipeObject.getOutputFluids();
		outputItems = recipeObject.getOutputItems();

		if (yield != 1.0) {
			// Iterate over item output list and apply yield values.
			for (ItemStackLong itemStackLong : outputItems) {
				itemStackLong.stackSize *= yield;
			}

			// Iterate over fluid output list and apply yield values.
			for (FluidStack fluidStack : mOutputFluids) {
				fluidStack.amount *= yield;
			}
		}

		updateSlots();

		if (animationsEnabled) {
			createRenderBlock(currentRecipe);
		}

		recipeRunning = true;
		return CheckRecipeResultRegistry.SUCCESSFUL;
	}

	/**
	 * @author Taskeren
	 * @reason to replace the new logic that used above 2.5.0
	 */
	@Overwrite
	private void outputFailedChance() {
		var amount = BigDecimal.valueOf(successChance)
			.multiply(BigDecimal.valueOf(MOLTEN_SPACETIME_PER_FAILURE_TIER))
			.multiply(BigDecimal.valueOf(SPACETIME_FAILURE_BASE).pow((int) (currentRecipeRocketTier + 1)));
		outputFluidToAENetwork(
			MaterialsUEVplus.SpaceTime.getMolten(1),
			amount.longValue()
		);
		super.outputAfterRecipe_EM();
	}

}
