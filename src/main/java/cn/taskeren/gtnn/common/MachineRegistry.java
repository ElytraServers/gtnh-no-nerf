package cn.taskeren.gtnn.common;

import cn.taskeren.gtnn.NNItemList;
import cn.taskeren.gtnn.common.config.Config;
import cn.taskeren.gtnn.machine.MTELargeProcessingFactory;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class MachineRegistry {

	public static void registerMachines() {
		NNItemList.Machine_LargeProcessingFactory.set(
			new MTELargeProcessingFactory(
				Config.getMachineId("large-processing-factory", 10680, true),
				"industrialmultimachine.controller.tier.single",
				"Large Processing Factory"
			).getStackForm(1)
		);

		GTModHandler.addShapelessCraftingRecipe(NNItemList.Machine_LargeProcessingFactory.get(1),
			GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.BUFFERED
				| GTModHandler.RecipeBits.NOT_REMOVABLE
				| GTModHandler.RecipeBits.REVERSIBLE,
			new Object[]{GregtechItemList.Industrial_MultiMachine.get(1)});
	}

}
