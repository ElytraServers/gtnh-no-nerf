package cn.taskeren.gtnn.mixin.gt5u;

import cn.taskeren.gtnn.mod.gt5u.recipe.ReverseShapelessRecipe;
import gregtech.api.util.GT_Shapeless_Recipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GT_Shapeless_Recipe.class, remap = false)
public class GTShapelessRecipeMixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void nn$init(ItemStack aResult, boolean aDismantleAble, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] aEnchantmentsAdded, int[] aEnchantmentLevelsAdded, Object[] aRecipe, CallbackInfo ci) {
		if(aDismantleAble) {
			new ReverseShapelessRecipe(aResult, aRecipe);
		}
	}

}
