package cn.elytra.mod.gtnn.mixin.gt5u.gt_recipes;

import cn.elytra.mod.gtnn.modules.disassembler.ReversedRecipeRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTShapedRecipe;
import gregtech.api.util.GTShapelessRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GTModHandler.class, remap = false)
public class Disassembler_GTModHandler_Mixin {

	@WrapOperation(method = "addCraftingRecipe(Lnet/minecraft/item/ItemStack;[Lnet/minecraft/enchantment/Enchantment;[IZZZZZZZZZZZZZ[Ljava/lang/Object;)Z", at = @At(value = "NEW", target = "(Lnet/minecraft/item/ItemStack;ZZ[Lnet/minecraft/enchantment/Enchantment;[I[Ljava/lang/Object;)Lgregtech/api/util/GTShapedRecipe;"))
	private static GTShapedRecipe nn$addCraftingRecipe(ItemStack aResult, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] enchants, int[] enchantLevels, Object[] aRecipe, Operation<GTShapedRecipe> original, @Local(ordinal = 3, argsOnly = true) boolean canDisassemble) {
		GTShapedRecipe recipe = original.call(aResult, aRemovableByGT, aKeepingNBT, enchants, enchantLevels, aRecipe);
		if(canDisassemble) ReversedRecipeRegistry.registerShaped(aResult, aRecipe);
		return recipe;
	}

	@WrapOperation(method = "addShapelessCraftingRecipe(Lnet/minecraft/item/ItemStack;[Lnet/minecraft/enchantment/Enchantment;[IZZZZZ[Ljava/lang/Object;)Z", at = @At(value = "NEW", target = "(Lnet/minecraft/item/ItemStack;ZZZ[Lnet/minecraft/enchantment/Enchantment;[I[Ljava/lang/Object;)Lgregtech/api/util/GTShapelessRecipe;"))
	private static GTShapelessRecipe nn$addShapelessCraftingRecipe(ItemStack aResult, boolean aRemovableByGT, boolean aKeepingNBT, boolean overwriteNBT, Enchantment[] enchants, int[] enchantLevels, Object[] aRecipe, Operation<GTShapelessRecipe> original, @Local(ordinal = 2, argsOnly = true) boolean canDisassemble) {
		GTShapelessRecipe recipe = original.call(aResult, aRemovableByGT, aKeepingNBT, overwriteNBT, enchants, enchantLevels, aRecipe);
		if(canDisassemble) ReversedRecipeRegistry.registerShapeless(aResult, aRecipe);
		return recipe;
	}

}
