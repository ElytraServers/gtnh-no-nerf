package cn.taskeren.gtnn.mixin.newhorizonscore;

import com.dreammaster.gthandler.GT_Loader_Machines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GT_Loader_Machines.class, remap = false)
public class GTLoaderMachinesMixin {

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/dreammaster/gthandler/GT_Loader_Machines;recipes()V", shift = At.Shift.BEFORE))
	private void nn$registerBeforeRecipe(CallbackInfo ci) {
	}

}
