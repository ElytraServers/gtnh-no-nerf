package cn.elytra.mod.gtnn.rewind

import cn.elytra.mod.gtnn.rewind.module.disassembler.ModDisassembler
import cn.elytra.mod.gtnn.rewind.module.indium_comb_proc.ModIndiumCombProc
import cn.elytra.mod.gtnn.rewind.module.large_essentia_generator.ModLargeEssentiaGenerator
import cn.elytra.mod.gtnn.rewind.module.large_processing_factory.ModLargeProcessingFactory
import cn.elytra.mod.gtnn.rewind.module.no_default_server_list.ModAntiDefaultServerList
import cn.elytra.mod.gtnn.rewind.module.processing_array.ModProcessingArray
import cn.elytra.mod.gtnn.rewind.module.waterline_skip.ModWaterlineSkip
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager

object ModularLoader {

	private val LOG = LogManager.getLogger()

	private val Modules = mutableListOf<IModule>()

	private val EnabledModules get() = Modules.filter { it.enabled }

	init {
		Modules += ModDisassembler
		Modules += ModLargeProcessingFactory
		Modules += ModWaterlineSkip
		Modules += ModIndiumCombProc
		Modules += ModProcessingArray
		Modules += ModLargeEssentiaGenerator

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
