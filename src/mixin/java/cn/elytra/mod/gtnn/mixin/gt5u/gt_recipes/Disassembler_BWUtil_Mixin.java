package cn.elytra.mod.gtnn.mixin.gt5u.gt_recipes;

import bartworks.util.BWUtil;
import cn.elytra.mod.gtnn.modules.disassembler.ReversedRecipeRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import gregtech.api.util.GTShapedRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BWUtil.class, remap = false)
public class Disassembler_BWUtil_Mixin {

	@WrapOperation(method = "createGTCraftingRecipe(Lnet/minecraft/item/ItemStack;[Lnet/minecraft/enchantment/Enchantment;[IZZZZZZZZZZZZZ[Ljava/lang/Object;)Lnet/minecraftforge/oredict/ShapedOreRecipe;", at = @At(value = "NEW", target = "(Lnet/minecraft/item/ItemStack;ZZ[Lnet/minecraft/enchantment/Enchantment;[I[Ljava/lang/Object;)Lgregtech/api/util/GTShapedRecipe;"))
	private static GTShapedRecipe nn$createGTCraftingRecipe(ItemStack aResult, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] enchants, int[] enchantLevels, Object[] aRecipe, Operation<GTShapedRecipe> original, @Local(ordinal = 3, argsOnly = true) boolean canDisassemble) {
		GTShapedRecipe recipe = original.call(aResult, aRemovableByGT, aKeepingNBT, enchants, enchantLevels, aRecipe);
		if(canDisassemble) ReversedRecipeRegistry.registerShaped(aResult, aRecipe);
		return recipe;
	}

}
