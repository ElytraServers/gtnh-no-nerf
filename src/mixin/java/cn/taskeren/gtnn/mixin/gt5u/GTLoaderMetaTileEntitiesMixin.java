package cn.taskeren.gtnn.mixin.gt5u;

import gregtech.loaders.preload.LoaderMetaTileEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LoaderMetaTileEntities.class, remap = false)
public class GTLoaderMetaTileEntitiesMixin {

	@Inject(method = "run", at = @At("RETURN"))
	private void nn$run2(CallbackInfo ci) {
	}

}
