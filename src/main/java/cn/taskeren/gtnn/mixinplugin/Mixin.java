package cn.taskeren.gtnn.mixinplugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public enum Mixin {

	GTLoaderMetaTileEntities("gt5u.GTLoaderMetaTileEntitiesMixin", TargetedMod.GregTech),
	GTShapedRecipe("gt5u.GTShapedRecipeMixin", TargetedMod.GregTech),
	GTShapelessRecipe("gt5u.GTShapelessRecipeMixin", TargetedMod.GregTech),
	GTMetaTileEntityLightningRod("gt5u.GTMetaTileEntityLightningRodMixin", TargetedMod.GregTech),

	GTLoaderMachinesMixin("newhorizonscore.GTLoaderMachinesMixin", TargetedMod.NewHorizonsCoreMod),

	LargeEssentiaGeneratorMixin("goodgenerator.LargeEssentiaGeneratorMixin", TargetedMod.GoodGenerator),

	EyeOfHarmonyMixin("tectech.GTMetaTileEntityEOMMixin", TargetedMod.TecTech),

	TecTechMainLoaderFixMixin("tectech.MainLoaderMixin", TargetedMod.TecTech),

	AddCatalyst_ItemUtils("gtpp.AddCatalyst_ItemUtils_Mixin", TargetedMod.GTPlusPlus),
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
