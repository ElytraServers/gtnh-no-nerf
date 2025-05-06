package cn.elytra.mod.gtnn.util

import gregtech.api.util.GTModHandler

internal fun getMachineTranslationKey(name: String, tier: Int): String =
	"BasicMachine.${name}.tier.${tier.toString().padStart(2, '0')}".lowercase()

internal fun getMachineEnglishNameInGTStyle(basicEnglishName: String, advancedEnglishName: String, tier: Int): String =
	when(tier) {
		0 -> "Steam $basicEnglishName"
		1 -> "Basic $basicEnglishName"
		2 -> "Advanced $basicEnglishName"
		3 -> "Advanced $basicEnglishName II"
		4 -> "Advanced $basicEnglishName III"
		5 -> "Advanced $basicEnglishName IV"
		6 -> "Elite $advancedEnglishName"
		7 -> "Elite $advancedEnglishName I"
		8 -> "Ultimate $advancedEnglishName"
		9 -> "Epic $advancedEnglishName"
		10 -> "Epic $advancedEnglishName II"
		11 -> "Epic $advancedEnglishName III"
		12 -> "Epic $advancedEnglishName IV"
		else -> "$advancedEnglishName (T$tier)"
	}

internal val DefaultMachineRecipeMask = GTModHandler.RecipeBits.DISMANTLEABLE or
	GTModHandler.RecipeBits.BUFFERED or
	GTModHandler.RecipeBits.NOT_REMOVABLE or
	GTModHandler.RecipeBits.REVERSIBLE
