package cn.taskeren.gtnn.mixin.gtpp;

import cn.taskeren.gtnn.mod.gtPlusPlus.GenericChemExt;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemUtils.class, remap = false)
public class AddCatalyst_ItemUtils_Mixin {

	@Inject(method = "isCatalyst", at = @At("HEAD"), cancellable = true)
	private static void gtnn$isCatalyst(ItemStack aStack, CallbackInfoReturnable<Boolean> cir) {
		if (GenericChemExt.isAdditionalCatalyst(aStack)) {
			cir.setReturnValue(true);
		}
	}

}
