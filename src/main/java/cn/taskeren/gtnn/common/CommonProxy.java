package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTechAPI;

public class CommonProxy {

	public void onPreInit(FMLPreInitializationEvent event) {
	}

	public void onInit(FMLInitializationEvent event) {
	}

	public void onPostInit(FMLPostInitializationEvent event) {
		MachineRegistry.registerMachines();

		ReversedRecipeRegistry.registerRecipesToDisassemblers();
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		DisassemblerRecipes.loadAssemblerRecipes();

		logAvailableMachineIds();
	}

	private static void logAvailableMachineIds() {
		GTNN.logger.info("Available MTE IDs:");

		var startId = 0;
		for (int id = 0; id < GregTechAPI.METATILEENTITIES.length; id++) {
			if(GregTechAPI.METATILEENTITIES[id] != null) {
				if(id - 1 == startId) {
					GTNN.logger.info("{}", id);
				} else {
					GTNN.logger.info("{}-{}", startId, id);
					startId = -1;
				}
			} else if(startId == -1) {
				startId = id;
			}
		}
	}
}
