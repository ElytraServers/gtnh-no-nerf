package cn.elytra.mod.gtnn.rewind.module.disassembler

import cn.elytra.mod.gtnn.rewind.IModule
import cn.elytra.mod.gtnn.util.VoltageIndexedMap
import cn.elytra.mod.gtnn.util.withOffsetStartBy
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration

object ModDisassembler : IModule {

	override var enabled: Boolean = true

	private var offsetBasicVersion = 451
	private var offsetAdvancedVersion = 11160

	val Disassembler: VoltageIndexedMap<ItemStack> = VoltageIndexedMap()

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean("enabled", "disassembler", enabled, "enable disassemblers")
		offsetBasicVersion = configuration.getInt(
			"disassembler-id-t1",
			"disassembler",
			offsetBasicVersion,
			0,
			32767,
			"the mte id of disassemblers from T1-T5; you should ensure that from value to value+4 are all available."
		)
		offsetAdvancedVersion = configuration.getInt(
			"disassembler-id-t6",
			"disassembler",
			offsetAdvancedVersion,
			0,
			32767,
			"the mte id of disassembler from T6-T12; you should ensure that from value to value+6 are all available."
		)
	}

	override fun registerGregTechItems() {
		// disassembler in GT
		for((tier, mteId) in 1..5 withOffsetStartBy offsetBasicVersion) {
			val stack = MTEDisassembler(
				mteId,
				IModule.getPreferredTieredMachineLocalizationKey(tier, "disassembler"),
				IModule.getPreferredTieredMachineNameRegional(tier, "Disassembler", "Deconstructor"),
				tier,
			).getStackForm(1)
			Disassembler.put(tier, stack)
		}
		// disassembler in GT++
		for((tier, mteId) in 6..12 withOffsetStartBy offsetAdvancedVersion) {
			val stack = MTEDisassembler(
				mteId,
				IModule.getPreferredTieredMachineLocalizationKey(tier, "disassembler"),
				IModule.getPreferredTieredMachineNameRegional(tier, "Disassembler", "Deconstructor"),
				tier,
			).getStackForm(1)
			Disassembler.put(tier, stack)
		}
	}

	override fun loadRecipesOnComplete() {
		DisassemblerRecipes.loadAssemblerRecipes()
		ReversedRecipeRegistry.registerAllReversedRecipes()
	}
}
