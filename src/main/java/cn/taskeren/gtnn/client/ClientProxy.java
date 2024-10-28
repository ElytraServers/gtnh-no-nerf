package cn.taskeren.gtnn.client;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.client.no_default_server.NoDefaultServer;
import cn.taskeren.gtnn.common.CommonProxy;
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

		// the list is loaded in the pre-init stage in the DefaultServerList mod,
		// so we clear it at post-init stage, it should not be filled again
		try {
			if(NoDefaultServer.isDefaultServerListInstalled()) {
				NoDefaultServer.clearDefaultServerList();
			}
		} catch(Exception e) {
			GTNN.logger.error("Failed to remove Default Server List!", e);
		}
	}
}
