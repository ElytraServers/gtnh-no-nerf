package cn.taskeren.gtnn.mixin.goodgenerator;

import goodgenerator.blocks.tileEntity.LargeEssentiaGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LargeEssentiaGenerator.class)
public class LargeEssentiaGeneratorMixin {

	@Inject(method = "checkNoLaser", at = @At("HEAD"), cancellable = true)
	private void nn$checkNoLaser(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

}
