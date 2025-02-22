package cn.elytra.mod.gtnn.modules.simple.module.no_default_server_list

import cn.elytra.mod.gtnn.modules.simple.IModule
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.multiplayer.ServerData
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object ModAntiDefaultServerList : IModule {

	override val enabled: Boolean = FMLCommonHandler.instance().side == Side.CLIENT

	override fun readConfig(configuration: Configuration) {
	}

	private val LOG = LogManager.getLogger()
	private val ThatList by lazy {
		MyAlwaysEmptyAndTellYouMamaWhenElementIsAddedList<ServerData> {
			LOG.info("You are not my Elytra server! ${it.serverName}(${it.serverIP})")
		}
	}

	override fun fmlPreInit(e: FMLPreInitializationEvent) {
		cheatDefaultServerList()
	}

	private fun cheatDefaultServerList() {
		try {
			val dslConfigClass = Class.forName("glowredman.defaultserverlist.Config")
			val dslServersField = dslConfigClass.getDeclaredField("SERVERS")

			// it should be accessible, because it is public
			@Suppress("DEPRECATION") // only deprecated in J9+
			if(!dslServersField.isAccessible) dslServersField.isAccessible = true

			// get access to Field.modifiers to remove "final" modifier on "Config.SERVERS"
			val fieldModifiersField = Field::class.java.getDeclaredField("modifiers")
			@Suppress("DEPRECATION") // only deprecated in J9+
			if(!fieldModifiersField.isAccessible) fieldModifiersField.isAccessible = true
			// remove the "final"
			fieldModifiersField.setInt(dslServersField, dslServersField.modifiers and Modifier.FINAL.inv())

			LOG.info("Replacing the server list, hush!")
			dslServersField.set(null, ThatList)
			LOG.info("Completed, Default Server List will no longer be able to add its server, never!")
		} catch(_: ClassNotFoundException) {
			LOG.warn("Unable to find Default Server List, is that mf installed?")
		} catch(e: Exception) {
			LOG.error("Unable to fuck up Default Server List", e)
		}
	}

	private class MyAlwaysEmptyAndTellYouMamaWhenElementIsAddedList<T>(val callback: ((T) -> Unit)? = null) :
		ArrayList<T>(0) {
		override fun add(index: Int, element: T) {
			callback?.invoke(element)
		}

		override fun add(e: T): Boolean {
			callback?.invoke(e)
			return false
		}

		override fun addAll(c: Collection<T>): Boolean {
			callback?.let { c.forEach(it) }
			return false
		}

		override fun addAll(index: Int, c: Collection<T>): Boolean {
			callback?.let { c.forEach(it) }
			return false
		}
	}

}
