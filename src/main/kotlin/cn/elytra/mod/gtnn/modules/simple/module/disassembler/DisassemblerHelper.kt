package cn.elytra.mod.gtnn.modules.simple.module.disassembler

import cn.elytra.mod.gtnn.GTNN
import cn.elytra.mod.gtnn.modules.simple.module.disassembler.DisassemblerHelper.oreDictReplace
import com.google.common.collect.ArrayListMultimap
import gregtech.api.enums.*
import gregtech.api.items.MetaGeneratedTool
import gregtech.api.objects.GTItemStack
import gregtech.api.objects.ItemData
import gregtech.api.recipe.RecipeMaps
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTUtility
import ic2.api.item.IC2Items
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import kotlin.math.min

object DisassemblerHelper {

	private val alwaysReplace by lazy {
		listOf(
			ItemStack(Blocks.trapped_chest, 1, OreDictionary.WILDCARD_VALUE) to
				ItemStack(Blocks.chest, 1, OreDictionary.WILDCARD_VALUE)
		)
	}

	private val oreDictReplace by lazy {
		listOf(
			"plankWood" to ItemStack(Blocks.planks),
			"stoneCobble" to ItemStack(Blocks.cobblestone),
			"gemDiamond" to ItemStack(Items.diamond),
			"logWood" to ItemStack(Blocks.log),
			"stickWood" to ItemStack(Items.stick),
			"treeSapling" to ItemStack(Blocks.sapling),
		)
	}

	fun handleRecipeTransformation(
		outputs: Array<ItemStack>,
		outputsInOtherRecipes: Set<Array<ItemStack>>?,
	): List<ItemStack> {
		// the outputs
		val retOutputs = arrayOfNulls<ItemStack>(outputs.size)

		forOutputSlots@ for((idx, itemInSlotIdx) in outputs.withIndex()) {
			// check if the material is present, so that we can handle it on GT materials system.
			// if not, just simply skip it.
			val itemDataInSlotIdx: ItemData? = GTOreDictUnificator.getItemData(itemInSlotIdx)
			if(itemDataInSlotIdx == null
				|| itemDataInSlotIdx.mMaterial == null
				|| itemDataInSlotIdx.mMaterial.mMaterial == null
				|| itemDataInSlotIdx.mPrefix == null
			) {
				retOutputs[idx] = itemInSlotIdx
				continue
			}

			// the material is present, then compare the same slots in other recipes if there are some.
			// if there is no other recipes for this, just simply skip.
			val thisMaterial = itemDataInSlotIdx.mMaterial.mMaterial
			if(outputsInOtherRecipes != null) {
				for(outputsInOtherRecipe in outputsInOtherRecipes) {
					val dataAgainst = GTOreDictUnificator.getItemData(outputsInOtherRecipe[idx])
					if(!(dataAgainst == null
							|| dataAgainst.mMaterial == null
							|| dataAgainst.mMaterial.mMaterial == null
							|| dataAgainst.mPrefix != itemDataInSlotIdx.mPrefix)
					) {
						// replace with cheaper materials.
						// like Aluminum -> Iron, if there is an iron recipe provided.
						val cheaper =
							replaceCheaperOrNull(thisMaterial, dataAgainst.mMaterial.mMaterial)
						if(cheaper != null) {
							retOutputs[idx] = GTOreDictUnificator.get(
								OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name),
								cheaper,
								itemInSlotIdx.stackSize.toLong(),
							)
							continue@forOutputSlots
						}

						// replace "Any" Materials to non-any ones.
						// like AnyIron -> Iron, AnyRubber -> Rubber, etc.
						val nonAny = replaceAnyOrNull(thisMaterial)
						if(nonAny != null) {
							retOutputs[idx] = GTOreDictUnificator.get(
								OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name),
								nonAny,
								itemInSlotIdx.stackSize.toLong(),
							)
							continue@forOutputSlots
						}
					}
				}
			}

			// replace materials to unprocessed ones.
			// like IronMagnetic -> Iron, AnnealedCopper -> Copper, etc.
			val unprocessed = getUnprocessedMaterials(thisMaterial)
			if(unprocessed != null) {
				retOutputs[idx] = GTOreDictUnificator.get(
					OrePrefixes.valueOf(itemDataInSlotIdx.mPrefix.name),
					unprocessed,
					itemInSlotIdx.stackSize.toLong(),
				)
			}

			// replace the circuits to the cheapest one at the same tier.
			if(itemDataInSlotIdx.mPrefix == OrePrefixes.circuit) {
				val circuit = getCheapestCircuitOrNull(thisMaterial)
				if(circuit != null) {
					retOutputs[idx] = circuit.also { it.stackSize = itemInSlotIdx.stackSize }
				}
			}
		}

		for((idx, itemInSlotIdx) in outputs.withIndex()) {
			// fill the empty slots in the actual output array
			if(retOutputs[idx] == null) {
				retOutputs[idx] = itemInSlotIdx
			}

			// if the stacks are the same, set the output amount to the smaller one.
			if(GTUtility.areStacksEqual(retOutputs[idx], itemInSlotIdx)) {
				retOutputs[idx]!!.stackSize = min(retOutputs[idx]!!.stackSize, itemInSlotIdx.stackSize)
			}

			// handle the "alwaysReplace" list
			for((replaceMatcher, replaceItemStack) in alwaysReplace) {
				if(GTUtility.areStacksEqual(retOutputs[idx], replaceMatcher, true)) {
					retOutputs[idx] = replaceItemStack.copy()
					break
				}
			}

			// final replacements
			retOutputs[idx] = handleUnification(retOutputs[idx])
			retOutputs[idx] = handleWildcard(retOutputs[idx])
			retOutputs[idx] = handleContainerItem(retOutputs[idx])
		}

		return retOutputs.filterNotNull()
	}

	/**
	 * @return the 2nd material if it's cheaper. Otherwise, `null`.
	 */
	private fun replaceCheaperOrNull(first: Materials, second: Materials): Materials? {
		if(first == second) return null

		// if the second material is cheaper, return the second one
		// only except is Aluminum compare to WroughtIron result in normal Iron.
		return when {
			first == Materials.Aluminium && second == Materials.Iron -> second
			first == Materials.Steel && second == Materials.Iron -> second
			first == Materials.WroughtIron && second == Materials.Iron -> second
			first == Materials.Aluminium && second == Materials.WroughtIron -> Materials.Iron
			first == Materials.Aluminium && second == Materials.Steel -> second
			first == Materials.Polytetrafluoroethylene && second == Materials.Plastic -> second
			first == Materials.Polybenzimidazole && second == Materials.Plastic -> second
			first == Materials.Polystyrene && second == Materials.Plastic -> second
			first == Materials.Silicone && second == Materials.Plastic -> second
			first == Materials.NetherQuartz || first == Materials.CertusQuartz && second == Materials.Quartzite -> second
			first == Materials.Plastic && second == Materials.Wood -> second
			first == Materials.Diamond && second == Materials.Glass -> second
			else -> null
		}
	}

	/**
	 * @return non-any Materials if [first] is anyWhat (like AnyIron, AnyRubber, ...). Otherwise, `null`.
	 */
	private fun replaceAnyOrNull(first: Materials): Materials? {
		return when {
			first.mOreReRegistrations.stream().anyMatch { it == Materials.AnyIron } -> {
				Materials.Iron
			}

			first.mOreReRegistrations.stream().anyMatch { it == Materials.AnyCopper } -> {
				Materials.Copper
			}

			first.mOreReRegistrations.stream().anyMatch { it == Materials.AnyRubber } -> {
				Materials.Rubber
			}

			first.mOreReRegistrations.stream().anyMatch { it == Materials.AnyBronze } -> {
				Materials.Bronze
			}

			first.mOreReRegistrations.stream().anyMatch { it == Materials.AnySyntheticRubber } -> {
				Materials.Rubber
			}

			else -> null
		}
	}

	/**
	 * @return the unprocessed Materials (for something like AnnealedCopper, IronMagnetic, ...). Otherwise, `null`.
	 */
	private fun getUnprocessedMaterials(first: Materials): Materials? {
		return when(first) {
			Materials.SteelMagnetic -> Materials.Steel
			Materials.IronMagnetic -> Materials.Iron
			Materials.NeodymiumMagnetic -> Materials.Neodymium
			Materials.SamariumMagnetic -> Materials.Samarium
			Materials.AnnealedCopper -> Materials.Copper
			else -> null
		}
	}

	/**
	 * @return the cheapest circuit in the same tier. Make sure to update the amount when using.
	 */
	private fun getCheapestCircuitOrNull(first: Materials): ItemStack? {
		return when(first) {
			Materials.ULV -> ItemList.NandChip.get(1)
			Materials.LV -> ItemList.Circuit_Microprocessor.get(1)
			Materials.MV -> ItemList.Circuit_Good.get(1)
			Materials.Advanced -> ItemList.Circuit_Advanced.get(1)
			Materials.EV -> ItemList.Circuit_Data.get(1)
			Materials.LuV -> ItemList.Circuit_Master.get(1)
			Materials.Ultimate -> ItemList.Circuit_Quantummainframe.get(1)
			Materials.SuperconductorUHV -> ItemList.Circuit_Crystalmainframe.get(1)
			Materials.UHV -> ItemList.Circuit_Wetwaremainframe.get(1)
			Materials.UEV -> ItemList.Circuit_Biomainframe.get(1)
			else -> null
		}
	}

	/**
	 * Replace the output by [oreDictReplace].
	 *
	 * @return unificated one.
	 */
	private fun handleUnification(stack: ItemStack?): ItemStack? {
		if(stack != null) {
			for(oreId in OreDictionary.getOreIDs(stack)) {
				for((oreDictMatcher, replaceItemStack) in oreDictReplace) {
					if(OreDictionary.getOreName(oreId) == oreDictMatcher) {
						return replaceItemStack.copy().also { it.stackSize = stack.stackSize }
					}
				}
			}
		}
		return GTOreDictUnificator.get(stack)
	}

	private fun handleWildcard(stack: ItemStack?): ItemStack? {
		if(stack != null && stack.itemDamage == OreDictionary.WILDCARD_VALUE && !stack.item.isDamageable) {
			stack.itemDamage = 0
		}
		return stack
	}

	private fun handleContainerItem(stack: ItemStack?): ItemStack? {
		if(stack != null && stack.item.hasContainerItem(stack)) {
			return null
		}
		return stack
	}

	private val inputBlacklist by lazy {
		buildList {
			add(GTItemStack(ItemList.Casing_Coil_Superconductor.get(1)))
			add(GTItemStack(Materials.Graphene.getDust(1)))
			add(GTItemStack(ItemList.Circuit_Parts_Vacuum_Tube.get(1)))
			add(GTItemStack(ItemList.Schematic.get(1)))
			if(Mods.Railcraft.isModLoaded()) {
				add(GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 0)))
				add(GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 736)))
				add(GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "track", 1L, 816)))
			}
			add(GTItemStack(IC2Items.getItem("mixedMetalIngot")))
			add(GTItemStack(GTModHandler.getModItem(Mods.Railcraft.ID, "machine.alpha", 1, 14)))

			// region transformer
			add(GTItemStack(ItemList.Transformer_MV_LV.get(1L)))
			add(GTItemStack(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockElectric", 1L, 3)))
			add(GTItemStack(ItemList.Transformer_HV_MV.get(1L)))
			add(GTItemStack(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockElectric", 1L, 4)))
			add(GTItemStack(ItemList.Transformer_EV_HV.get(1L)))
			add(GTItemStack(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockElectric", 1L, 5)))
			add(GTItemStack(ItemList.Transformer_IV_EV.get(1L)))
			add(GTItemStack(GTModHandler.getModItem(Mods.IndustrialCraft2.ID, "blockElectric", 1L, 6)))

			// endregion transformer
			add(GTItemStack(GTModHandler.getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 36)))
			add(GTItemStack(GTModHandler.getModItem(Mods.AppliedEnergistics2.ID, "item.ItemMultiPart", 1L, 536)))
		}
	}

	private val outputHardOverride by lazy {
		ArrayListMultimap.create<GTItemStack, ItemStack>().apply {
			put(GTItemStack(ItemStack(Blocks.torch, 6)), ItemStack(Items.stick))
		}
	}

	private val overrideEUt = 30
	private val overrideDuration = 600

	fun loadAssemblerRecipesToDisassembler() {
		val assemblerRecipes = RecipeMaps.assemblerRecipes.allRecipes
			.filter { shouldDisassemble(it.mOutputs) }
			.groupBy { it.mOutputs[0] }

		val totalCount = assemblerRecipes.size
		GTNN.logger.info("Loading reversed assembler recipes, total: $totalCount")

		forRecipes@ for((_, recipes) in assemblerRecipes) {
			try {
				val revRecipes = recipes.map { getReversedRecipe(it) }.toMutableList()
				val revRecipeFirst = revRecipes.removeFirst()

				// the input item to disassemble
				val revInput = revRecipeFirst.mInputs.single()

				// handle hard overrides
				for((overrideInput, overrideOutput) in outputHardOverride.entries()) {
					// matched hard override, add the overriden recipe and continue for other inputs
					if(overrideInput.isStackEqual(revInput)) {
						GTValues.RA.stdBuilder()
							.itemInputs(revInput)
							.itemOutputs(overrideOutput)
							.duration(overrideDuration)
							.eut(overrideEUt)
							.addTo(MTEDisassembler.RecipeMap)

						continue@forRecipes
					}
				}

				val outputs =
					handleRecipeTransformation(revRecipeFirst.mOutputs, revRecipes.map { it.mOutputs }.toSet())
						.filter { GTUtility.isStackValid(it) && it.stackSize > 0 }
				val revDuration = revRecipeFirst.mDuration
				val revEUt = revRecipeFirst.mEUt

				GTValues.RA.stdBuilder()
					.itemInputs(revInput)
					.itemOutputs(*outputs.toTypedArray())
					.duration(revDuration)
					.eut(revEUt)
					.addTo(MTEDisassembler.RecipeMap)
			} catch(e: Exception) {
				// @formatter:off
				GTNN.logger.warn("Failed to process the assembler recipe to disassembler recipe, information below.")
				GTNN.logger.warn("mInputs: {}", recipes.firstOrNull()?.mInputs?.joinToString(separator = ", ", prefix = "[", postfix = "]"))
				GTNN.logger.warn("mOutputs: {}", recipes.firstOrNull()?.mOutputs?.joinToString(separator = ", ", prefix = "[", postfix = "]"))
				GTNN.logger.warn("Exception", e)
				// @formatter:on

				// rethrow cetain exceptions
				if(e is ArrayIndexOutOfBoundsException) throw e
			}
		}
	}

	/**
	 * Check if the input is single and valid for disassmbling.
	 */
	private fun shouldDisassemble(mInputsOrOutputs: Array<ItemStack>): Boolean {
		return mInputsOrOutputs.size == 1 && shouldDisassembleItemStack(mInputsOrOutputs[0])
	}

	/**
	 * Check if the input item is valid for disassembling.
	 */
	private fun shouldDisassembleItemStack(stack: ItemStack?): Boolean {
		if(stack == null) return false

		if(stack.item is MetaGeneratedTool) return false
		if(isCircuit(stack)) return false
		if(isOre(stack)) return false
		if(hasUnpackerRecipe(stack)) return false
		if(inputBlacklist.any { GTUtility.areStacksEqual(it.toStack(), stack, true) }) return false

		return true
	}

	private fun isCircuit(stack: ItemStack): Boolean {
		val data = GTOreDictUnificator.getAssociation(stack)
		return data != null && data.mPrefix == OrePrefixes.circuit
	}

	private fun hasUnpackerRecipe(stack: ItemStack): Boolean {
		return RecipeMaps.unpackagerRecipes.findRecipeQuery()
			.items(stack)
			.find() != null
	}

	private fun isOre(stack: ItemStack): Boolean {
		val data = GTOreDictUnificator.getAssociation(stack)
		return data != null && (data.mPrefix == OrePrefixes.ore
			|| data.mPrefix == OrePrefixes.crushed
			|| data.mPrefix == OrePrefixes.crushedCentrifuged
			|| data.mPrefix == OrePrefixes.crushedPurified)
	}

	private fun getReversedRecipe(recipe: GTRecipe): GTRecipe {
		return recipe.copy().apply {
			mInputs = recipe.mOutputs
			mOutputs = recipe.mInputs
		}
	}

	/**
	 * Adds a **REVERSED** recipe to disassembler recipe map.
	 *
	 * The passed-in recipe should have been reversed once before, and we don't reverse it again.
	 */
	fun addCraftingTableReverseRecipe(revRecipe: GTRecipe) {
		if(!shouldDisassemble(revRecipe.mInputs)) return

		try {
			GTValues.RA.stdBuilder()
				.itemInputs(revRecipe.mInputs)
				.itemOutputs(*handleRecipeTransformation(revRecipe.mOutputs, null).toTypedArray())
				.duration(300)
				.eut(30)
				.addTo(MTEDisassembler.RecipeMap)
		} catch(e: Exception) {
			// @formatter:off
			GTNN.logger.warn("Failed to process reversed crafting table recipe to disassembler recipe, information below.")
			GTNN.logger.warn("mInputs: {}", revRecipe.mInputs.joinToString(separator = ", ", prefix = "[", postfix = "]"))
			GTNN.logger.warn("mOutputs: {}", revRecipe.mOutputs.joinToString(separator = ", ", prefix = "[", postfix = "]"))
			GTNN.logger.warn("Exception", e)
			// @formatter:on
		}
	}

}
