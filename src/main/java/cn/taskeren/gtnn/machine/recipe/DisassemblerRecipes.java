package cn.taskeren.gtnn.machine.recipe;

import cn.elytra.mod.gtnn.GTNN;
import cn.taskeren.gtnn.util.KtCandy;
import cn.taskeren.gtnn.util.ProgressIterable;
import cn.taskeren.gtnn.util.ToStringHelper;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.enums.*;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.objects.GTItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.IndustrialCraft2;

public class DisassemblerRecipes {

	private static final List<GTItemStack> INPUT_BLACKLIST = KtCandy.buildList((list) -> {
		list.add(new GTItemStack(ItemList.Casing_Coil_Superconductor.get(1)));
		list.add(new GTItemStack(Materials.Graphene.getDust(1)));
		list.add(new GTItemStack(ItemList.Circuit_Parts_Vacuum_Tube.get(1)));
		list.add(new GTItemStack(ItemList.Schematic.get(1)));
		if(Mods.Railcraft.isModLoaded()) {
			list.add(new GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 0)));
			list.add(new GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 736)));
			list.add(new GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 816)));
		}
		list.add(new GTItemStack(IC2Items.getItem("mixedMetalIngot")));
		list.add(new GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "machine.alpha", 1, 14)));
		// region transformer
		list.add(new GTItemStack(ItemList.Transformer_MV_LV.get(1L)));
		list.add(new GTItemStack(GTModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 3)));
		list.add(new GTItemStack(ItemList.Transformer_HV_MV.get(1L)));
		list.add(new GTItemStack(GTModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 4)));
		list.add(new GTItemStack(ItemList.Transformer_EV_HV.get(1L)));
		list.add(new GTItemStack(GTModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 5)));
		list.add(new GTItemStack(ItemList.Transformer_IV_EV.get(1L)));
		list.add(new GTItemStack(GTModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 6)));
		// endregion transformer
		list.add(new GTItemStack(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 36)));
		list.add(new GTItemStack(GTModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 536)));
	});

	private static final ArrayListMultimap<GTItemStack, ItemStack> OUTPUT_HARD_OVERRIDE = KtCandy.apply(ArrayListMultimap.create(), m -> m.put(new GTItemStack(new ItemStack(Blocks.torch, 6)), new ItemStack(Items.stick)));

	private static final long EUT_HARD_OVERRIDE = 30;
	private static final long DUR_HARD_OVERRIDE = 600;

	public static final RecipeMap<RecipeMapBackend> DISASSEMBLER_RECIPES =
		RecipeMapBuilder.of("gtnn.recipe.disassembler")
			.maxIO(1, 9, 0, 0)
			.minInputs(1, 0)
			.slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
			.progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
			.disableOptimize()
			.build();

	public static boolean canDisassemble(ItemStack[] itemsToDisassemble) {
		if(itemsToDisassemble.length != 1) return false;
		var item = itemsToDisassemble[0];
		if(item == null) return false;

		if(item.getItem() instanceof MetaGeneratedTool) return false;
		if(isCircuit(item)) return false;
		if(isCrushedOre(item)) return false;
		if(INPUT_BLACKLIST.stream().anyMatch(b -> GTUtility.areStacksEqual(b.toStack(), item, true))) return false;
		if(isUnpackerRecipe(item)) return false;

		return true;
	}

	public static void loadAssemblerRecipes() {
		var recipesGroupingByOutputs = RecipeMaps.assemblerRecipes.getAllRecipes().stream()
			.filter(r -> canDisassemble(r.mOutputs))
			.collect(Collectors.groupingBy(r -> r.mOutputs));

		var totalRecipesCount = recipesGroupingByOutputs.entrySet().size();
		GTNN.logger.info("Loading reversed assembler recipes...");
		var recipesIterator = ProgressIterable.ofCollection(recipesGroupingByOutputs.entrySet(), 600, (i) -> GTNN.logger.info(String.format("%.2f%%", ((double) i * 100 / totalRecipesCount)) + " | " + i + " of " + totalRecipesCount));

		disassembleInputItemIteration:
		for(var itemAndItsRecipes : recipesIterator) {
			var items = itemAndItsRecipes.getKey();
			var recipes = itemAndItsRecipes.getValue();

			try {
				// length of items is not one is filtered in #canDisassemble
				assert items.length == 1;

				var revRecipes = recipes.stream().map(DisassemblerRecipeHelper::getReversedRecipe).collect(Collectors.toList());
				var revRecipeFirst = revRecipes.get(0); // the first recipe

				if(revRecipeFirst.mInputs.length > 1) {
					throw new IllegalArgumentException("Invalid assembler recipe, output types are more than 1: " + ToStringHelper.getItemStacksString(revRecipeFirst.mInputs));
				}

				// the item to disassemble
				var revInput = revRecipeFirst.mInputs[0];

				// handle hard overrides
				for(var hardOverridePair : OUTPUT_HARD_OVERRIDE.entries()) {
					var hardOverrideItem = hardOverridePair.getKey();
					if(hardOverrideItem.isStackEqual(revInput)) {
						GTValues.RA.stdBuilder()
							.itemInputs(revInput)
							.itemOutputs(hardOverridePair.getValue())
							.duration(DUR_HARD_OVERRIDE)
							.eut(EUT_HARD_OVERRIDE)
							.addTo(DisassemblerRecipes.DISASSEMBLER_RECIPES);

						// if the hard override is done,
						// directly continue to process the next item.
						continue disassembleInputItemIteration;
					}
				}

				// the items output from the disassembled item
				var revOutputs = DisassemblerRecipeHelper.handleRecipeTransformation(
					// the first recipe
					revRecipeFirst.mOutputs,
					// the remaining (skipped the first) recipes
					revRecipes.stream().skip(1).map(r -> r.mOutputs).collect(Collectors.toSet()));
				var revDuration = revRecipeFirst.mDuration;
				var revEUT = revRecipeFirst.mEUt;

				// region removeInvalidStacks
				revOutputs = Arrays.stream(revOutputs).filter(stack -> GTUtility.isStackValid(stack) && stack.stackSize > 0).toArray(ItemStack[]::new);
				// endregion

				GTValues.RA.stdBuilder()
					.itemInputs(revInput)
					.itemOutputs(revOutputs)
					.duration(revDuration)
					.eut(revEUT)
					.addTo(DisassemblerRecipes.DISASSEMBLER_RECIPES);
			} catch(Exception ex) {
				GTNN.logger.error("Failed to register the reversed assembler recipe", ex);
				GTNN.logger.error("Input:  " + ToStringHelper.getItemStacksString(items));
				for(int i = 0; i < recipes.size(); i++) {
					GTNN.logger.error("Output: " + i + " " + recipes.get(i).toString());
				}

				// directly throw OOB exception, because it is usually throws a lot if something is broken,
				// and the logs are going to be crazy.
				// I don't like it.
				if(ex instanceof ArrayIndexOutOfBoundsException) {
					throw ex;
				}
			}
		}
	}

	private static boolean isCircuit(ItemStack stack) {
		var data = GTOreDictUnificator.getAssociation(stack);
		if(data != null) {
			return data.mPrefix == OrePrefixes.circuit;
		}
		return false;
	}

	private static boolean isUnpackerRecipe(ItemStack stack) {
		var unpackerRecipe = RecipeMaps.unpackagerRecipes.findRecipeQuery()
			.items(stack)
			.find();
		return unpackerRecipe != null;
	}

	private static boolean isCrushedOre(ItemStack stack) {
		var data = GTOreDictUnificator.getAssociation(stack);
		if(data != null) {
			return data.mPrefix == OrePrefixes.crushed ||
				data.mPrefix == OrePrefixes.crushedCentrifuged ||
				data.mPrefix == OrePrefixes.crushedPurified;
		}
		return false;
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
	public static void registerReversedCraftingRecipe(@Nullable GTRecipe revRecipe) {
		if(revRecipe == null) return;

		if(!canDisassemble(revRecipe.mInputs)) {
			return;
		}

		try {
			GTValues.RA.stdBuilder()
				.itemInputs(revRecipe.mInputs)
				.itemOutputs(
					DisassemblerRecipeHelper.handleRecipeTransformation(
						revRecipe.mOutputs,
						Collections.singleton(revRecipe.mOutputs)
					)
				)
				.duration(300)
				.eut(30)
				.addTo(DISASSEMBLER_RECIPES);
		} catch(Exception ex) {
			GTNN.logger.error("Unable to register reversed crafting recipe: " + (revRecipe.mInputs.length > 0 ? revRecipe.mInputs[0] : "mInputs is null"));
			GTNN.logger.error("", ex);
		}
	}

}
