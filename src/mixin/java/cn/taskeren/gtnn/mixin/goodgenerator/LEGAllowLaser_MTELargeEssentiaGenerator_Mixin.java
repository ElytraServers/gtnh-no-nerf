package cn.taskeren.gtnn.mixin.goodgenerator;

import goodgenerator.blocks.tileEntity.MTELargeEssentiaGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MTELargeEssentiaGenerator.class, remap = false)
public class LEGAllowLaser_MTELargeEssentiaGenerator_Mixin {

	@Inject(method = "checkNoLaser", at = @At("HEAD"), cancellable = true)
	private void nn$checkNoLaser(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

}
