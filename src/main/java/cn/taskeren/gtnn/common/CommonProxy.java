package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.mod.gt5u.recipe.ReverseShapedRecipe;
import cn.taskeren.gtnn.mod.gt5u.recipe.ReverseShapelessRecipe;
import cn.taskeren.gtnn.mod.gt5u.util.DisassemblerRecipes;
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
		ReverseShapedRecipe.runReverseRecipes();
		ReverseShapelessRecipe.runReverseRecipes();
	}

	public void onLoadComplete(FMLLoadCompleteEvent event) {
		DisassemblerRecipes.loadAssemblerRecipes();
	}
}
