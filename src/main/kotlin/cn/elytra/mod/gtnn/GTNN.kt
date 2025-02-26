package cn.elytra.mod.gtnn

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.SidedProxy
import cpw.mods.fml.common.event.*
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
	modid = GTNN.MOD_ID,
	name = GTNN.MOD_NAME,
	version = Tags.VERSION,
	dependencies = GTNN.DEPS,
	modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter"
)
object GTNN {

	@JvmField
	val logger: Logger = LogManager.getLogger("GTNN")

	const val MOD_ID = "gtnn"
	const val MOD_NAME = "GTNN"

	@Suppress("SpellCheckingInspection")
	const val DEPS = "required-after:forgelin;" +
		"required-after:gregtech;" +
		"required-after:miscutils;" +
		"required-after:GoodGenerator;" +
		"required-after:dreamcraft;" +
		"required-after:tectech;" +
		"required-after:gtnhlib;" +
		"required-after:bartworks;"

	@SidedProxy(
		serverSide = "cn.elytra.mod.gtnn.CommonLoader",
		clientSide = "cn.elytra.mod.gtnn.ClientLoader",
	)
	lateinit var proxy: CommonLoader

	@EventHandler
	fun preInit(e: FMLPreInitializationEvent) = proxy.preInit(e)

	@EventHandler
	fun init(e: FMLInitializationEvent) = proxy.init(e)

	@EventHandler
	fun postInit(e: FMLPostInitializationEvent) = proxy.postInit(e)

	@EventHandler
	fun complete(e: FMLLoadCompleteEvent) = proxy.complete(e)

	@EventHandler
	fun serverStarting(e: FMLServerStartingEvent) = proxy.serverStarting(e)

	@EventHandler
	fun serverStarted(e: FMLServerStartedEvent) = proxy.serverStarted(e)

}
