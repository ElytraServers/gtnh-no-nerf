package cn.elytra.mod.gtnhkt.utils

import gregtech.api.util.GTUtility

object GTUtilityKt {

	fun formatNumbers(aNumber: Number): String = when(aNumber) {
		is Int, is Long, is Byte, is Short -> GTUtility.formatNumbers(aNumber.toLong())
		is Float, is Double -> GTUtility.formatNumbers(aNumber.toDouble())
		else -> GTUtility.formatNumbers(aNumber.toLong())
	}

	fun getTier(aNumber: Number): Int = GTUtility.getTier(aNumber.toLong()).toInt()

}
