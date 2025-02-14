package cn.taskeren.gtnn.common;

import cn.elytra.mod.gtnn.GtnhNoNerf;
import cpw.mods.fml.common.event.*;

public class CommonProxy {

	public void onPreInit(FMLPreInitializationEvent event) {
		GtnhNoNerf.Init.fmlPreInit(event);
	}

	public void onInit(FMLInitializationEvent event) {
		GtnhNoNerf.Init.fmlInit(event);
	}

	public void onPostInit(FMLPostInitializationEvent event) {
		GtnhNoNerf.Init.fmlPostInit(event);
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		GtnhNoNerf.Init.fmlComplete(event);
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		GtnhNoNerf.Init.fmlServerStarting(event);
	}

}
