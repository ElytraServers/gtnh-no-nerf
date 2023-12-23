package cn.taskeren.gtnn.mod.gt5u.util;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeMapUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class NNRecipe {

	public static class Disassembler {
		private Disassembler() {
		}

		private static final RecipeMap<DisassemblerRecipeMapBackend> theDisassemblerRecipes = RecipeMapBuilder.of("gt.recipe.disassembler", DisassemblerRecipeMapBackend::new)
			.maxIO(1, 9, 0, 0)
			.minInputs(1, 0)
			.slotOverlays((index, isFluid, isOutput, isSpecial) -> {
				if(!isFluid && !isOutput) return GT_UITextures.OVERLAY_SLOT_WRENCH;
				switch(index) {
					case 0, 2, 6, 8 -> {
						return GT_UITextures.OVERLAY_SLOT_CIRCUIT;
					}
					case 4 -> {
						return GT_UITextures.OVERLAY_SLOT_WRENCH;
					}
				}
				return null;
			})
			.progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
			.recipeConfigFile("disassembler", GT_RecipeMapUtil.FIRST_ITEM_INPUT)
			.disableRegisterNEI()
			.build();

		private static final RecipeMap<RecipeMapBackend> theCraftingTableReversedRecipes = RecipeMapBuilder.of("gt.recipe.disassembler.crafting")
			.maxIO(1, 9, 0, 0)
			.minInputs(1, 0)
			.slotOverlays((index, isFluid, isOutput, isSpecial) -> {
				if(!isFluid && !isOutput) return GT_UITextures.OVERLAY_SLOT_WRENCH;
				switch(index) {
					case 0, 2, 6, 8 -> {
						return GT_UITextures.OVERLAY_SLOT_CIRCUIT;
					}
					case 4 -> {
						return GT_UITextures.OVERLAY_SLOT_WRENCH;
					}
				}
				return null;
			})
			.progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
			.recipeConfigFile("disassembler", GT_RecipeMapUtil.FIRST_ITEM_INPUT)
//		    .disableRegisterNEI() // todo: reverse this change
			.build();

		static {
			theDisassemblerRecipes.addFakeRecipe(
				false,
				GT_RecipeBuilder.builder()
					.itemInputs(new ItemStack(Items.stone_sword))
					.itemOutputs(new ItemStack(Items.wooden_sword))
					.duration(1)
					.eut(0)
					.build()
					.get()
			);
		}

		public static RecipeMap<DisassemblerRecipeMapBackend> getRecipeMap() {
			return theDisassemblerRecipes;
		}

		public static RecipeMap<RecipeMapBackend> getTheCraftingTableReversedRecipeMap() {
			return theCraftingTableReversedRecipes;
		}

	}

}
