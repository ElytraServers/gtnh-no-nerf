package cn.taskeren.gtnn.mixin.newhorizonscore;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.NNItemList;
import cn.taskeren.gtnn.machine.MTEDisassembler;
import com.dreammaster.gthandler.GT_Loader_Machines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GT_Loader_Machines.class, remap = false)
public class GTLoaderMachinesMixin {

	@Inject(method = "run", at = @At(value = "INVOKE", target = "Lcom/dreammaster/gthandler/GT_Loader_Machines;recipes()V", shift = At.Shift.BEFORE))
	private void nn$registerBeforeRecipe(CallbackInfo ci) {
		GTNN.logger.info("Register advanced disassemblers after gtnhcore");
		NNItemList.Machine_LuV_Disassembler.set(
			new MTEDisassembler(11160, "basicmachine.disassembler.tier.06", "Elite Disassembler", 6)
				.getStackForm(1L));
		NNItemList.Machine_ZPM_Disassembler.set(
			new MTEDisassembler(
				11161,
				"basicmachine.disassembler.tier.07",
				"Elite Disassembler II",
				7).getStackForm(1L));
		NNItemList.Machine_UV_Disassembler.set(
			new MTEDisassembler(
				11162,
				"basicmachine.disassembler.tier.08",
				"Ultimate Deconstructor",
				8).getStackForm(1L));
		NNItemList.Machine_UHV_Disassembler.set(
			new MTEDisassembler(11163, "basicmachine.disassembler.tier.09", "Epic Deconstructor", 9)
				.getStackForm(1L));
		NNItemList.Machine_UEV_Disassembler.set(
			new MTEDisassembler(
				11164,
				"basicmachine.disassembler.tier.10",
				"Epic Deconstructor II",
				10).getStackForm(1L));
		NNItemList.Machine_UIV_Disassembler.set(
			new MTEDisassembler(
				11165,
				"basicmachine.disassembler.tier.11",
				"Epic Deconstructor III",
				11).getStackForm(1L));
		NNItemList.Machine_UMV_Disassembler.set(
			new MTEDisassembler(
				11166,
				"basicmachine.disassembler.tier.12",
				"Epic Deconstructor IV",
				12).getStackForm(1L));
	}

}
