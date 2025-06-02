package cn.elytra.mod.gtnn

import cn.elytra.mod.gtnn.client.NNTextures
import cn.elytra.mod.gtnn.common.GtnnCommand
import cn.elytra.mod.gtnn.mod_v2.ModuleManager
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.*
import cpw.mods.fml.relauncher.Side
import net.minecraft.launchwrapper.Launch

open class CommonLoader {

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

	fun construct(e: FMLConstructionEvent) {
	}

	fun preInit(event: FMLPreInitializationEvent) {
		if(dev) {
			GTNN.logger.info("Deobfuscated environment detected!")
		}

		ModuleManager.onFMLPreInit(event)

		// to make sure it is loaded, so that the icon registration callbacks are added to the GT api
		NNTextures.GT.entries
		NNTextures.GTPlusPlus.entries
	}

	fun init(event: FMLInitializationEvent) {
		ModuleManager.onFMLInit(event)
	}

	fun postInit(event: FMLPostInitializationEvent) {
		ModuleManager.onFMLPostInit(event)
	}

	fun complete(event: FMLLoadCompleteEvent) {
		ModuleManager.onFMLLoadComplete(event)
	}

	fun serverStarting(event: FMLServerStartingEvent) {
		event.registerServerCommand(GtnnCommand)
		ModuleManager.onFMLServerStarting(event)
	}

	fun serverStarted(event: FMLServerStartedEvent) {
		ModuleManager.onFMLServerStarted(event)
	}

	fun serverStopping(event: FMLServerStoppingEvent) {
		ModuleManager.onFMLServerStopping(event)
	}

	fun serverStopped(event: FMLServerStoppedEvent) {
		ModuleManager.onFMLServerStopped(event)
	}
}
