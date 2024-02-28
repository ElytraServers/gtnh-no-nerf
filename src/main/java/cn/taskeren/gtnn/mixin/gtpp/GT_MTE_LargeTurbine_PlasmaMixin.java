package cn.taskeren.gtnn.mixin.gtpp;

import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Plasma;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GT_MTE_LargeTurbine_Plasma.class, remap = false)
public class GT_MTE_LargeTurbine_PlasmaMixin {
	@Redirect(method = "checkProcessing", at = @At(target = "Ljava/lang/Math;min(FF)F", value = "INVOKE"))
	public float removeEffLoss(float v, float u) {
		return 1.0f;
	}
}
