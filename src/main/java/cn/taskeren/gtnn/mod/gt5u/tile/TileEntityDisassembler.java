package cn.taskeren.gtnn.mod.gt5u.tile;

import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipes;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityDisassembler extends GT_MetaTileEntity_BasicMachine_GT_Recipe {

	public TileEntityDisassembler(int aID, String aName, String aNameRegional, int aTier) {
		super(
			aID,
			aName,
			aNameRegional,
			aTier,
			"Disassembles items into their components",
//			NNRecipe.Disassembler.getRecipeMap(),
			DisassemblerRecipes.DISASSEMBLER_RECIPES,
			1,
			9,
			false,
			SoundResource.NONE,
			SpecialEffects.NONE,
			"ASSEMBLER",
			new Object[]{"ACA", "WHW", "ACA", 'A', X.ROBOT_ARM, 'C', X.CIRCUIT,'W', X.WIRE, 'H', X.HULL}
		);
	}

	@Override
	public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
		tooltip.add("§cPresented by GTNH-NO-NERF!");
	}

}
