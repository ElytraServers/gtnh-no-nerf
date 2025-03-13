package cn.elytra.mod.gtnn.mixin.tectech;

import gregtech.api.interfaces.IItemContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tectech.loader.thing.MachineLoader;

@Mixin(value = MachineLoader.class, remap = false)
public class NoHide_MachineLoader_Mixin {

	@Redirect(method = "run", at = @At(value = "INVOKE", target = "Lgregtech/api/interfaces/IItemContainer;hidden()Lgregtech/api/interfaces/IItemContainer;"))
	private IItemContainer nn$noHide(IItemContainer instance) {
		// remove hide() call
		return instance;
	}

}
