package cn.taskeren.gtnn.mixin.gt5u;

import cn.elytra.mod.gtnn.mod.GTNN;
import cn.elytra.mod.gtnn.rewind.module.disassembler.ReversedRecipeRegistry;
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes;
import gregtech.api.util.GTShapelessRecipe;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTShapelessRecipe.class, remap = false)
public class DisassemblerReversedRecipe_GTShapelessRecipe_Mixin {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void nn$init(ItemStack aResult, boolean aDismantleAble, boolean aRemovableByGT, boolean aKeepingNBT, Enchantment[] aEnchantmentsAdded, int[] aEnchantmentLevelsAdded, Object[] aRecipe, CallbackInfo ci) {
		if(aDismantleAble) {
			ReversedRecipeRegistry.registerShapeless(aResult, aRecipe);
		} else if(DisassemblerRecipes.DEBUG) {
			GTNN.logger.info("Skipped {} because aDismantleAble = false", aResult);
		}
	}

}
