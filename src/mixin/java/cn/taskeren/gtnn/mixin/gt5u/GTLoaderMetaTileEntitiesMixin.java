package cn.taskeren.gtnn.mixin.gt5u;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.NNItemList;
import cn.taskeren.gtnn.machine.MTEDisassembler;
import gregtech.loaders.preload.LoaderMetaTileEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LoaderMetaTileEntities.class, remap = false)
public class GTLoaderMetaTileEntitiesMixin {

	@Inject(method = "run", at = @At("RETURN"))
	private void nn$run2(CallbackInfo ci) {
		GTNN.logger.info("Registering disassemblers after gt5u");
		NNItemList.Machine_LV_Disassembler.set(
			new MTEDisassembler(
				451,
				"basicmachine.disassembler.tier.01",
				"Basic Disassembler",
				1).getStackForm(1L));
		NNItemList.Machine_MV_Disassembler.set(
			new MTEDisassembler(
				452,
				"basicmachine.disassembler.tier.02",
				"Advanced Disassembler",
				2).getStackForm(1L));
		NNItemList.Machine_HV_Disassembler.set(
			new MTEDisassembler(
				453,
				"basicmachine.disassembler.tier.03",
				"Advanced Disassembler II",
				3).getStackForm(1L));
		NNItemList.Machine_EV_Disassembler.set(
			new MTEDisassembler(
				454,
				"basicmachine.disassembler.tier.04",
				"Advanced Disassembler III",
				4).getStackForm(1L));
		NNItemList.Machine_IV_Disassembler.set(
			new MTEDisassembler(
				455,
				"basicmachine.disassembler.tier.05",
				"Advanced Disassembler IV",
				5).getStackForm(1L));
	}

}
