package cn.taskeren.gtnn.mixinplugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public enum Mixin {

	GTLoaderMetaTileEntities("gt5u.GTLoaderMetaTileEntitiesMixin", TargetedMod.GregTech),
	GTShapedRecipe("gt5u.GTShapedRecipeMixin", TargetedMod.GregTech),
	GTShapelessRecipe("gt5u.GTShapelessRecipeMixin", TargetedMod.GregTech),
	GTMetaTileEntityLightningRod("gt5u.GTMetaTileEntityLightningRodMixin", TargetedMod.GregTech),

	GTLoaderMachinesMixin("newhorizonscore.GTLoaderMachinesMixin", TargetedMod.NewHorizonsCoreMod),

	LargeEssentiaGeneratorMixin("goodgenerator.LargeEssentiaGeneratorMixin", TargetedMod.GoodGenerator),

	EyeOfHarmonyMixin("tectech.GTMetaTileEntityEOMMixin", TargetedMod.TecTech),
	XLPlasmaTurbineMixin("gtpp.GT_MTE_LargeTurbine_PlasmaMixin", TargetedMod.GTPlusPlus),
	XLSCTurbineMixin("gtpp.GT_MTE_LargeTurbine_SCSteamMixin", TargetedMod.GTPlusPlus),

	RocketFuelsMixin("gtpp.RocketFuelsMixin", TargetedMod.GTPlusPlus),

	TecTechMainLoaderFixMixin("tectech.MainLoaderMixin", TargetedMod.TecTech),
	;

	public final String mixinClass;
	public final List<TargetedMod> targetedMods;

	Mixin(String mixinClass, TargetedMod... targetedMods) {
		this.mixinClass = mixinClass;
		this.targetedMods = Arrays.asList(targetedMods);
	}

	public boolean shouldLoad(Collection<TargetedMod> loadedMods) {
		return loadedMods.containsAll(this.targetedMods);
	}

}
