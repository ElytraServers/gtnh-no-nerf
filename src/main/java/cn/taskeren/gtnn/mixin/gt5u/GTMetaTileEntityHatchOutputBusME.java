package cn.taskeren.gtnn.mixin.gt5u;

import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GT_MetaTileEntity_Hatch_OutputBus_ME.class)
public class GTMetaTileEntityHatchOutputBusME {

	/**
	 * INJECT: overwrites the capacity of the hatch to Long.MAX_VALUE.
	 */
	@Inject(method = "getCacheCapacity", at = @At("RETURN"), cancellable = true)
	private void gtnn$getCacheCapacity(CallbackInfoReturnable<Long> cir) {
		cir.setReturnValue(Long.MAX_VALUE);
	}

}
