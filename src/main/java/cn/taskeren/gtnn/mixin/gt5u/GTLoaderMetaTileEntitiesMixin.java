package cn.taskeren.gtnn.mixin.gt5u;

import cn.taskeren.gtnn.mod.gt5u.tile.TileEntityDisassembler;
import cn.taskeren.gtnn.mod.gt5u.util.NNItemList;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.loaders.preload.GT_Loader_MetaTileEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GT_Loader_MetaTileEntities.class, remap = false)
public class GTLoaderMetaTileEntitiesMixin {

	private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
		| GT_ModHandler.RecipeBits.BUFFERED;
	private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | bits;
	private static final String aTextWireHull = "WMW";

	@Inject(method = "run2", at = @At("RETURN"))
	private static void nn$run2(CallbackInfo ci) {
		NNItemList.Machine_LV_Disassembler.set(
			new TileEntityDisassembler(
				451,
				"basicmachine.disassembler.tier.01",
				"Basic Disassembler",
				1).getStackForm(1L));
		NNItemList.Machine_MV_Disassembler.set(
			new TileEntityDisassembler(
				452,
				"basicmachine.disassembler.tier.02",
				"Advanced Disassembler",
				2).getStackForm(1L));
		NNItemList.Machine_HV_Disassembler.set(
			new TileEntityDisassembler(
				453,
				"basicmachine.disassembler.tier.03",
				"Advanced Disassembler II",
				3).getStackForm(1L));
		NNItemList.Machine_EV_Disassembler.set(
			new TileEntityDisassembler(
				454,
				"basicmachine.disassembler.tier.04",
				"Advanced Disassembler III",
				4).getStackForm(1L));
		NNItemList.Machine_IV_Disassembler.set(
			new TileEntityDisassembler(
				455,
				"basicmachine.disassembler.tier.05",
				"Advanced Disassembler IV",
				5).getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_LV_Disassembler.get(1L),
			bitsd,
			new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_LV, 'A', ItemList.Robot_Arm_LV, 'C',
				OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_MV_Disassembler.get(1L),
			bitsd,
			new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_MV, 'A', ItemList.Robot_Arm_MV, 'C',
				OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_HV_Disassembler.get(1L),
			bitsd,
			new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_HV, 'A', ItemList.Robot_Arm_HV, 'C',
				OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_EV_Disassembler.get(1L),
			bitsd,
			new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_EV, 'A', ItemList.Robot_Arm_EV, 'C',
				OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });
		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_IV_Disassembler.get(1L),
			bitsd,
			new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_IV, 'A', ItemList.Robot_Arm_IV, 'C',
				OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten) });
	}

}
