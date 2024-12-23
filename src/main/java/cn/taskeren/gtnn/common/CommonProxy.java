package cn.taskeren.gtnn.common;

import cn.elytra.mod.gtnn.GtnhNoNerf;
import cn.taskeren.gtnn.GTNN;
import cpw.mods.fml.common.event.*;
import gregtech.api.GregTechAPI;

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

		logAvailableMachineIds();
	}

	public void onServerStarting(FMLServerStartingEvent event) {
		GtnhNoNerf.Init.fmlServerStarting(event);
	}

	private static void logAvailableMachineIds() {
		GTNN.logger.info("Available MTE IDs:");

		var startId = 0;
		for (int id = 0; id < GregTechAPI.METATILEENTITIES.length; id++) {
			if (GregTechAPI.METATILEENTITIES[id] != null) {
				if (id - 1 == startId) {
					GTNN.logger.info("{}", id);
				} else {
					GTNN.logger.info("{}-{}", startId, id);
					startId = -1;
				}
			} else if (startId == -1) {
				startId = id;
			}
		}
	}
}
