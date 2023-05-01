package cn.taskeren.gtnn.mixin.newhorizonscore;

import cn.taskeren.gtnn.mod.gt5u.tile.TileEntityDisassembler;
import cn.taskeren.gtnn.mod.gt5u.util.NNItemList;
import com.dreammaster.gthandler.CustomItemList;
import com.dreammaster.gthandler.GT_Loader_Machines;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GT_Loader_Machines.class, remap = false)
public class GTLoaderMachinesMixin {

	private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
		| GT_ModHandler.RecipeBits.BUFFERED;
	private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | bits;

	@Inject(method = "registerMachines2", at = @At("RETURN"))
	private void nn$registerMachines2(CallbackInfo ci) {
		NNItemList.Machine_LuV_Disassembler.set(
			new TileEntityDisassembler(11160, "basicmachine.disassembler.tier.06", "Elite Disassembler", 6)
				.getStackForm(1L));
		NNItemList.Machine_ZPM_Disassembler.set(
			new TileEntityDisassembler(
				11161,
				"basicmachine.disassembler.tier.07",
				"Elite Disassembler II",
				7).getStackForm(1L));
		NNItemList.Machine_UV_Disassembler.set(
			new TileEntityDisassembler(
				11162,
				"basicmachine.disassembler.tier.08",
				"Ultimate Deconstructor",
				8).getStackForm(1L));
		NNItemList.Machine_UHV_Disassembler.set(
			new TileEntityDisassembler(11163, "basicmachine.disassembler.tier.09", "Epic Deconstructor", 9)
				.getStackForm(1L));
		NNItemList.Machine_UEV_Disassembler.set(
			new TileEntityDisassembler(
				11164,
				"basicmachine.disassembler.tier.10",
				"Epic Deconstructor II",
				10).getStackForm(1L));
		NNItemList.Machine_UIV_Disassembler.set(
			new TileEntityDisassembler(
				11165,
				"basicmachine.disassembler.tier.11",
				"Epic Deconstructor III",
				11).getStackForm(1L));
		NNItemList.Machine_UMV_Disassembler.set(
			new TileEntityDisassembler(
				11166,
				"basicmachine.disassembler.tier.12",
				"Epic Deconstructor IV",
				12).getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_LuV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_LuV, 'H', ItemList.Hull_LuV, 'C',
				OrePrefixes.circuit.get(Materials.Master), 'W',
				OrePrefixes.cableGt01.get(Materials.VanadiumGallium)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_ZPM_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_ZPM, 'H', ItemList.Hull_ZPM, 'C',
				OrePrefixes.circuit.get(Materials.Ultimate), 'W',
				OrePrefixes.cableGt01.get(Materials.Naquadah)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_UV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_UV, 'H', ItemList.Hull_UV, 'C',
				OrePrefixes.circuit.get(Materials.Superconductor), 'W',
				OrePrefixes.cableGt01.get(Materials.ElectrumFlux)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_UHV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_UHV, 'H', ItemList.Hull_MAX, 'C',
				OrePrefixes.circuit.get(Materials.Infinite), 'W',
				OrePrefixes.cableGt01.get(Materials.ElectrumFlux)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_UEV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_UEV, 'H', CustomItemList.Hull_UEV, 'C',
				OrePrefixes.circuit.get(Materials.Bio), 'W',
				OrePrefixes.cableGt01.get(Materials.ElectrumFlux)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_UIV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_UIV, 'H', CustomItemList.Hull_UIV, 'C',
				OrePrefixes.circuit.get(Materials.Optical), 'W',
				OrePrefixes.cableGt01.get(Materials.ElectrumFlux)});

		GT_ModHandler.addCraftingRecipe(
			NNItemList.Machine_UMV_Disassembler.get(1L),
			bitsd,
			new Object[]{"RCR", "WHW", "RCR", 'R', ItemList.Robot_Arm_UMV, 'H', CustomItemList.Hull_UMV, 'C',
				OrePrefixes.circuit.get(Materials.Piko), 'W',
				OrePrefixes.cableGt01.get(Materials.ElectrumFlux)});
	}

}
