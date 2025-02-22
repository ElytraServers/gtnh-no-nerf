package cn.elytra.mod.gtnn.modules.mixins

import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object MixinLoader {

	val logger: Logger = LogManager.getLogger("GTNN-MixinModule-Loader")

	private val MixinModules = mutableListOf<IMixinModule>()

	init {
		MixinModules += MixinModuleBuilder("unbreakable-lightning-rod") {
			addMixin("gt5u.UnbreakableLightningRod_MTELightningRod_Mixin")
			dependsOn(TargetMod.GregTech)
			enabledByDefault()
			setConfigCategoryDesc("Disasbles the Iron Fence eating on Lightning Rod generating EU.")
		}
		MixinModules += MixinModuleBuilder("disassembler-recipe-getter") {
			addMixin("gt5u.DisassemblerReversedRecipe_GTShapedRecipe_Mixin")
			addMixin("gt5u.DisassemblerReversedRecipe_GTShapelessRecipe_Mixin")
			dependsOn(TargetMod.GregTech)
			mustBeEnabled()
			setConfigCategoryDesc("To get all the GregTech-source recipes for the Disassembler recipe map.")
		}
		MixinModules += MixinModuleBuilder("large-essentia-generator-allow-laser") {
			addMixin("goodgenerator.LEGAllowLaser_MTELargeEssentiaGenerator_Mixin")
			dependsOn(TargetMod.GoodGenerator)
			enabledByDefault()
			setConfigCategoryDesc("Disables the Laser Hatch check in Large Essentia Generators.")
		}
		MixinModules += MixinModuleBuilder("catalyst-adder") {
			addMixin("gtpp.AddCatalyst_ItemUtils_Mixin")
			dependsOn(TargetMod.GTPlusPlus)
			mustBeEnabled()
			setConfigCategoryDesc("To allow us to add new (removed) Catalysts to GT++.")
		}
		MixinModules += MixinModuleBuilder("infinity-me-bus") {
			addMixin("gt5u.InfinityMEBus_MTEHatchOutputME_Mixin")
			addMixin("gt5u.InfinityMEBus_MTEHatchOutputBusME_Mixin")
			dependsOn(TargetMod.GregTech)
			enabledByDefault()
			setConfigCategoryDesc("Sets the ME Output Bus and Hatches to maximum capacity.")
		}
		MixinModules += MixinModuleBuilder("aal-subtick") {
			addMixin("ggfab.AALSubTick_MTEAdvAssLine_Mixin")
			dependsOn(TargetMod.GGFab)
			enabledByDefault()
		}
	}

	private val _loadedMixinModules = mutableListOf<IMixinModule>()

	val loadedMixinModules: List<String> get() = _loadedMixinModules.map { it::class.java.simpleName }

	fun readConfig() {
		val config = Configuration(File(Launch.minecraftHome, "config/gtnn-mixin.cfg"))
		MixinModules.forEach {
			it.readConfig(config)
		}
		config.save()
	}

	fun getMixins(loadedMods: Set<String>): List<String> {
		// set up the load states of the targeted mods
		TargetMod.entries.forEach { it.init(loadedMods) }
		// iterate all mixin modules to get the mixin classes to apply
		return buildList<String> {
			MixinModules.forEach { mixinModule ->
				if(mixinModule.isEnabled()) {
					if(mixinModule.canApplyMixins()) {
						logger.info("Applying Mixins from $mixinModule")
						mixinModule.mixins.forEach { mixin ->
							add(mixin)
							logger.info("- $mixin")
						}
						_loadedMixinModules += mixinModule
					} else {
						logger.info("Skipped Mixin module $mixinModule because requirements not met")
					}
				}
			}
		}
	}
}
