package cn.taskeren.gtnn.mixin.gt5u;

import cn.taskeren.gtnn.GTNN;
import cn.taskeren.gtnn.util.ToStringHelper;
import gregtech.common.tileentities.generators.MTELightningRod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MTELightningRod.class, remap = false)
public class UnbreakableLightningRod_MTELightningRod_Mixin {

	@Redirect(method = "onPostTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockToAir(III)Z"), remap = true)
	private boolean nn$onWorldSetBlockToAir(World world, int x, int y, int z) {
		GTNN.logger.debug("Prevented a Lightning Rod breaking at " + ToStringHelper.vecToString(x, y, z));
		return true;
	}

}
