package cn.taskeren.gtnn.mod.gt5u.util;

import cn.taskeren.gtnn.util.KtCandy;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.util.data.ArrayUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * The modified version of the Disassembler recipe processing/transforming methods.
 * <p>
 * You may find the class of the latest version before removal <a href="https://github.com/GTNewHorizons/GT5-Unofficial/blob/c68c9019d1285f48436661a031b4aff3823b2a37/src/main/java/gregtech/common/tileentities/machines/basic/GT_MetaTileEntity_Disassembler.java">here</a>.
 */
public class DisassemblerRecipeHelper {

	/**
	 * The always-replace pairs.
	 * <p>
	 * Array<(toBeReplaced: ItemStack, replacement: ItemStack)>
	 */
	private static final ItemStack[][] ALWAYS_REPLACE = {
			{new ItemStack(Blocks.trapped_chest, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.chest, 1, OreDictionary.WILDCARD_VALUE)}
	};

	/**
	 * The always-replace pairs.
	 * <p>
	 * Array<(oreDictName: String, replacement: ItemStack)>
	 */
	private static final Object[][] ORE_DICT_REPLACE = {
			{"plankWood", new ItemStack(Blocks.planks)},
			{"stoneCobble", new ItemStack(Blocks.cobblestone)},
			{"gemDiamond", new ItemStack(Items.diamond)},
			{"logWood", new ItemStack(Blocks.log)},
			{"stickWood", new ItemStack(Items.stick)},
			{"treeSapling", new ItemStack(Blocks.sapling)}
	};

	/**
	 * Turn the rawOutputs into nice retOutputs.
	 * <p>
	 * The steps are:
	 * <ol>
	 *     <li>
	 *         <b>Find the cheaper materials between recipes.</b>
	 *         E.g.: Both recipes have a metal output, one is WroughtIron, and the other is Iron,
	 *         we choose Iron as it is cheaper.
	 *     </li>
	 *     <li>
	 *         <b>Turn AnyMaterial to Material.</b>
	 *         E.g.: Find AnyCopper, just turn it into Copper.
	 *     </li>
	 *     <li>
	 *         <b>Find unprocessed one.</b>
	 *         E.g.: Magnetic things like Magnetic Iron will be turned into Iron.
	 *         AnnealedCopper will be turned into Copper.
	 *     </li>
	 *     <li>
	 *         <b>Find the most downgraded circuit.</b>
	 *         The transformation rule is in {@link #getCheapestCircuit(Materials)}.
	 *     </li>
	 *     <li>
	 *         <b>Perform the forced replacements.</b>
	 *         The replacement rule is in {@link #ALWAYS_REPLACE} and {@link #ORE_DICT_REPLACE}.
	 *     </li>
	 *     <li>
	 *         <b>Other stuff.</b>
	 *         See {@link #handleUnification(ItemStack)}, {@link #handleWildcard(ItemStack)}, and {@link #handleContainerItem(ItemStack)}.
	 *     </li>
	 *
	 * @param rawOutputs          the output itemstacks before processed.
	 * @param inputItemsOfRecipes the output itemstacks of other recipes.
	 * @return the processed output itemstacks.
	 */
	public static ItemStack[] handleRecipeTransformation(
			@NotNull
			ItemStack[] rawOutputs,
			@Nullable
			Set<ItemStack[]> inputItemsOfRecipes // "inputStacks" in the legacy codes
	) {
		// the final result!
		ItemStack[] retOutputs = new ItemStack[rawOutputs.length];

		// walk through the firstOutputs
		iterateRecipe:
		// <--- used to jump out the inner loop
		for(int idx = 0; idx < rawOutputs.length; idx++) {
			// region handleRecipeTransformationInternal
			// get the stack and data for the item iterated.
			var itemInSlotIdx = rawOutputs[idx];
			var itemDataInSlotIdx = GT_OreDictUnificator.getItemData(itemInSlotIdx);

			// if nothing can be transformed, just skip transformation for this item.
			if(itemDataInSlotIdx == null || itemDataInSlotIdx.mMaterial == null || itemDataInSlotIdx.mMaterial.mMaterial == null || itemDataInSlotIdx.mPrefix == null) {
				retOutputs[idx] = itemInSlotIdx;
				continue;
			}

			// region handleReplacement

			// true if the item has been downgraded in the 1st step.
			// then don't continue to downgrade again.
			var materialMaterialOfFirst = itemDataInSlotIdx.mMaterial.mMaterial;

			// region handleInputStacks
			// if there are some other assembler recipes,
			// we should compare the items in the SAME slot, and get the cheaper one.
			if(inputItemsOfRecipes != null) {
				for(ItemStack[] inputsInOtherRecipes : inputItemsOfRecipes) {
					var dataAgainst = GT_OreDictUnificator.getItemData(inputsInOtherRecipes[idx]);
					if(!(dataAgainst == null || dataAgainst.mMaterial == null || dataAgainst.mMaterial.mMaterial == null || dataAgainst.mPrefix != itemDataInSlotIdx.mPrefix)) {
						// region handleDifferentMaterialsOnRecipes
						// replaces the cheaper item in the same slot.
						// (e.g.: Aluminum -> Iron)
						var cheaper = getCheaperMaterialsBetweenTwo(materialMaterialOfFirst, dataAgainst.mMaterial.mMaterial);
						if(cheaper != null) {
							retOutputs[idx] = GT_OreDictUnificator.get(
									OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name()),
									cheaper,
									itemInSlotIdx.stackSize
							);
							continue iterateRecipe; // <--- done with this slot, no more downgrading.
						}
						// endregion

						// region handleAnyMaterials
						// get the more basic item in the slot.
						var nonAny = getNonAnyMaterials(materialMaterialOfFirst);
						if(nonAny != null) {
							retOutputs[idx] = GT_OreDictUnificator.get(
									OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name()),
									nonAny,
									itemInSlotIdx.stackSize
							);
							continue iterateRecipe; // <--- done with this slot, no more downgrading.
						}
						// endregion
					}
				}
			}
			// endregion

			// region handleBetterMaterialsVersions
			// get the unprocessed item. (e.g.: AnnealedCopper -> Copper, IronMagnetic -> Iron)
			var unprocessed = getUnprocessedMaterials(materialMaterialOfFirst);
			if(unprocessed != null) {
				retOutputs[idx] = GT_OreDictUnificator.get(
						OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name()),
						unprocessed,
						itemInSlotIdx.stackSize
				);
				continue;
			}
			// endregion

			// region handleCircuits
			// if the item is CIRCUIT, replace with the cheapest (most downgrade) circuit.
			if(itemDataInSlotIdx.mPrefix == OrePrefixes.circuit) {
				var circuit = getCheapestCircuit(materialMaterialOfFirst);
				if(circuit != null) {
					circuit.stackSize = itemInSlotIdx.stackSize;
					retOutputs[idx] = circuit;
				}
				// continue; <--- useless, but add it for structural beauty.
			}
			// endregion

			// endregion handleReplacement

			// endregion handleRecipeTransformationInternal
		}

		// region addOthersAndHandleAlwaysReplace
		// walk through again
		for(int i = 0; i < rawOutputs.length; i++) {
			// add items if did not be downgraded and replaced
			if(retOutputs[i] == null) {
				retOutputs[i] = rawOutputs[i];
			}

			// select smaller amount
			if(GT_Utility.areStacksEqual(retOutputs[i], rawOutputs[i])) {
				retOutputs[i].stackSize = Math.min(retOutputs[i].stackSize, rawOutputs[i].stackSize);
			}

			// process ALWAYS_REPLACE
			for(var pair : ALWAYS_REPLACE) {
				if(GT_Utility.areStacksEqual(retOutputs[i], pair[0], true)) {
					retOutputs[i] = pair[1].copy();
					break; // <--- break the ALWAYS_REPLACE for-each loop
				}
			}

			retOutputs[i] = handleUnification(retOutputs[i]);
			retOutputs[i] = handleWildcard(retOutputs[i]);
			retOutputs[i] = handleContainerItem(retOutputs[i]);
		}
		// endregion

		// need to filter null element
		return ArrayUtils.removeNulls(retOutputs);
	}

	/**
	 * Get the cheaper Materials.
	 * <p>
	 * Except: Aluminum, WroughtIron => Iron
	 *
	 * @param first  the first materials.
	 * @param second the second materials.
	 * @return the cheaper materials. null if nothing is going to be replaced.
	 */
	@Nullable
	private static Materials getCheaperMaterialsBetweenTwo(Materials first, Materials second) {
		// return anyone if they are the same.
		if(first.equals(second)) return null;

		// if the second material is cheaper, return the second one
		// only except is Aluminum compare to WroughtIron result in normal Iron.
		if(first.equals(Materials.Aluminium) && second.equals(Materials.Iron)) return second;
		else if(first.equals(Materials.Steel) && second.equals(Materials.Iron)) return second;
		else if(first.equals(Materials.WroughtIron) && second.equals(Materials.Iron)) return second;
		else if(first.equals(Materials.Aluminium) && second.equals(Materials.WroughtIron)) return Materials.Iron;
		else if(first.equals(Materials.Aluminium) && second.equals(Materials.Steel)) return second;
		else if(first.equals(Materials.Polytetrafluoroethylene) && second.equals(Materials.Plastic)) return second;
		else if(first.equals(Materials.Polybenzimidazole) && second.equals(Materials.Plastic)) return second;
		else if(first.equals(Materials.Polystyrene) && second.equals(Materials.Plastic)) return second;
		else if(first.equals(Materials.Silicone) && second.equals(Materials.Plastic)) return second;
		else if(first.equals(Materials.NetherQuartz) || first.equals(Materials.CertusQuartz) && second.equals(Materials.Quartzite))
			return second;
		else if(first.equals(Materials.Plastic) && second.equals(Materials.Wood)) return second;
		else if(first.equals(Materials.Diamond) && second.equals(Materials.Glass)) return second;

		return null;
	}

	/**
	 * Get the base(cheaper) Materials.
	 *
	 * @param first the materials.
	 * @return the base materials. null if nothing to be replaced.
	 */
	@Nullable
	private static Materials getNonAnyMaterials(Materials first) {
		if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyIron)))
			return Materials.Iron;
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyCopper)))
			return Materials.Copper;
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyRubber)))
			return Materials.Rubber;
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyBronze)))
			return Materials.Bronze;
		else if(first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnySyntheticRubber)))
			return Materials.Rubber;
		return null;
	}

	/**
	 * Get the unprocessed Materials.
	 *
	 * @param first the materials.
	 * @return the base materials. null if nothing to be replaced.
	 */
	@Nullable
	private static Materials getUnprocessedMaterials(Materials first) {
		if(first.equals(Materials.SteelMagnetic)) return Materials.Steel;
		if(first.equals(Materials.IronMagnetic)) return Materials.Iron;
		if(first.equals(Materials.NeodymiumMagnetic)) return Materials.Neodymium;
		if(first.equals(Materials.SamariumMagnetic)) return Materials.Samarium;
		if(first.equals(Materials.AnnealedCopper)) return Materials.Copper;

		return null;
	}

	/**
	 * Get the cheapest circuit in the same tier.
	 * The result is at amount 1, you need to update the stack size manually!
	 *
	 * @param first the materials.
	 * @return 1x the cheapest circuit item. null if nothing to be replaced.
	 */
	@SuppressWarnings("deprecation")
	@Nullable
	private static ItemStack getCheapestCircuit(Materials first) {
		if(first.equals(Materials.Primitive)) return ItemList.NandChip.get(1);
		else if(first.equals(Materials.Basic)) return ItemList.Circuit_Microprocessor.get(1);
		else if(first.equals(Materials.Good)) return ItemList.Circuit_Good.get(1);
		else if(first.equals(Materials.Advanced)) return ItemList.Circuit_Advanced.get(1);
		else if(first.equals(Materials.Data)) return ItemList.Circuit_Data.get(1);
		else if(first.equals(Materials.Master)) return ItemList.Circuit_Master.get(1);
		else if(first.equals(Materials.Ultimate)) return ItemList.Circuit_Quantummainframe.get(1);
		else if(first.equals(Materials.Superconductor)) return ItemList.Circuit_Crystalmainframe.get(1);
		else if(first.equals(Materials.Infinite)) return ItemList.Circuit_Wetwaremainframe.get(1);
		else if(first.equals(Materials.Bio)) return ItemList.Circuit_Biomainframe.get(1);

		return null;
	}

	/**
	 * Get the unified item.
	 * <p>
	 * If it matches the ORE_DICT_REPLACE, it will be replaced to the replacement.
	 *
	 * @param stack the source item.
	 * @return the unified item.
	 */
	private static ItemStack handleUnification(ItemStack stack) {
		for(var oreId : OreDictionary.getOreIDs(stack)) {
			for(var pair : ORE_DICT_REPLACE) {
				var oreDictName = (String) pair[0];
				var replacement = (ItemStack) pair[1];
				// if matched, replace with the replacement.
				if(OreDictionary.getOreName(oreId).equals(oreDictName)) {
					return KtCandy.apply(replacement.copy(), i -> i.stackSize = stack.stackSize);
				}
			}
		}
		return GT_OreDictUnificator.get(stack);
	}

	// need document
	private static ItemStack handleWildcard(ItemStack stack) {
		// if stack is not null, then the item in the stack is not null.
		assert stack == null || stack.getItem() != null;

		if(stack != null && stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && !stack.getItem().isDamageable()) {
			stack.setItemDamage(0);
		}
		return stack;
	}

	// need document
	@Nullable
	private static ItemStack handleContainerItem(ItemStack stack) {
		assert stack == null || stack.getItem() != null;

		if(stack != null && stack.getItem().hasContainerItem(stack)) {
			return null;
		}
		return stack;
	}

	/**
	 * Create a reversed Assembler recipe.
	 * The fluid is not exchanged, but it should never be used.
	 *
	 * @param recipe the forwarding recipe from Assemblers
	 * @return the input-output-exchanged recipe from the original one
	 */
	public static GT_Recipe getReversedRecipe(GT_Recipe recipe) {
		var ret = recipe.copy();

		ret.mInputs = recipe.mOutputs;
		ret.mOutputs = recipe.mInputs;

		return ret;
	}

}
