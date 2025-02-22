package cn.elytra.mod.gtnn.modules.simple.module.disassembler

import cn.elytra.mod.gtnn.util.GTNNText
import gregtech.api.enums.SoundResource
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe
import gregtech.api.recipe.BasicUIProperties.SlotOverlayGetter
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBackend
import gregtech.api.recipe.RecipeMapBuilder
import net.minecraft.item.ItemStack

class MTEDisassembler : MTEBasicMachineWithRecipe {

	companion object {
		val RecipeMap: RecipeMap<RecipeMapBackend?> = RecipeMapBuilder.of("gtnn.recipe.disassembler")
			.maxIO(1, 9, 0, 0)
			.minInputs(1, 0)
			.slotOverlays(SlotOverlayGetter { index: Int, isFluid: Boolean, isOutput: Boolean, isSpecial: Boolean -> if(!isFluid && !isOutput) GTUITextures.OVERLAY_SLOT_CIRCUIT else null })
			.progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
			.disableOptimize()
			.build()

		val MachineCraftRecipe = arrayOf<Any>(
			"ACA",
			"WHW",
			"ACA",
			'A', X.ROBOT_ARM,
			'C', X.CIRCUIT,
			'W', X.WIRE,
			'H', X.HULL,
		)
	}

	constructor(id: Int, name: String, nameRegional: String, tier: Int) : super(
		id, name, nameRegional, tier,
		"Disassembles items into their components",
		RecipeMap, 1, 9, false,
		SoundResource.NONE, SpecialEffects.NONE, "ASSEMBLER", MachineCraftRecipe.copyOf(),
	)

	override fun addAdditionalTooltipInformation(stack: ItemStack, tooltip: MutableList<String>) {
		tooltip.add(GTNNText.Tooltips.ForDeprecatedMachines)
	}

}
