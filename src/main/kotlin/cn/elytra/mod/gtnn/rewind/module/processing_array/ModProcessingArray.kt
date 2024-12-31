package cn.elytra.mod.gtnn.rewind.module.processing_array

import cn.elytra.mod.gtnn.rewind.IModule
import cn.elytra.mod.gtnn.util.copyOf
import gregtech.api.enums.ItemList
import gregtech.api.util.GTModHandler
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration

// see https://github.com/GTNewHorizons/GT5-Unofficial/pull/2273
object ModProcessingArray : IModule {

	override var enabled: Boolean = false
	private var processingArrayId: Int = 6600

	lateinit var ProcessingArray: ItemStack

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean("enabled", "processing-array", enabled, "enable gtnn processing array")
		processingArrayId = configuration.getInt(
			"id",
			"processing-array",
			processingArrayId,
			0,
			32767,
			"the mte id of gtnn processing array"
		)
	}

	override fun registerGregTechItems() {
		ProcessingArray = MTEProcessingArray(processingArrayId, "multimachine.processingarray", "Processing Array")
			.getStackForm(1)
	}

	override fun registerRecipes() {
		GTModHandler.addShapelessCraftingRecipe(
			ProcessingArray.copyOf(),
			IModule.DefaultMachineRecipeMask,
			arrayOf(ItemList.Processing_Array.get(1)),
		)
	}
}
