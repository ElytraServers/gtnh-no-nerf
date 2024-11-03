package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.NNItemList;
import cn.taskeren.gtnn.common.config.Config;
import cn.taskeren.gtnn.machine.MTELargeProcessingFactory;

public class MachineRegistry {

	public static void registerMachine() {
		NNItemList.Machine_LargeProcessingFactory.set(
			new MTELargeProcessingFactory(
				Config.getMachineId("large-processing-factory", 860, true),
				"industrialmultimachine.controller.tier.single",
				"Large Processing Factory"
			).getStackForm(1)
		);
	}

}
