package cn.elytra.mod.gtnn.rewind.module.disassembler

import cn.elytra.mod.gtnn.GtnhNoNerf
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack

object ReversedRecipeRegistry {

	/**
	 * The list used to store all the **REVERSED** recipes.
	 */
	internal val Registry = arrayListOf<GTRecipe>().apply { ensureCapacity(2048) }

	@JvmStatic
	fun registerShaped(output: ItemStack, recipe: Array<Any?>) {
		runCatching {
			GTUtility.reverseShapedRecipe(output, *recipe).ifPresent(Registry::add)
		}.onFailure {
			GtnhNoNerf.LOG.warn("An invalid reversed recipe was found with exception, output: {}, inputs: {}", output, recipe, it)
		}
	}

	@JvmStatic
	fun registerShapeless(output: ItemStack, recipe: Array<Any?>) {
		runCatching {
			GTUtility.reverseShapelessRecipe(output, *recipe).ifPresent(Registry::add)
		}.onFailure {
			GtnhNoNerf.LOG.warn("An invalid reversed recipe was found with exception, output: {}, inputs: {}", output, recipe, it)
		}
	}

	@JvmStatic
	internal fun registerAllReversedRecipes() {
		Registry.forEach { DisassemblerRecipes.registerReversedCraftingRecipe(it) }
	}

}
