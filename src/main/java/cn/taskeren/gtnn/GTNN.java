package cn.taskeren.gtnn;

import cn.taskeren.gtnn.common.CommonProxy;
import cn.taskeren.gtnn.common.command.NoNerfCommand;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GTNN.MOD_ID, name = GTNN.MOD_NAME, dependencies = GTNN.DEPENDENCIES)
public class GTNN {

	public static Logger logger = LogManager.getLogger("GTNN");

	static final String MOD_ID = "gtnn";
	static final String MOD_NAME = "GT-NO-NERF";

	@SuppressWarnings("SpellCheckingInspection")
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

	public static final String MOD_NAME_TOOLTIP = EnumChatFormatting.GOLD + "GTNH-NO-NERF";

	public static final String NOT_DEPRECATED_TOOLTIP = EnumChatFormatting.GREEN + "Not Deprecated! Brought back by " + MOD_NAME_TOOLTIP + EnumChatFormatting.GREEN + "!";

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
