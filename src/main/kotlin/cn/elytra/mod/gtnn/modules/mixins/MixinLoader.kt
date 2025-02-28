package cn.elytra.mod.gtnn.modules.mixins

import cpw.mods.fml.common.Loader
import cpw.mods.fml.common.ModContainer
import cpw.mods.fml.common.versioning.ComparableVersion
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

object MixinLoader {

	val logger: Logger = LogManager.getLogger("GTNN-Mixin-Module-Loader")

	private val MixinModules = mutableListOf<IMixinModule>()

	init {
		MixinModules += MixinModuleBuilder("unbreakable-lightning-rod") {
			addMixin("gt5u.UnbreakableLightningRod_MTELightningRod_Mixin")
			dependsOn(TargetMod.GregTech)
			enabledByDefault()
			setConfigCategoryDesc("Disasbles the Iron Fence eating on Lightning Rod generating EU.")
		}
		MixinModules += MixinModuleBuilder("disassembler-recipe-getter") {
			var forceLoadingShapeless = false

			onConfigLoad { config, category ->
				forceLoadingShapeless = config.getBoolean(
					"force-loading-shapeless-recipes", category, forceLoadingShapeless,
					"""
						Set to 'true' to load shapeless recipes ignoring incompatible GregTech version.
						This can only cause inection failed or inappropriate behaviors.

						You can try to enable this with version equal to or later than '5.09.51.63', because the only
						reported incorrectly added disassembling recipe is being removed before the commit of this tag.
					""".trimIndent()
				)
			}

			addMixin("gt5u.DisassemblerReversedRecipe_GTShapedRecipe_Mixin")
			addMixinDynamic {
				// https://github.com/GTNewHorizons/GT5-Unofficial/commit/07cc2ec931b0e479026e78298a7bd926019c9334
				// the commit above changed the argument of GTShapelessRecipe and removed the "aDismantleable"
				// causing the injection mistakenly treat the "aRemoveable" as "aDismantleable"
				// we should not load any shapless recipes until we have a fix to this problem
				val gtVersion = ComparableVersion(getModContainer("gregtech")!!.metadata.version)
				val tagVersionChangedTheBehavior = ComparableVersion("5.09.49.93") // must be lower than this version
				logger.info("Loaded GregTech version: $gtVersion")
				if(forceLoadingShapeless || gtVersion < tagVersionChangedTheBehavior) {
					add("gt5u.DisassemblerReversedRecipe_GTShapelessRecipe_Mixin")
				} else {
					logger.info("Skipped gt5u.DisassemblerReversedRecipe_GTShapelessRecipe_Mixin due to version incompatibility")
				}
			}
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

	val loadedMixinModules = mutableListOf<IMixinModule>()

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
						logger.info("Applying Mixins from ${mixinModule.id}")
						mixinModule.mixins.forEach { mixin ->
							add(mixin)
							logger.info("- $mixin")
						}
						loadedMixinModules += mixinModule
					} else {
						logger.info("Skipped Mixin module $mixinModule because requirements not met")
					}
				}
			}
		}
	}

	internal fun getModContainer(modId: String): ModContainer? {
		return Loader.instance().modList.firstOrNull { it.modId == modId }
	}
}
