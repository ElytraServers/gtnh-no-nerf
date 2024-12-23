package cn.elytra.mod.gtnn.rewind

import cn.elytra.mod.gtnn.rewind.module.disassembler.ModDisassembler
import cn.elytra.mod.gtnn.rewind.module.large_processing_factory.ModLargeProcessingFactory
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
	}

	fun fmlPreInit(e: FMLPreInitializationEvent) {
		LOG.info("Reading configuration")
		val config = Configuration(e.suggestedConfigurationFile)
		Modules.forEach {
			it.readConfig(config)
		}
		config.save()
		LOG.info("Enabled modules (${EnabledModules.size}): [${EnabledModules.joinToString(", ")}]")
	}

	fun fmlInit(e: FMLInitializationEvent) {
	}

	fun fmlPostInit(e: FMLPostInitializationEvent) {
		EnabledModules.forEach {
			it.registerGregTechItems()
		}
		EnabledModules.forEach {
			it.registerRecipes()
		}
	}

	fun fmlComplete(e: FMLLoadCompleteEvent) {
		EnabledModules.forEach {
			it.loadRecipesOnComplete()
		}
	}
}
