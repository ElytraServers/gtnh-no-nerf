package cn.elytra.mod.gtnn.rewind.module.large_processing_factory

import cn.elytra.mod.gtnn.rewind.IModule
import cn.elytra.mod.gtnn.util.copyOf
import gregtech.api.util.GTModHandler
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration

object ModLargeProcessingFactory : IModule {

	override var enabled = false

	private var largeProcessingFactoryId = 680

	lateinit var LargeProcessingFactory: ItemStack

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean(
			"enabled",
			"large_processing_factory",
			enabled,
			"enable gtnn large processing factory"
		)
		largeProcessingFactoryId = configuration.getInt(
			"id",
			"large_processing_factory",
			largeProcessingFactoryId,
			0,
			32767,
			"the mte id of gtnn large processing factory"
		)
	}

	override fun registerGregTechItems() {
		LargeProcessingFactory = MTELargeProcessingFactory(
			10680,
			"industrialmultimachine.controller.tier.single",
			"Large Processing Factory",
		).getStackForm(1)
	}

	override fun registerRecipes() {
		GTModHandler.addShapelessCraftingRecipe(
			LargeProcessingFactory.copyOf(),
			IModule.DefaultMachineRecipeMask,
			arrayOf(GregtechItemList.Industrial_MultiMachine.get(1))
		)
	}
}
