package cn.taskeren.gtnn.mixin.ggfab;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import ggfab.mte.MTEAdvAssLine;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MTEAdvAssLine.class, remap = false)
public class AALSubTick_MTEAdvAssLine_Mixin {

	@Inject(method = "checkProcessing", at = @At(value = "INVOKE", target = "Lggfab/mte/MTEAdvAssLine;isBatchModeEnabled()Z", ordinal = 0))
	private void nn$setMaxParallel(CallbackInfoReturnable<CheckRecipeResult> cir, @Local(ordinal = 2) LocalIntRef maxParallelBeforeBatchMode, @Local(ordinal = 1) LocalIntRef maxParallel, @Local(ordinal = 1) OverclockCalculator calculator) {
		double tickTimeAfterOC = calculator.calculateDurationUnderOneTick();
		if(tickTimeAfterOC < 1) {
			// get the value of updated time
			int value = GTUtility.safeInt((long) (maxParallel.get() / tickTimeAfterOC), 0);

			// set both variable to the value, since we are injecting at the isBatchModeEnabled(),
			// the maxParallelBeforeBatchMode is already assigned to the maxParallel, so we need to manually update it as well.
			maxParallel.set(value);
			maxParallelBeforeBatchMode.set(value);
		}
	}

}
