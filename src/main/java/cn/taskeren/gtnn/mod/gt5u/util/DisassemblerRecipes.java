package cn.taskeren.gtnn.mod.gt5u.util;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.util.KtCandy;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.enums.*;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.*;
import gregtech.api.util.*;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.IndustrialCraft2;

public class DisassemblerRecipes {

	private static final Marker MARKER_DISASSEMBLER_RECIPES = MarkerManager.getMarker("DisassemblerRecipes");

	private static final List<GT_ItemStack> INPUT_BLACKLIST = KtCandy.run(() -> {
		var list = new ArrayList<GT_ItemStack>();

		list.add(new GT_ItemStack(ItemList.Casing_Coil_Superconductor.get(1)));
		list.add(new GT_ItemStack(Materials.Graphene.getDust(1)));
		list.add(new GT_ItemStack(ItemList.Circuit_Parts_Vacuum_Tube.get(1)));
		list.add(new GT_ItemStack(ItemList.Schematic.get(1)));
		if(Mods.Railcraft.isModLoaded()) {
			list.add(new GT_ItemStack(GT_ModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 0)));
			list.add(new GT_ItemStack(GT_ModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 736)));
			list.add(new GT_ItemStack(GT_ModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 816)));
		}
		list.add(new GT_ItemStack(IC2Items.getItem("mixedMetalIngot")));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(Mods.Railcraft.ID, "machine.alpha", 1, 14)));
		// region transformer
		list.add(new GT_ItemStack(ItemList.Transformer_MV_LV.get(1L)));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 3)));
		list.add(new GT_ItemStack(ItemList.Transformer_HV_MV.get(1L)));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 4)));
		list.add(new GT_ItemStack(ItemList.Transformer_EV_HV.get(1L)));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 5)));
		list.add(new GT_ItemStack(ItemList.Transformer_IV_EV.get(1L)));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 6)));
		// endregion transformer
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 36)));
		list.add(new GT_ItemStack(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 536)));

		return list;
	});

	private static final ArrayListMultimap<GT_ItemStack, ItemStack> OUTPUT_HARD_OVERRIDE = KtCandy.apply(ArrayListMultimap.create(), m -> m.put(new GT_ItemStack(new ItemStack(Blocks.torch, 6)), new ItemStack(Items.stick)));

	private static final long EUT_HARD_OVERRIDE = 30;
	private static final long DUR_HARD_OVERRIDE = 600;

	public static final RecipeMap<DisassemblerBackend> DISASSEMBLER_RECIPES = RecipeMapBuilder
		.of("gtnn.recipe.disassembler", DisassemblerBackend::new)
		.maxIO(1, 9, 0, 0)
		.minInputs(1, 0)
		.slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CIRCUIT : null)
		.progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE)
		.disableOptimize()
		.recipeConfigFile("disassembling", GT_RecipeMapUtil.FIRST_ITEM_OUTPUT)
		.build();

	public static void loadAssemblerRecipes() {

		// todo: do something... uh, I don't know if it is needed.
		// var recipesGroupingByOutputs = RecipeMaps.assemblerRecipes.getAllRecipes().stream().collect(Collectors.groupingBy(r -> r.mOutputs));

		var size = RecipeMaps.assemblerRecipes.getAllRecipes().size();
		GTNN.logger.info(MARKER_DISASSEMBLER_RECIPES, "Importing Disassembler Recipes from Assembler, size = {}", size);

		int success = 0, blockedBadInput = 0, blockedTool = 0, blockedBlacklist = 0, blockedCircuit = 0, blockedUnpacker = 0;

		for(GT_Recipe recipe : RecipeMaps.assemblerRecipes.getAllRecipes()) {

			if(recipe.mOutputs.length > 1 || recipe.mOutputs[0] == null /*wtf? how could this happen?*/) {
				GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Bad: {}", (Object) recipe.mOutputs);
				blockedBadInput++;
				continue;
			}

			var originalRecipe = recipe.copy();
			var inputItemStack = originalRecipe.mOutputs[0];

			// check input (the thing to disassemble)
			if(inputItemStack.getItem() instanceof GT_MetaGenerated_Item) {
				GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Tool: {}", inputItemStack);
				blockedTool++;
				continue;
			}
			if(isCircuit(inputItemStack)) {
				GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Circuit: {}", inputItemStack);
				blockedCircuit++;
				continue;
			}
			if(INPUT_BLACKLIST.stream().anyMatch(b -> GT_Utility.areStacksEqual(b.toStack(), inputItemStack, true))) {
				GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Blacklisted: {}", inputItemStack);
				blockedBlacklist++;
				continue;
			}
			if(isUnpackerRecipe(inputItemStack)) {
				GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Unpacker: {}");
				blockedUnpacker++;
				continue;
			}

			// modify the disassembled outputs
			for(GT_ItemStack override : OUTPUT_HARD_OVERRIDE.keySet()) {
				var in = inputItemStack.copy();
				in.stackSize = 1;
				if(override.isStackEqual(in) && override.mStackSize <= inputItemStack.stackSize) {
					originalRecipe.mOutputs = OUTPUT_HARD_OVERRIDE.get(override).toArray(new ItemStack[0]);
					originalRecipe.mInputs[0].stackSize = override.mStackSize;
					originalRecipe.mEUt = (int) EUT_HARD_OVERRIDE;
					originalRecipe.mDuration = (int) DUR_HARD_OVERRIDE;
				}
			}

			var outputItemStack = Arrays.stream(originalRecipe.mInputs)
				.filter(Objects::nonNull)
				.filter(i -> !GT_Utility.isAnyIntegratedCircuit(i))
				.collect(Collectors.toList()).toArray(new ItemStack[0]);

			GT_Values.RA.stdBuilder()
				.itemInputs(originalRecipe.mOutputs)
				.itemOutputs(outputItemStack)
				.duration(originalRecipe.mDuration)
				.eut(originalRecipe.mEUt)
				.addTo(DISASSEMBLER_RECIPES)
			;

			GTNN.logger.debug(MARKER_DISASSEMBLER_RECIPES, "Loaded an assembler recipe for {}", (Object) recipe.mOutputs);
			success++;
		}

		GTNN.logger.info(MARKER_DISASSEMBLER_RECIPES, "Successfully loaded assembler-source recipes, success = %d, badInput = %d, tool = %d, blacklist = %d, circuit = %d, unpacker = %d", success, blockedBadInput, blockedTool, blockedBlacklist, blockedCircuit, blockedUnpacker);
	}

	private static boolean isCircuit(ItemStack stack) {
		var data = GT_OreDictUnificator.getAssociation(stack);
		if(data != null) {
			return data.mPrefix == OrePrefixes.circuit;
		}
		return false;
	}

	private static boolean isUnpackerRecipe(ItemStack stack) {
		return RecipeMaps.unpackagerRecipes
			.findRecipe(null, true, true, Long.MAX_VALUE, null, stack) != null;
	}

	public static class DisassemblerBackend extends RecipeMapBackend {
		public DisassemblerBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
			super(propertiesBuilder);
		}
	}

}
