package cn.taskeren.gtnn.mixin.tectech;

import cpw.mods.fml.common.ProgressManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tectech.loader.MainLoader;

@Mixin(value = MainLoader.class, remap = false)
public class MainLoaderMixin {

	@SuppressWarnings("deprecation")
	@Inject(method = "postLoad", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void gtnn$postLoad(CallbackInfo ci, ProgressManager.ProgressBar bar) {
		gtnn$consumeProgressBar(bar);
		ProgressManager.pop(bar);
	}

	@Unique
	@SuppressWarnings("deprecation")
	private static void gtnn$consumeProgressBar(ProgressManager.ProgressBar bar) {
		if(bar.getSteps() != bar.getStep()) {
			for(var i = 0; i < bar.getSteps() - bar.getStep(); i++) {
				bar.step("CONSUMING THIS BAR!");
			}
		}
	}

}
