package cn.taskeren.gtnn.mixin.gt5u;

import cn.elytra.mod.gtnn.rewind.module.disassembler.ReversedRecipeRegistry;
import gregtech.api.util.GTShapelessRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTShapelessRecipe.class, remap = false)
public class GTShapelessRecipeMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void nn$init(ItemStack aResult, boolean aDismantleAble, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] aEnchantmentsAdded, int[] aEnchantmentLevelsAdded, Object[] aRecipe, CallbackInfo ci) {
		if(aDismantleAble) {
			ReversedRecipeRegistry.registerShapeless(aResult, aRecipe);
		}
	}

}
