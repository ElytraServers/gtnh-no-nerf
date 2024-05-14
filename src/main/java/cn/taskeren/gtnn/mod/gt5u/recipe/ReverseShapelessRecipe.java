package cn.taskeren.gtnn.mod.gt5u.recipe;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipeHelper;
import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipes;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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
		for(ReverseShapelessRecipe x : reverseRecipes) {
			Optional<GT_Recipe> recipeOptional = GT_Utility.reverseShapelessRecipe(x.aResult, x.aRecipe);
			registerReversedCraftingRecipe(recipeOptional.orElse(null));
		}
	}

	/**
	 * Register a reversed recipe.
	 * <p>
	 * This function handles both Shaped and Shapeless Recipes.
	 * <p>
	 * Note: the param itself should have been reversed once, DON'T reverse it again!
	 *
	 * @param revRecipe the REVERSED recipe.
	 */
	public static void registerReversedCraftingRecipe(@Nullable GT_Recipe revRecipe) {
		if(revRecipe == null) return;
		try {
			GT_Values.RA.stdBuilder()
				.itemInputs(revRecipe.mInputs)
				.itemOutputs(
					DisassemblerRecipeHelper.handleRecipeTransformation(
						revRecipe.mOutputs,
						Collections.singleton(revRecipe.mOutputs)
					)
				)
				.duration(300)
				.eut(30)
				.addTo(DisassemblerRecipes.DISASSEMBLER_RECIPES);
		} catch(Exception ex) {
			GTNN.logger.error("Unable to register reversed crafting recipe: " + (revRecipe.mInputs.length > 0 ? revRecipe.mInputs[0] : "mInputs is null"));
			GTNN.logger.error("", ex);
		}
	}

}
