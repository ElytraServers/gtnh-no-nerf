package cn.taskeren.gtnn.client;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.client.rawinput.RawInput;
import cn.taskeren.gtnn.common.CommonProxy;
import cn.taskeren.gtnn.common.config.Config;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		super.onPreInit(event);
	}

	@Override
	public void onInit(FMLInitializationEvent event) {
		super.onInit(event);
	}

	@Override
	public void onPostInit(FMLPostInitializationEvent event) {
		super.onPostInit(event);

		if(RawInput.isSuccessInit() && Config.useRawMouseMotion) {
			if(RawInput.isRawMouseMotionSupported()) {
				RawInput.setRawMouseMotion(true);
			} else {
				GTNN.logger.warn("Your device doesn't support Raw Mouse Motion, reported by OpenGL.");
			}
		}
	}
}
