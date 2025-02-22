package cn.elytra.mod.gtnn

import cn.elytra.mod.gtnn.common.GtnnCommand
import cn.elytra.mod.gtnn.modules.simple.ModularLoader
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.event.FMLServerStartedEvent
import cpw.mods.fml.common.event.FMLServerStartingEvent
import cpw.mods.fml.relauncher.Side
import net.minecraft.launchwrapper.Launch

sealed class CommonLoader {

	/**
	 * Whether the server or client environment this mod currently in.
	 *
	 * You should always use [net.minecraft.world.World.isRemote] for checking the world side.
	 */
	val side: Side get() = FMLCommonHandler.instance().side

	/**
	 * `true` if in a deobfuscated environment, which is usually the development environment.
	 */
	val dev: Boolean get() = Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean

	fun preInit(event: FMLPreInitializationEvent) {
		if(dev) {
			GTNN.logger.info("Deobfuscated environment detected!")
		}

		ModularLoader.fmlPreInit(event)
	}

	fun init(event: FMLInitializationEvent) {
		ModularLoader.fmlInit(event)
	}

	fun postInit(event: FMLPostInitializationEvent) {
		ModularLoader.fmlPostInit(event)
	}

	fun complete(event: FMLLoadCompleteEvent) {
		ModularLoader.fmlComplete(event)
	}

	fun serverStarting(event: FMLServerStartingEvent) {
		event.registerServerCommand(GtnnCommand)
	}

	fun serverStarted(event: FMLServerStartedEvent) {

	}
}
