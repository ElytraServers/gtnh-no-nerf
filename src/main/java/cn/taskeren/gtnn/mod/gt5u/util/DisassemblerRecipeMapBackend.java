package cn.taskeren.gtnn.mod.gt5u.util;

import cn.taskeren.gtnn.mod.gt5u.recipe.ReverseShapedRecipe;
import cn.taskeren.gtnn.mod.gt5u.recipe.ReverseShapelessRecipe;
import com.google.common.collect.ArrayListMultimap;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.*;
import gregtech.api.util.extensions.ArrayExt;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static gregtech.api.enums.Mods.*;

@SuppressWarnings("DuplicatedCode")
@Deprecated
public class DisassemblerRecipeMapBackend extends RecipeMapBackend {

	public DisassemblerRecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
		super(propertiesBuilder);
	}

	/**
	 * All the recipes of disassembler are processed here as fallback.
	 * Thus, we cannot provide a NEI category that lists the recipes.
	 * <p>
	 * The most important part is in {@link #getRecipe(ItemStack, int, IHasWorldObjectAndCoords)}.
	 */
	@Nullable
	@Override
	@SuppressWarnings("OptionalGetWithoutIsPresent")
	protected GT_Recipe findFallback(ItemStack[] items, FluidStack @NotNull [] fluids, @Nullable ItemStack specialSlot) {
		var item = items[0].copy();
		var disassemblerRecipe = getRecipe(item, 9, null);
		if(disassemblerRecipe instanceof RecipeData data) {
			item.stackSize = data.stackSize;
			return GT_RecipeBuilder.builder()
				.itemInputs(item)
				.itemOutputs(data.outputs)
				.eut(data.EUt)
				.duration(data.duration)
				.build()
				.get()
				;
		}
		return null;
	}

	private static final Set<GT_ItemStack> INPUT_BLACKLIST = new HashSet<>();

	private static final ArrayListMultimap<GT_ItemStack, ItemStack> OUTPUT_HARD_OVERRIDE = ArrayListMultimap.create();

	/**
	 * The output replacement list.
	 * <p>
	 * An array of ItemStack tuples, each tuple contains two ItemStacks, the second one is the replacement.
	 */
	private static final ItemStack[][] ALWAYS_REPLACE = {{new ItemStack(Blocks.trapped_chest, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.chest, 1, OreDictionary.WILDCARD_VALUE)}};

	/**
	 * The output replacement list.
	 * <p>
	 * An array of ItemStack tuples, each tuple contains two ItemStacks, the second one is the replacement.
	 */
	private static final Object[][] ORE_DICT_OVERRIDE = {{"plankWood", new ItemStack(Blocks.planks)}, {"stoneCobble", new ItemStack(Blocks.cobblestone)}, {"gemDiamond", new ItemStack(Items.diamond)}, {"logWood", new ItemStack(Blocks.log)}, {"stickWood", new ItemStack(Items.stick)}, {"treeSapling", new ItemStack(Blocks.sapling)}};

	static {
		addBlacklist(ItemList.Casing_Coil_Superconductor.get(1L));
		addBlacklist(Materials.Graphene.getDust(1));
		addBlacklist(ItemList.Circuit_Parts_Vacuum_Tube.get(1L));
		addBlacklist(ItemList.Schematic.get(1L));
		if(Railcraft.isModLoaded()) {
			addBlacklist(GT_ModHandler.getModItem(Railcraft.ID, "track", 1L, 0));
			addBlacklist(GT_ModHandler.getModItem(Railcraft.ID, "track", 1L, 736));
			addBlacklist(GT_ModHandler.getModItem(Railcraft.ID, "track", 1L, 816));
		}
		addBlacklist(IC2Items.getItem("mixedMetalIngot"));
		addBlacklist(GT_ModHandler.getModItem(Railcraft.ID, "machine.alpha", 1L, 14));
		// region transformer
		// Temporary solution for cable dupe
		// Maybe we can mark assembler recipes as "cannot disassemble"
		// and only disassemble crafting recipes in the future
		// Also `getIC2Item` doesn't work, maybe loading order?
		addBlacklist(ItemList.Transformer_MV_LV.get(1L));
		addBlacklist(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 3));
		addBlacklist(ItemList.Transformer_HV_MV.get(1L));
		addBlacklist(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 4));
		addBlacklist(ItemList.Transformer_EV_HV.get(1L));
		addBlacklist(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 5));
		addBlacklist(ItemList.Transformer_IV_EV.get(1L));
		addBlacklist(GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockElectric", 1L, 6));
		// endregion transformer
		addBlacklist(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 36));
		addBlacklist(GT_ModHandler.getModItem(AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 536));

		OUTPUT_HARD_OVERRIDE.put(new GT_ItemStack(new ItemStack(Blocks.torch, 6)), new ItemStack(Items.stick));
	}

	// region recipe map transformation

	public enum RecipeStatus {
		DID_NOT_FIND_RECIPE,
		CANNOT_DISASSEMBLE,
		NOT_MEET_REQUIREMENTS,
		NOT_MEET_REQUIREMENTS_VOLTAGE,
	}

	protected static class RecipeData {
		ItemStack[] outputs;
		int stackSize;
		long EUt;
		long duration;
		@Nullable GT_Recipe assemblerRecipe;

		public RecipeData(ItemStack[] outputs, int stackSize, long EUt, long duration, @Nullable GT_Recipe assemblerRecipe) {
			this.outputs = outputs;
			this.stackSize = stackSize;
			this.EUt = EUt;
			this.duration = duration;
			this.assemblerRecipe = assemblerRecipe;
		}

		public RecipeData(ItemStack[] outputs, int stackSize, long EUt, long duration) {
			this(outputs, stackSize, EUt, duration, null);
		}

		@Override
		public String toString() {
			return "RecipeData(outputs=" + Arrays.toString(outputs) + ", stack_size=" + stackSize + ", EUt=" + EUt + ", original_recipe=" + assemblerRecipe + ")";
		}
	}

	public static final long EUT_HARD_OVERRIDE = 30; // EUt
	public static final long DUR_HARD_OVERRIDE = 600; // duration

	/**
	 * Try to get the recipe for the given input.
	 *
	 * @param stack the stack to be disassembled
	 * @return the processed result. Can be {@link RecipeStatus} of the reason or {@link RecipeData} if the recipe is found.
	 */
	public static Object getRecipe(ItemStack stack, int voltageTier, @Nullable IHasWorldObjectAndCoords tile) {
		if(GT_Utility.isStackInvalid(stack)) return RecipeStatus.DID_NOT_FIND_RECIPE;

		if(stack.getItem() instanceof GT_MetaGenerated_Tool || isCircuit(stack) || INPUT_BLACKLIST.stream().anyMatch(t -> GT_Utility.areStacksEqual(t.toStack(), stack, true)) || isUnpackerRecipe(stack)) {
			return RecipeStatus.CANNOT_DISASSEMBLE;
		}

		// check hard override
		for(GT_ItemStack override : OUTPUT_HARD_OVERRIDE.keySet()) {
			var in = stack.copy();
			in.stackSize = 1;
			if(override.isStackEqual(in) && override.mStackSize <= stack.stackSize) {
				return new RecipeData(OUTPUT_HARD_OVERRIDE.get(override).toArray(new ItemStack[0]), override.mStackSize, EUT_HARD_OVERRIDE, DUR_HARD_OVERRIDE);
			}
		}

		// (onFlyGeneration)
		// get recipes that output the given item
		var assemblerRecipes = findRelatedAssemblerRecipes(stack);
		if(!assemblerRecipes.isEmpty()) {
			// get the most suitable recipe
			var downcastAssemblerRecipe = ensureDowncasting(assemblerRecipes);
			// remove invalid stacks
			for(int i = 0; i < downcastAssemblerRecipe.outputs.length; i++) {
				var outputStack = downcastAssemblerRecipe.outputs[i];
				if(GT_Utility.isStackInvalid(outputStack) || outputStack.stackSize < 1) {
					downcastAssemblerRecipe.outputs[i] = null;
				}
				// copy and set the stack
				downcastAssemblerRecipe = new DisassembleReference(downcastAssemblerRecipe.stackSize, ArrayExt.withoutNulls(downcastAssemblerRecipe.outputs, ItemStack[]::new), downcastAssemblerRecipe.recipe, downcastAssemblerRecipe.EUt, downcastAssemblerRecipe.duration);
			}
			// check voltage
			if(downcastAssemblerRecipe.EUt > GT_Values.V[voltageTier]) {
				return RecipeStatus.NOT_MEET_REQUIREMENTS_VOLTAGE;
			} else {
				return new RecipeData(downcastAssemblerRecipe.outputs, downcastAssemblerRecipe.stackSize, downcastAssemblerRecipe.EUt, downcastAssemblerRecipe.duration, null);
			}
		}

		// (checkRecipeMap)
		// todo
//		RecipeMap<?> theRecipeMap = NNRecipe.Disassembler.getTheCraftingTableReversedRecipeMap();
//		var recipe = theRecipeMap.findRecipe(tile, true, GT_Values.V[voltageTier], null, stack);
		GT_Recipe recipe = null;
		if(recipe == null) return Optional.of(RecipeStatus.DID_NOT_FIND_RECIPE);
		if(recipe.isRecipeInputEqual(false, null, stack)) {
			if(recipe.mSpecialValue == -100) {
				// bypass standard disassembler restrictions
				return Optional.of(new RecipeData(recipe.mOutputs, 1, recipe.mEUt, recipe.mDuration));
			} else {
				return Optional.of(new RecipeData(recipe.mOutputs, recipe.mInputs[0].stackSize, recipe.mEUt, recipe.mDuration));
			}
		}

		return Optional.of(RecipeStatus.NOT_MEET_REQUIREMENTS);
	}

	public static class DisassembleReference {
		int stackSize;
		// the outputs of assembler recipe is the outputs of disassembler recipe
		ItemStack[] outputs;
		// the assembler recipe
		GT_Recipe recipe;
		// the EUt of assembler recipe
		int EUt;
		// the duration of assembler recipe
		int duration;

		public DisassembleReference(int stackSize, ItemStack[] outputs, GT_Recipe recipe, int EUt, int duration) {
			this.stackSize = stackSize;
			this.outputs = outputs;
			this.recipe = recipe;
			this.EUt = EUt;
			this.duration = duration;
		}
	}

	/**
	 * Get related Assembler recipes of given item.
	 */
	private static Collection<DisassembleReference> findRelatedAssemblerRecipes(ItemStack stack) {
		var stackSize = new AtomicInteger();
		var possibleRecipes = RecipeMaps.assemblerRecipes.getAllRecipes().stream()
			.filter(assemblerRecipe -> Arrays.stream(assemblerRecipe.mOutputs).anyMatch(assemblerRecipeOutput -> {
				if(assemblerRecipeOutput == null) return false;
				var out = stack.copy();
				boolean isDone = GT_Utility.areStacksEqual(assemblerRecipeOutput, out, true) && assemblerRecipeOutput.stackSize <= stack.stackSize;
				if(isDone) stackSize.set(assemblerRecipeOutput.stackSize);
				return isDone;
			}))
			.map(assemblerRecipe -> new DisassembleReference(stackSize.get(), assemblerRecipe.mInputs, assemblerRecipe, assemblerRecipe.mEUt, assemblerRecipe.mDuration))
			.collect(Collectors.toList());

		// return the single one
		if(possibleRecipes.size() == 1) return possibleRecipes;
		// sort if there are multiple recipes
		return possibleRecipes.stream().sorted(Comparator.comparingDouble(DisassemblerRecipeMapBackend::getRecipeCheapness)).collect(Collectors.toList());
	}

	/**
	 * Calculate the cheapness of the recipe.
	 * This is used to get the cheaper recipe when there are multiple recipes.
	 */
	public static double getRecipeCheapness(DisassembleReference ref) {
		double fluidInputValueRaw = Arrays.stream(ref.recipe.mFluidInputs).flatMapToInt(f -> IntStream.of(f.amount)).sum();
		fluidInputValueRaw = fluidInputValueRaw > 0 ? fluidInputValueRaw : 144D;
		double inputValue = Arrays.stream(ref.outputs).flatMapToInt(f -> IntStream.of(f.stackSize)).sum() + (fluidInputValueRaw / 144D);
		double fluidOutputValueRaw = Arrays.stream(ref.recipe.mFluidOutputs).flatMapToInt(f -> IntStream.of(f.amount)).sum();
		fluidOutputValueRaw = fluidOutputValueRaw > 0 ? fluidOutputValueRaw : 144D;
		double outputValue = Arrays.stream(ref.recipe.mOutputs).flatMapToInt(f -> IntStream.of(f.stackSize)).sum() + (fluidOutputValueRaw / 144D);
		return outputValue / inputValue;
	}

	private static DisassembleReference ensureDowncasting(Collection<? extends DisassembleReference> recipes) {
		// make a copy of the recipes
		ArrayList<? extends DisassembleReference> mutableRecipes = new ArrayList<>(recipes);
		// get the first recipe to get some data that should be the same in all recipes.
		var firstRecipe = mutableRecipes.remove(0);
		var initialOutputs = firstRecipe.outputs;
		var eut = firstRecipe.EUt;
		var dur = firstRecipe.duration;

		// initialize the final outputs array for further use
		var finalOutputs = new ItemStack[initialOutputs.length];
		// the remaining assembler recipes except the first one that was removed above.
		List<? extends GT_Recipe> remainingAssemblerRecipes = mutableRecipes.stream()
			.map(x -> x.recipe)
			.collect(Collectors.toList());

		// get(fill) the final outputs by DreamMaster's magic algorithm
		handleRecipeTransformation(initialOutputs, finalOutputs, remainingAssemblerRecipes);

		var stackSize = recipes.stream()
			.mapToInt(x -> x.stackSize)
			.min()
			.orElseThrow(IllegalStateException::new);
		return new DisassembleReference(stackSize, finalOutputs, null, eut, dur);
	}

	private static void handleRecipeTransformation(ItemStack[] firstOutputs, ItemStack[] finalOutputs, List<? extends GT_Recipe> assemblerRecipes) {
		// the list of items in all assembler recipes
		Set<ItemStack[]> assemblerInputsOfRecipes = assemblerRecipes != null
			? assemblerRecipes.stream().map(x -> x.mInputs).collect(Collectors.toSet())
			: null;
		handleRecipeTransformation(firstOutputs, finalOutputs, assemblerInputsOfRecipes);
	}

	/**
	 * Fill the finalOutputs array by the given outputs and assembler recipes by DreamMaster's magic algorithm.
	 * <p>
	 * To be public is also used for the {@link ReverseShapelessRecipe} and {@link ReverseShapedRecipe}.
	 */
	public static void handleRecipeTransformation(ItemStack[] firstOutputs, ItemStack[] finalOutputs, Set<ItemStack[]> inputItemsOfRecipes) {
		// iterate each slot of outputs
		for(int idx = 0, inputsLength = firstOutputs.length; idx < inputsLength; idx++) {
			// region handleRecipeTransformationInternal logics
			var itemInSlotIdx = firstOutputs[idx];
			var itemDataInSlotIdx = GT_OreDictUnificator.getItemData(itemInSlotIdx);
			// if the item is nothing can be transformed, just place it into the final outputs
			if(itemDataInSlotIdx == null || itemDataInSlotIdx.mMaterial == null || itemDataInSlotIdx.mMaterial.mMaterial == null || itemDataInSlotIdx.mPrefix == null) {
				finalOutputs[idx] = itemInSlotIdx;
			} else { // or if the item is something of GregTech related, then we can do something
				// region handleReplacement logics
				// the replacement reference of the item in the slot idx
				AtomicReference<Materials> toReplace = new AtomicReference<>();
				var materialMaterialOfFirst = itemDataInSlotIdx.mMaterial.mMaterial;
				// if there are some other assembler recipes,
				// we can compare the items in the same slot, and transform them.
				if(inputItemsOfRecipes != null) { // handleInputsStacks logics
					final int finalIndex = idx;
					inputItemsOfRecipes.forEach(inputsInOtherRecipe -> {
						// the item data of the same slot in other recipes
						var dataAgainst = GT_OreDictUnificator.getItemData(inputsInOtherRecipe[finalIndex]);
						if(!(dataAgainst == null || dataAgainst.mMaterial == null || dataAgainst.mMaterial.mMaterial == null || dataAgainst.mPrefix == null || dataAgainst.mPrefix != itemDataInSlotIdx.mPrefix)) {
							handleDifferentMaterialsOnRecipes(materialMaterialOfFirst, dataAgainst.mMaterial.mMaterial, toReplace);
							handleAnyMaterials(materialMaterialOfFirst, toReplace);
						}
					});
				}
				// if the slot is not replaced yet, we try to place it with cheaper materials
				// like we don't give annealed copper, instead we give normal copper.
				if(toReplace.get() == null) {
					// remove Magnetic and Annealed Modifiers
					handleBetterMaterialsVersions(itemDataInSlotIdx, toReplace);
				}
				// if the slot is replaced, we put it to the final outputs.
				if(toReplace.get() != null) {
					finalOutputs[idx] = GT_OreDictUnificator.get(
						OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name()),
						toReplace.get(),
						itemInSlotIdx.stackSize //
					);
				} else { // or we try to transform one last step, the circuits!
					if(itemDataInSlotIdx.mPrefix == OrePrefixes.circuit) {
						handleCircuits(materialMaterialOfFirst, finalOutputs, itemInSlotIdx, idx);
					}
				}
				// endregion
			}
			// endregion
		}
		// region addOthersAndHandleAlwaysReplace logics
		for(int i = 0; i < firstOutputs.length; i++) {
			// adds rest of items
			if(finalOutputs[i] == null) {
				finalOutputs[i] = firstOutputs[i];
			}

			// Math.min the recipe output if Items are the same
			if(GT_Utility.areStacksEqual(finalOutputs[i], firstOutputs[i])) {
				finalOutputs[i].stackSize = Math.min(finalOutputs[i].stackSize, firstOutputs[i].stackSize);
			}

			// handles replacements overrides
			for(ItemStack[] entry : ALWAYS_REPLACE) {
				if(GT_Utility.areStacksEqual(finalOutputs[i], entry[0], true)) {
					finalOutputs[i] = entry[1].copy();
					break;
				}
			}

			finalOutputs[i] = handleUnification(finalOutputs[i]);
			finalOutputs[i] = handleWildcard(finalOutputs[i]);
			finalOutputs[i] = handleContainerItem(finalOutputs[i]);
		}
		// endregion
	}

	// endregion

	// region const modification functions

	public static void addBlacklist(ItemStack stack) {
		INPUT_BLACKLIST.add(new GT_ItemStack(stack));
	}

	// endregion

	// region replacement functions

	private static void handleAnyMaterials(Materials first, AtomicReference<? super Materials> toRpl) {
		if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyIron))) toRpl.set(Materials.Iron);
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyCopper)))
			toRpl.set(Materials.Copper);
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyRubber)))
			toRpl.set(Materials.Rubber);
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyBronze)))
			toRpl.set(Materials.Bronze);
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnySyntheticRubber)))
			toRpl.set(Materials.Rubber);
	}

	private static void handleDifferentMaterialsOnRecipes(Materials first, Materials second, AtomicReference<? super Materials> toRpl) {
		if(!first.equals(second))
			if(first.equals(Materials.Aluminium) && second.equals(Materials.Iron)) toRpl.set(second);
			else if(first.equals(Materials.Steel) && second.equals(Materials.Iron)) toRpl.set(second);
			else if(first.equals(Materials.WroughtIron) && second.equals(Materials.Iron)) toRpl.set(second);
			else if(first.equals(Materials.Aluminium) && second.equals(Materials.WroughtIron))
				toRpl.set(Materials.Iron);
			else if(first.equals(Materials.Aluminium) && second.equals(Materials.Steel)) toRpl.set(second);
			else if(first.equals(Materials.Polytetrafluoroethylene) && second.equals(Materials.Plastic))
				toRpl.set(second);
			else if(first.equals(Materials.Polybenzimidazole) && second.equals(Materials.Plastic)) toRpl.set(second);
			else if(first.equals(Materials.Polystyrene) && second.equals(Materials.Plastic)) toRpl.set(second);
			else if(first.equals(Materials.Silicone) && second.equals(Materials.Plastic)) toRpl.set(second);
			else if(first.equals(Materials.NetherQuartz) || first.equals(Materials.CertusQuartz) && second.equals(Materials.Quartzite))
				toRpl.set(second);
			else if(first.equals(Materials.Plastic) && second.equals(Materials.Wood)) toRpl.set(second);
			else if(first.equals(Materials.Diamond) && second.equals(Materials.Glass)) toRpl.set(second);
	}

	private static void handleBetterMaterialsVersions(ItemData data, AtomicReference<? super Materials> toRpl) {
		if(Materials.SteelMagnetic.equals(data.mMaterial.mMaterial)) {
			toRpl.set(Materials.Steel);
		} else if(Materials.IronMagnetic.equals(data.mMaterial.mMaterial)) {
			toRpl.set(Materials.Iron);
		} else if(Materials.NeodymiumMagnetic.equals(data.mMaterial.mMaterial)) {
			toRpl.set(Materials.Neodymium);
		} else if(Materials.SamariumMagnetic.equals(data.mMaterial.mMaterial)) {
			toRpl.set(Materials.Samarium);
		} else if(Materials.AnnealedCopper.equals(data.mMaterial.mMaterial)) {
			toRpl.set(Materials.Copper);
		}
	}

	@SuppressWarnings("deprecation")
	private static void handleCircuits(Materials first, ItemStack[] output, ItemStack input, int i) {
		if(first.equals(Materials.Primitive)) output[i] = ItemList.NandChip.get(input.stackSize);
		else if(first.equals(Materials.Basic)) output[i] = ItemList.Circuit_Microprocessor.get(input.stackSize);
		else if(first.equals(Materials.Good)) output[i] = ItemList.Circuit_Good.get(input.stackSize);
		else if(first.equals(Materials.Advanced)) output[i] = ItemList.Circuit_Advanced.get(input.stackSize);
		else if(first.equals(Materials.Data)) output[i] = ItemList.Circuit_Data.get(input.stackSize);
		else if(first.equals(Materials.Master)) output[i] = ItemList.Circuit_Master.get(input.stackSize);
		else if(first.equals(Materials.Ultimate)) output[i] = ItemList.Circuit_Quantummainframe.get(input.stackSize);
		else if(first.equals(Materials.Superconductor))
			output[i] = ItemList.Circuit_Crystalmainframe.get(input.stackSize);
		else if(first.equals(Materials.Infinite)) output[i] = ItemList.Circuit_Wetwaremainframe.get(input.stackSize);
		else if(first.equals(Materials.Bio)) output[i] = ItemList.Circuit_Biomainframe.get(input.stackSize);
	}

	private static ItemStack handleUnification(ItemStack stack) {
		for(int oreID : OreDictionary.getOreIDs(stack)) {
			for(var entry : ORE_DICT_OVERRIDE) {
				if(OreDictionary.getOreName(oreID).equals(entry[0])) {
					ItemStack ret = ((ItemStack) entry[1]).copy();
					ret.stackSize = stack.stackSize;
					return ret;
				}
			}
		}
		return GT_OreDictUnificator.get(stack);
	}

	private static ItemStack handleWildcard(ItemStack stack) {
		assert stack == null || stack.getItem() != null;

		if(stack != null && stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && !stack.getItem().isDamageable()) {
			stack.setItemDamage(0);
		}
		return stack;
	}

	private static ItemStack handleContainerItem(ItemStack stack) {
		assert stack == null || stack.getItem() != null;

		if(stack != null && stack.getItem().hasContainerItem(stack)) {
			return null;
		}
		return stack;
	}

	// endregion

	// region static helper functions

	public static boolean isCircuit(ItemStack stack) {
		ItemData data = GT_OreDictUnificator.getAssociation(stack);
		if(data != null) {
			return data.mPrefix == OrePrefixes.circuit;
		}
		return false;
	}

	public static boolean isUnpackerRecipe(ItemStack stack) {
		return RecipeMaps.unpackagerRecipes
			.findRecipe(null, true, true, Long.MAX_VALUE, null, stack) != null;
	}

	// endregion

}
