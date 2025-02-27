package cn.elytra.mod.gtnn.util

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import org.apache.logging.log4j.Logger

internal fun Logger.logRecipe(inputs: Array<Any?>, outputs: Array<Any?>) {
	info("Recipe:")
	info("\tmInputs: [{}]", inputs.joinToString(", ") { anyInRecipeToString(it) })
	info("\tmOutputs: [{}]", outputs.joinToString(", ") { anyInRecipeToString(it) })
}

internal fun anyInRecipeToString(a: Any?): String = when(a) {
	null -> "NULL"
	is String -> a
	is Item -> a.unlocalizedName
	is ItemStack -> a.toString()
	else -> a.toString()
}
