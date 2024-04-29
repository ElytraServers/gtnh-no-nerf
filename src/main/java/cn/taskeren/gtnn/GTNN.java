package cn.taskeren.gtnn;

import cn.taskeren.gtnn.common.CommonProxy;
import cn.taskeren.gtnn.common.command.NoNerfCommand;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GTNN.MOD_ID, name = GTNN.MOD_NAME, dependencies = GTNN.DEPENDENCIES)
public class GTNN {

	public static Logger logger = LogManager.getFormatterLogger("GTNN");

	static final String MOD_ID = "gtnn";
	static final String MOD_NAME = "GT-NO-NERF";

	static final String DEPENDENCIES
		= "required-after:gregtech;"
		+ "required-after:miscutils;" // gt++
		+ "required-after:GoodGenerator;"
		+ "required-after:dreamcraft;" // new-horizons-core
		+ "required-after:tectech;"
		+ "required-after:gtnhlib;"
		+ "required-after:bartworks;"
		;

	@SidedProxy(clientSide = "cn.taskeren.gtnn.client.ClientProxy", serverSide = "cn.taskeren.gtnn.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance
	public static GTNN instance;

	public GTNN() {
	}

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {
		proxy.onPreInit(event);
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event) {
		proxy.onInit(event);
	}

	@Mod.EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		proxy.onPostInit(event);
	}

	@Mod.EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		proxy.onLoadComplete(event);
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new NoNerfCommand());
	}

}
