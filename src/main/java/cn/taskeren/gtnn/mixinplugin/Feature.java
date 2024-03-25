package cn.taskeren.gtnn.mixinplugin;

import java.util.Arrays;
import java.util.Collection;

public enum Feature {

	ReturnDisassembler(
		"Add disassemblers back to the game.",
		Mixin.GTLoaderMachinesMixin,
		Mixin.GTShapedRecipe,
		Mixin.GTShapelessRecipe
	),

	NoLightningRodDestruction(
		"Remove the destruction when generating EU.",
		Mixin.GTMetaTileEntityLightningRod
	),

	LargeEssentiaGeneratorLaserHatchCompat(
		"Make Large Essentia Generators laser hatches compatible.",
		Mixin.LargeEssentiaGeneratorMixin
	),

	LegacyEyeOfHarmony(
		"Replace new Eye of Harmony with legacy one.",
		Mixin.EyeOfHarmonyMixin
	),

	LegacySupercriticalTurbine(
		"Replace new Supercritical Steam Turbine with legacy one.",
		Mixin.XLPlasmaTurbineMixin,
		Mixin.XLSCTurbineMixin
	),
	;

	public final String desc;
	public final Mixin[] mixins;

	Feature(String desc, Mixin... mixins) {
		this.desc = desc;
		this.mixins = mixins;
	}

	public boolean isEnabled() {
		return MixinConfig.isFeatureEnabled(this);
	}

	public boolean isTargetedModsLoad(Collection<TargetedMod> loadedMods) {
		return Arrays.stream(mixins).allMatch(mixin -> mixin.shouldLoad(loadedMods));
	}

}
