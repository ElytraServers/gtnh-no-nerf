package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.machine.recipe.DisassemblerRecipes;
import cn.taskeren.gtnn.machine.recipe.ReverseShapedRecipe;
import cn.taskeren.gtnn.machine.recipe.ReverseShapelessRecipe;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void onPreInit(FMLPreInitializationEvent event) {
	}

	public void onInit(FMLInitializationEvent event) {
	}

	public void onPostInit(FMLPostInitializationEvent event) {
		MachineRegistry.registerMachine();

		ReverseShapedRecipe.runReverseRecipes();
		ReverseShapelessRecipe.runReverseRecipes();
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		DisassemblerRecipes.loadAssemblerRecipes();
	}
}
