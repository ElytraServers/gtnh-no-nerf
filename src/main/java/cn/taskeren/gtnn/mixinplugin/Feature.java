package cn.taskeren.gtnn.mixinplugin;

import java.util.Arrays;
import java.util.Collection;

public enum Feature {

	ReturnDisassembler(
		"Add disassemblers back to the game.",
		Mixin.GTLoaderMetaTileEntities,
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

	RevertRocketFuelsRecipes(
		"Revert Rocket Fuels Recipes",
		Mixin.RocketFuelsMixin
	),

	MainLoaderProgressBarFix(
		"Fix the progress bar bug of TecTech",
		Mixin.TecTechMainLoaderFixMixin
	),

	InfinityCapacityMEOutputBusAndHatch(
		"Overwrite the capacity of the Output Bus ME and Output Hatch ME to infinity",
		Mixin.InfinityOutputBusMixin,
		Mixin.InfinityOutputHatchMixin
	)
	;

	public final String desc;
	public final Mixin[] mixins;

	public final boolean defaultEnabled;

	Feature(String desc, Mixin... mixins) {
		this(desc, true, mixins);
	}

	Feature(String desc, boolean defaultEnabled, Mixin... mixins) {
		this.desc = desc;
		this.defaultEnabled = defaultEnabled;
		this.mixins = mixins;
	}

	public boolean isEnabled() {
		return MixinConfig.isFeatureEnabled(this);
	}

	public boolean isDefaultEnabled() {
		return defaultEnabled;
	}

	public boolean isTargetedModsLoad(Collection<TargetedMod> loadedMods) {
		return Arrays.stream(mixins).allMatch(mixin -> mixin.shouldLoad(loadedMods));
	}

}
