package cn.taskeren.gtnn.mixin.gtpp;

import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_SCSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GregtechMetaTileEntity_LargerTurbineBase;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;

@Mixin(GT_MTE_LargeTurbine_SCSteam.class)
public abstract class MixinGT_MTE_LargeTurbine_SCSteam extends GregtechMetaTileEntity_LargerTurbineBase {
	public MixinGT_MTE_LargeTurbine_SCSteam(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	@Overwrite(remap = false)
	int fluidIntoPower(ArrayList<FluidStack> aFluids, long aOptFlow, int aBaseEff, float[] flowMultipliers) {
		int tEU = 0;
		int totalFlow = 0; // Byproducts are based on actual flow
		int flow = 0;
		this.realOptFlow = (double) aOptFlow * (double) flowMultipliers[0];

		int remainingFlow = MathUtils.safeInt((long) (realOptFlow * 1.25f)); // Allowed to use up to 125% of optimal flow.
		// Variable required outside of loop for
		// multi-hatch scenarios.

		storedFluid = 0;
		FluidStack tSCSteam = FluidRegistry.getFluidStack("supercriticalsteam", 1);
		for (
			int i = 0; i < aFluids.size() && remainingFlow > 0; i++) {
			if (GT_Utility.areFluidsEqual(aFluids.get(i), tSCSteam, true)) {
				flow = Math.min(aFluids.get(i).amount, remainingFlow); // try to use up w/o exceeding remainingFlow
				depleteInput(new FluidStack(aFluids.get(i), flow)); // deplete that amount
				this.storedFluid += aFluids.get(i).amount;
				remainingFlow -= flow; // track amount we're allowed to continue depleting from hatches
				totalFlow += flow; // track total input used
			}
		}
		if (totalFlow <= 0) return 0;
		tEU = totalFlow;

		addOutput(GT_ModHandler.getSteam(totalFlow));
		if (totalFlow != realOptFlow) {
			float efficiency = 1.0f - Math.abs((totalFlow - (float) realOptFlow) / (float) realOptFlow);
			tEU *= efficiency;
			tEU = Math.max(1, MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L));
		} else {
			tEU = MathUtils.safeInt((long) tEU * (long) aBaseEff / 10000L);
		}

		return (int) Math.min(tEU * 100L, Integer.MAX_VALUE);
	}
}
