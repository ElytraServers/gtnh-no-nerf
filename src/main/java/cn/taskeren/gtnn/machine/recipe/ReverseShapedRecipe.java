package cn.taskeren.gtnn.machine.recipe;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;

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
			Optional<GTRecipe> recipeOptional = GTUtility.reverseShapedRecipe(x.aResult, x.aRecipe);
			DisassemblerRecipes.registerReversedCraftingRecipe(recipeOptional.orElse(null));
		}
	}

}
