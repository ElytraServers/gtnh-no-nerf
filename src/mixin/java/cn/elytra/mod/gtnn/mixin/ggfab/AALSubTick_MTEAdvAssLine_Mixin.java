package cn.elytra.mod.gtnn.mixin.ggfab;

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

	@Inject(method = "checkProcessing", at = @At(value = "INVOKE_ASSIGN", target = "Lgregtech/api/util/OverclockCalculator;setMaxRegularOverclocks(I)Lgregtech/api/util/OverclockCalculator;", shift = At.Shift.AFTER))
	private void nn$setMaxParallel(CallbackInfoReturnable<CheckRecipeResult> cir, @Local OverclockCalculator calculator, @Local(ordinal = 1) LocalIntRef maxParallel) {
		maxParallel.set(GTUtility.safeInt((long) (maxParallel.get() * calculator.calculateMultiplierUnderOneTick()), 0));
	}

}
