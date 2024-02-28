package cn.taskeren.gtnn.mixin.gtpp;

import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Plasma;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GT_MTE_LargeTurbine_Plasma.class)
public class MixinGT_MTE_LargeTurbine_Plasma {
	@Redirect(method = "checkProcessing", at = @At(target = "Ljava/lang/Math;min(FF)F", value = "INVOKE", remap = false), remap = false)
	public float removeEffLoss(float v, float u) {
		return 1.0f;
	}
}
