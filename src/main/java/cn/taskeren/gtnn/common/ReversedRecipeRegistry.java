package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReversedRecipeRegistry {

	private static final List<GTRecipe> REVERSED_RECIPES = new ArrayList<>(1024);

	public static void registerShapedRecipe(ItemStack output, Object[] recipe) {
		var reversed = GTUtility.reverseShapedRecipe(output, recipe);
		reversed.ifPresent(REVERSED_RECIPES::add);
	}

	public static void registerShapelessRecipe(ItemStack output, Object[] recipe) {
		var reversed = GTUtility.reverseShapelessRecipe(output, recipe);
		reversed.ifPresent(REVERSED_RECIPES::add);
	}

	public static void registerRecipesToDisassemblers() {
		REVERSED_RECIPES.forEach(DisassemblerRecipes::registerReversedCraftingRecipe);
	}

}
