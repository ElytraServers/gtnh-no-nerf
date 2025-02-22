package cn.taskeren.gtnn.mixin.gt5u;

import cn.elytra.mod.gtnn.GTNN;
import cn.elytra.mod.gtnn.modules.simple.module.disassembler.ReversedRecipeRegistry;
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes;
import gregtech.api.util.GTShapedRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTShapedRecipe.class, remap = false)
public class DisassemblerReversedRecipe_GTShapedRecipe_Mixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void nn$init(ItemStack aResult, boolean aDismantleAble, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] aEnchantmentsAdded, int[] aEnchantmentLevelsAdded, Object[] aRecipe, CallbackInfo ci) {
		if (aDismantleAble) {
			ReversedRecipeRegistry.registerShaped(aResult, aRecipe);
		}
	}

}
