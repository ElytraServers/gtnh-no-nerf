package cn.elytra.mod.gtnn.modules.simple.module.waterline_skip

import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack

/**
 * This class tracks the new/rewind catalysts.
 *
 * @see gtPlusPlus.core.util.minecraft.ItemUtils.isCatalyst
 */
object CatalystInject {

	private val addedCatalysts: MutableList<ItemStack> = mutableListOf()

	@JvmStatic
	fun isAddedCatalyst(stack: ItemStack): Boolean {
		return addedCatalysts.any { GTUtility.areStacksEqual(stack, it, true) }
	}

	fun registerAddedCatalyst(stack: ItemStack) {
		addedCatalysts += stack
	}

}
