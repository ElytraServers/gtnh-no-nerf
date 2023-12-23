package cn.taskeren.gtnn.mod.gt5u.recipe;

import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipeMapBackend;
import cn.taskeren.gtnn.mod.gt5u.util.NNRecipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.Collections;
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
		for (ReverseShapelessRecipe x : reverseRecipes) {
			Optional<GT_Recipe> recipeOptional = GT_Utility.reverseShapelessRecipe(x.aResult, x.aRecipe);
			registerRecipe(recipeOptional.orElse(null));
		}
	}

	public static void registerRecipe(GT_Recipe recipe) {
		if (recipe == null) return;
		ItemStack[] replacement = new ItemStack[recipe.mOutputs.length];
		DisassemblerRecipeMapBackend.handleRecipeTransformation(
			recipe.mOutputs,
			replacement,
			Collections.singleton(recipe.mOutputs));

		recipe.mOutputs = replacement;
		NNRecipe.Disassembler.getTheCraftingTableReversedRecipeMap().add(recipe);
	}

}
