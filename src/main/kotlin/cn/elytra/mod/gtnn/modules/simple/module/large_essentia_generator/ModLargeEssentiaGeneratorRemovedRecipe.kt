package cn.elytra.mod.gtnn.modules.simple.module.large_essentia_generator

import cn.elytra.mod.gtnn.modules.simple.IModule
import goodgenerator.util.ItemRefer
import gregtech.api.GregTechAPI
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.TCAspects
import gregtech.api.util.GTOreDictUnificator
import ic2.core.Ic2Items
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration
import thaumcraft.common.config.ConfigBlocks

object ModLargeEssentiaGeneratorRemovedRecipe : IModule {

	override var enabled: Boolean = true

	override fun readConfig(configuration: Configuration) {
		enabled = configuration.getBoolean("add-removed-recipe", "large-essentia-generator", enabled, "add LEG recipe back")
	}

	override fun registerRecipes() {
		GregTechAPI.sThaumcraftCompat.addInfusionRecipe(
			"ESSENTIA_GENERATOR",
			ItemList.Hull_HV.get(1),
			arrayOf(
				GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 1L),
				ItemStack(ConfigBlocks.blockJar, 1),
				GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Thaumium, 1L),
				GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L),
				ItemStack(ConfigBlocks.blockWoodenDevice, 1),
				GTOreDictUnificator.get(OrePrefixes.spring, Materials.Manyullyn, 1L), Ic2Items.teslaCoil,
				ItemList.Sensor_MV.get(1)
			),
			ItemRefer.Large_Essentia_Generator.get(1),
			6,
			listOf(
				TCAspects.TC_AspectStack(TCAspects.POTENTIA, 64L),
				TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 64L),
				TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 32),
				TCAspects.TC_AspectStack(TCAspects.PERMUTATIO, 32),
			)
		)
	}

}
