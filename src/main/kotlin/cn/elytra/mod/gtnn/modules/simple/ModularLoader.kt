package cn.elytra.mod.gtnn.modules.simple

import cn.elytra.mod.gtnn.modules.simple.module.disassembler.ModDisassembler
import cn.elytra.mod.gtnn.modules.simple.module.indium_comb_proc.ModIndiumCombProc
import cn.elytra.mod.gtnn.modules.simple.module.large_essentia_generator.ModLargeEssentiaGeneratorRemovedRecipe
import cn.elytra.mod.gtnn.modules.simple.module.large_processing_factory.ModLargeProcessingFactory
import cn.elytra.mod.gtnn.modules.simple.module.large_processing_factory.ModLargeProcessingFactoryRemovedRecipe
import cn.elytra.mod.gtnn.modules.simple.module.no_default_server_list.ModAntiDefaultServerList
import cn.elytra.mod.gtnn.modules.simple.module.processing_array.ModProcessingArray
import cn.elytra.mod.gtnn.modules.simple.module.processing_array.ModProcessingArrayRemovedRecipe
import cn.elytra.mod.gtnn.modules.simple.module.waterline_skip.ModWaterlineSkip
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import kotlin.collections.plusAssign

object ModularLoader {

	private val LOG = LogManager.getLogger()

	private val Modules = mutableListOf<IModule>()

	private val EnabledModules get() = Modules.filter { it.enabled }

	init {
		// [re-added machines]
		// machines that have been totally removed from GTNH, and we copy its code and make it work again
		Modules += ModDisassembler
		Modules += ModLargeProcessingFactory
		Modules += ModProcessingArray

		// [removed recipes for machines]
		// machines that have been tagged as deprecated while their recipes are removed,
		// and we copy its recipe code and make it craftable again
		// once these machines are fully removed, they will be added back
		Modules += ModLargeProcessingFactoryRemovedRecipe
		Modules += ModProcessingArrayRemovedRecipe
		Modules += ModLargeEssentiaGeneratorRemovedRecipe

		// [removed recipe lines]
		// recipes of certain lines that have been removed from GTNH,
		// and we copy its recipe code and make it craftable again
		Modules += ModWaterlineSkip
		Modules += ModIndiumCombProc

		// misc.
		Modules += ModAntiDefaultServerList
	}

	fun fmlPreInit(e: FMLPreInitializationEvent) {
		LOG.info("Reading configuration")
		val config = Configuration(e.suggestedConfigurationFile)
		Modules.forEach {
			it.readConfig(config)
		}
		config.save()
		EnabledModules.forEach {
			it.fmlPreInit(e)
		}
		LOG.info("Enabled modules (${EnabledModules.size}): [${EnabledModules.joinToString(", ")}]")
	}

	fun fmlInit(e: FMLInitializationEvent) {
		EnabledModules.forEach {
			it.fmlInit(e)
		}
	}

	fun fmlPostInit(e: FMLPostInitializationEvent) {
		EnabledModules.forEach {
			it.fmlPostInit(e)
		}
		EnabledModules.forEach {
			it.registerGregTechItems()
		}
		EnabledModules.forEach {
			it.registerRecipes()
		}
	}

	fun fmlComplete(e: FMLLoadCompleteEvent) {
		EnabledModules.forEach {
			it.fmlComplete(e)
		}
		EnabledModules.forEach {
			it.loadRecipesOnComplete()
		}
	}
}
