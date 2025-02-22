package cn.elytra.mod.gtnn.modules.simple.module.disassembler

import cn.elytra.mod.gtnn.util.GTNNText
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes
import gregtech.api.enums.SoundResource
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMapBackend
import net.minecraft.item.ItemStack

class MTEDisassembler : MTEBasicMachineWithRecipe {

	companion object {
		val RecipeMap: RecipeMap<RecipeMapBackend> get() = DisassemblerRecipes.DISASSEMBLER_RECIPES

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
