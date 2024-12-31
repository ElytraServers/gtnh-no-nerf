package cn.elytra.mod.gtnn.rewind

import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import gregtech.api.util.GTModHandler
import net.minecraftforge.common.config.Configuration

interface IModule {

	/**
	 * If false, the methods will not be invoked.
	 *
	 * It should not be changed after [readConfig].
	 */
	val enabled: Boolean

	/**
	 * Read the configuration for the module.
	 *
	 * This will always be invoked no matter if [enabled].
	 */
	fun readConfig(configuration: Configuration)

	fun fmlPreInit(e: FMLPreInitializationEvent) {}
	fun fmlInit(e: FMLInitializationEvent) {}
	fun fmlPostInit(e: FMLPostInitializationEvent) {}
	fun fmlComplete(e: FMLLoadCompleteEvent) {}

	/**
	 * Register gregtech items.
	 */
	fun registerGregTechItems() {}

	/**
	 * Register recipes.
	 */
	fun registerRecipes() {}

	/**
	 * Fired by `FMLLoadCompleteEvent`, used to collect the all other registered recipes.
	 */
	fun loadRecipesOnComplete() {}

	companion object {
		val DefaultMachineRecipeMask = GTModHandler.RecipeBits.DISMANTLEABLE or
			GTModHandler.RecipeBits.BUFFERED or
			GTModHandler.RecipeBits.NOT_REMOVABLE or
			GTModHandler.RecipeBits.REVERSIBLE

		internal fun getPreferredTieredMachineLocalizationKey(tier: Int, name: String): String {
			return "BasicMachine.${name}.tier.${tier.toString().padStart(2, '0')}".lowercase()
		}

		/**
		 * Get the preferred name of machines used in GT and GT++, like Basic Disassembler, Epic Deconstructor.
		 */
		internal fun getPreferredTieredMachineNameRegional(
			tier: Int,
			basicNameRegional: String,
			advancedNameRegional: String = basicNameRegional,
		): String {
			return when(tier) {
				0 -> "Steam $basicNameRegional"
				1 -> "Basic $basicNameRegional"
				2 -> "Advanced $basicNameRegional"
				3 -> "Advanced $basicNameRegional II"
				4 -> "Advanced $basicNameRegional III"
				5 -> "Advanced $basicNameRegional IV"
				6 -> "Elite $advancedNameRegional"
				7 -> "Elite $advancedNameRegional I"
				8 -> "Ultimate $advancedNameRegional"
				9 -> "Epic $advancedNameRegional"
				10 -> "Epic $advancedNameRegional II"
				11 -> "Epic $advancedNameRegional III"
				12 -> "Epic $advancedNameRegional IV"
				else -> "$advancedNameRegional (T$tier)"
			}
		}
	}

}
