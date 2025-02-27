package cn.elytra.mod.gtnn.modules.simple.module.disassembler

import cn.elytra.mod.gtnn.GTNN
import cn.elytra.mod.gtnn.util.logRecipe
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack
import java.util.*

object ReversedRecipeRegistry {

	/**
	 * The list used to store all the GT recipes.
	 */
	internal val Registry = arrayListOf<GTCraftingRecipe>().apply { ensureCapacity(2048) }

	interface GTCraftingRecipe {
		val inputs: Array<Any?>
		val output: ItemStack

		val adderStackTrace: Throwable

		fun toReversed(): Optional<GTRecipe>
		fun toReversedSafe(): GTRecipe? {
			try {
				val r = toReversed()
				if(r.isPresent) {
					return r.get()
				} else {
					GTNN.logger.warn("Invalid reversed recipe found during reversing recipe")
				}
			} catch(_: IllegalStateException) {
				GTNN.logger.warn("Invalid recipe found during reversing recipe")
			}
			GTNN.logger.logRecipe(inputs, arrayOf(output))

			return null
		}
	}

	class Shaped(
		override val inputs: Array<Any?>,
		override val output: ItemStack,
		override val adderStackTrace: Throwable = Exception(),
	) : GTCraftingRecipe {
		override fun toReversed(): Optional<GTRecipe> {
			return GTUtility.reverseShapedRecipe(output, *inputs)
		}
	}

	class Shapeless(
		override val inputs: Array<Any?>,
		override val output: ItemStack,
		override val adderStackTrace: Throwable = Exception(),
	) : GTCraftingRecipe {
		override fun toReversed(): Optional<GTRecipe> {
			return GTUtility.reverseShapelessRecipe(output, *inputs)
		}
	}

	@JvmStatic
	fun registerShaped(output: ItemStack, recipe: Array<Any?>) {
		Registry.add(Shaped(recipe, output))
	}

	@JvmStatic
	fun registerShapeless(output: ItemStack, recipe: Array<Any?>) {
		Registry.add(Shapeless(recipe, output))
	}

	@JvmStatic
	internal fun registerAllReversedRecipes() {
		Registry.forEach { DisassemblerHelper.handleGTCraftingRecipe(it) }
	}

}
