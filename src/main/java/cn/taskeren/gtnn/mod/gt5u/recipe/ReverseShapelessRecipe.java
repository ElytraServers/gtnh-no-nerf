package cn.taskeren.gtnn.mod.gt5u.recipe;

import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipes;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class ReverseShapelessRecipe {

	private static final Queue<ReverseShapelessRecipe> reverseRecipes = new LinkedList<>();
	private final ItemStack aResult;
	private final Object[] aRecipe;

	public static Queue<ReverseShapelessRecipe> getReverseRecipes() {
		return reverseRecipes;
	}

	public ReverseShapelessRecipe(ItemStack output, Object[] aRecipe) {
		this.aResult = output;
		this.aRecipe = aRecipe;
		reverseRecipes.add(this);
	}

	public static void runReverseRecipes() {
		for(ReverseShapelessRecipe x : reverseRecipes) {
			Optional<GT_Recipe> recipeOptional = GT_Utility.reverseShapelessRecipe(x.aResult, x.aRecipe);
			DisassemblerRecipes.registerReversedCraftingRecipe(recipeOptional.orElse(null));
		}
	}

}
