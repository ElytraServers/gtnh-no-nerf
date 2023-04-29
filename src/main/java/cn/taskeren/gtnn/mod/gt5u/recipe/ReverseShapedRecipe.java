package cn.taskeren.gtnn.mod.gt5u.recipe;

import cn.taskeren.gtnn.mod.gt5u.tile.TileEntityDisassembler;
import cn.taskeren.gtnn.mod.gt5u.util.NNRecipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class ReverseShapedRecipe {

	private static final Queue<ReverseShapedRecipe> reverseRecipes = new LinkedList<>();
	private final ItemStack aResult;
	private final Object[] aRecipe;

	public static Queue<ReverseShapedRecipe> getReverseRecipes() {
		return reverseRecipes;
	}

	public ReverseShapedRecipe(ItemStack output, Object[] aRecipe) {
		this.aResult = output;
		this.aRecipe = aRecipe;
		reverseRecipes.add(this);
	}

	public static void runReverseRecipes() {
		for (ReverseShapedRecipe x : reverseRecipes) {
			Optional<GT_Recipe> recipeOptional = GT_Utility.reverseShapedRecipe(x.aResult, x.aRecipe);
			if (!recipeOptional.isPresent()) continue;
			GT_Recipe recipe = recipeOptional.get();
			ItemStack[] replacement = new ItemStack[recipe.mOutputs.length];
			TileEntityDisassembler.handleRecipeTransformation(
				recipe.mOutputs,
				replacement,
				Collections.singleton(recipe.mOutputs));

			recipe.mOutputs = replacement;
			NNRecipe.DisassemblerRecipes.add(recipe);
		}
	}

}
