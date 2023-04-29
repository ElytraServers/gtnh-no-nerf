package cn.taskeren.gtnn.mod.gt5u.util;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;

import java.util.HashSet;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

public class NNRecipe {

	private static final String TEXTURES_GUI_BASICMACHINES = "textures/gui/basicmachines";

	public static final GT_Recipe.GT_Recipe_Map DisassemblerRecipes = new GT_Recipe.GT_Recipe_Map(new HashSet<>(250), "gt.recipe.disassembler", "Disassembler", null, GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Disassembler"), 1, 9, 1, 0, 1, E, 1, E, true, false) {

		@Override
		public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
			if(isOutput) {
				switch(index) {
					case 0:
					case 2:
					case 6:
					case 8:
						return GT_UITextures.OVERLAY_SLOT_CIRCUIT;
					case 4:
						return GT_UITextures.OVERLAY_SLOT_WRENCH;
				}
			}
			return super.getOverlayForSlot(isFluid, isOutput, index, isSpecial);
		}
	}.setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_WRENCH).setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT);

}
