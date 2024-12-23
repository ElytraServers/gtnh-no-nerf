package cn.elytra.mod.gtnn.util

import gregtech.api.enums.GTValues
import gregtech.api.util.GTLanguageManager
import gregtech.api.util.GTModHandler
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTUtility
import net.minecraft.item.ItemStack

infix fun ItemStack.isStackEqual(other: ItemStack): Boolean {
	return isStackEqual(other, false, false)
}

fun ItemStack.isStackEqual(other: ItemStack, wildcard: Boolean, ignoreNBT: Boolean): Boolean {
	if(GTUtility.isStackInvalid(other)) return false
	return GTUtility.areUnificationsEqual(other, if(wildcard) copyOfWildcard() else copyOf(), ignoreNBT)
}

val ItemStack.isValid get() = GTUtility.isStackValid(this)
val ItemStack.isInvalid get() = GTUtility.isStackInvalid(this)

fun ItemStack.copyOf(amount: Int = 1): ItemStack {
	return GTUtility.copyAmount(amount, GTOreDictUnificator.get(this))
}

fun ItemStack.copyOfWildcard(amount: Int = 1): ItemStack {
	return GTUtility.copyAmountAndMetaData(amount, GTValues.W.toInt(), GTOreDictUnificator.get(this))
}

fun ItemStack.copyOfUndamaged(amount: Int = 1): ItemStack {
	return GTUtility.copyAmountAndMetaData(amount, 0, GTOreDictUnificator.get(this))
}

fun ItemStack.copyOfAlmostBroken(amount: Int = 1): ItemStack {
	return GTUtility.copyAmountAndMetaData(amount, this.maxDamage - 1, GTOreDictUnificator.get(this))
}

fun ItemStack.copyOfWithName(amount: Int = 1, displayName: String): ItemStack? {
	val stack = copyOf()

	val camelCasedDisplayName = buildString {
		displayName.split("\\W".toRegex()).forEach { word ->
			append(word.replaceFirstChar { if(it.isLowerCase()) it.titlecase() else it.toString() })
		}

		if(isEmpty()) {
			append(displayName.hashCode())
		}
	}

	val key = "${stack.unlocalizedName}.with.${camelCasedDisplayName}.name"

	stack.setStackDisplayName(GTLanguageManager.addStringLocalization(key, displayName))
	return GTUtility.copyAmount(amount, stack)
}

fun ItemStack.copyWithCharge(amount: Int = 1, energy: Int): ItemStack {
	val stack = copyOf()

	GTModHandler.chargeElectricItem(stack, energy, Integer.MAX_VALUE, true, false)

	return GTUtility.copyAmount(amount, stack)
}

fun ItemStack.copyWithDamage(amount: Int = 1, metaValue: Int): ItemStack {
	return GTUtility.copyAmountAndMetaData(amount, metaValue, GTOreDictUnificator.get(this))
}

fun ItemStack.registerOre(vararg oreNames: String) {
	oreNames.forEach {
		GTOreDictUnificator.registerOre(it, copyOf())
	}
}

fun ItemStack.registerWildcardAsOre(vararg oreNames: String) {
	oreNames.forEach {
		GTOreDictUnificator.registerOre(it, copyOfWildcard())
	}
}
