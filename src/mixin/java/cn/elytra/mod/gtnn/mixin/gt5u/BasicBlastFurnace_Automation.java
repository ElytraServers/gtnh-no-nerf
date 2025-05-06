package cn.elytra.mod.gtnn.mixin.gt5u;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTEBrickedBlastFurnace;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MTEBrickedBlastFurnace.class, remap = false)
public abstract class BasicBlastFurnace_Automation extends MetaTileEntity {

	public BasicBlastFurnace_Automation(int aID, String aBasicName, String aRegionalName, int aInvSlotCount) {
		super(aID, aBasicName, aRegionalName, aInvSlotCount);
	}

	@Shadow
	@Final
	public static int INPUT_SLOTS;

	@Inject(method = "allowPullStack", at = @At("HEAD"), cancellable = true)
	private void gtnn$pull(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side, ItemStack aStack, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(aIndex > INPUT_SLOTS);
	}

	@Inject(method = "allowPutStack", at = @At("HEAD"), cancellable = true)
	private void gtnn$put(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side, ItemStack aStack, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(aIndex < INPUT_SLOTS && (mInventory[aIndex] == null || GTUtility.areStacksEqual(aStack, mInventory[aIndex])));
	}

}
