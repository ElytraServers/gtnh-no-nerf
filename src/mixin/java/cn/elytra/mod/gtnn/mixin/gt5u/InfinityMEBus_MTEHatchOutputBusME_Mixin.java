package cn.elytra.mod.gtnn.mixin.gt5u;

import gregtech.common.tileentities.machines.MTEHatchOutputBusME;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MTEHatchOutputBusME.class, remap = false)
public class InfinityMEBus_MTEHatchOutputBusME_Mixin {

	@Inject(method = "getCacheCapacity", at = @At("RETURN"), cancellable = true)
	private void gtnn$getCacheCapacity(CallbackInfoReturnable<Long> cir) {
		cir.setReturnValue(Long.MAX_VALUE);
	}

}
