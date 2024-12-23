package cn.elytra.mod.gtnn

import cn.elytra.mod.gtnn.common.GtnnCommand
import cn.elytra.mod.gtnn.rewind.ModularLoader
import cpw.mods.fml.common.event.*
import net.minecraft.launchwrapper.Launch
import net.minecraft.util.EnumChatFormatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object GtnhNoNerf {
	val LOG: Logger = LogManager.getLogger()

	object Tooltips {
		val ModName = "${EnumChatFormatting.GOLD}GTNH-NO-NERF"
		val ForDeprecatedMachines = "${EnumChatFormatting.GREEN}Not Deprecated Anymore; brought back by ${ModName}!"
	}

	object Init {
		@JvmStatic
		fun fmlPreInit(e: FMLPreInitializationEvent) {
			ModularLoader.fmlPreInit(e)

			if(isDevelopment) {
				LOG.info("Development environment detected!")
			}
		}

		@JvmStatic
		fun fmlInit(e: FMLInitializationEvent) {
			ModularLoader.fmlInit(e)
		}

		@JvmStatic
		fun fmlPostInit(e: FMLPostInitializationEvent) {
			ModularLoader.fmlPostInit(e)
		}

		@JvmStatic
		fun fmlComplete(e: FMLLoadCompleteEvent) {
			ModularLoader.fmlComplete(e)
		}

		@JvmStatic
		fun fmlServerStarting(e: FMLServerStartingEvent) {
			e.registerServerCommand(GtnnCommand)
		}
	}

	val isDevelopment get() = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
}
