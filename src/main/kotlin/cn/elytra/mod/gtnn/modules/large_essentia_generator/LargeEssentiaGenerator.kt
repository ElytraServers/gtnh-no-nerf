package cn.elytra.mod.gtnn.modules.large_essentia_generator

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.registry.GameRegistry
import goodgenerator.blocks.regularBlock.BlockTEContainer
import goodgenerator.blocks.tileEntity.MTELargeEssentiaGenerator
import goodgenerator.items.GGItem
import goodgenerator.items.GGItemBlocks
import goodgenerator.main.GoodGenerator
import goodgenerator.util.ItemRefer
import gregtech.api.GregTechAPI
import gregtech.api.enums.ItemList
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.enums.TCAspects
import gregtech.api.util.GTOreDictUnificator
import ic2.core.Ic2Items
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import thaumcraft.common.config.ConfigBlocks

object LargeEssentiaGenerator : ModuleDefinitionBase("large_essentia_generator") {

	lateinit var upgradeEssentia: GGItem
	lateinit var largeEssentiaGenerator: ItemStack
	lateinit var essentiaHatch: Block

	val essentiaUpgradeEmpty: ItemStack get() = ItemStack(upgradeEssentia, 1, 0)
	val essentiaUpgradeAir: ItemStack get() = ItemStack(upgradeEssentia, 1, 1)
	val essentiaUpgradeThermal: ItemStack get() = ItemStack(upgradeEssentia, 1, 2)
	val essentiaUpgradeUnstable: ItemStack get() = ItemStack(upgradeEssentia, 1, 3)
	val essentiaUpgradeVictus: ItemStack get() = ItemStack(upgradeEssentia, 1, 4)
	val essentiaUpgradeTainted: ItemStack get() = ItemStack(upgradeEssentia, 1, 5)
	val essentiaUpgradeMechanics: ItemStack get() = ItemStack(upgradeEssentia, 1, 6)
	val essentiaUpgradeSpirit: ItemStack get() = ItemStack(upgradeEssentia, 1, 7)
	val essentiaUpgradeRadiation: ItemStack get() = ItemStack(upgradeEssentia, 1, 8)
	val essentiaUpgradeElectric: ItemStack get() = ItemStack(upgradeEssentia, 1, 9)

	override fun onFMLPreInit(e: FMLPreInitializationEvent) {
		upgradeEssentia = GGItem(
			"upgradeEssentia", GoodGenerator.GG,
			arrayOf(
				GoodGenerator.MOD_ID + ":upgradeEssentia/null", GoodGenerator.MOD_ID + ":upgradeEssentia/air",
				GoodGenerator.MOD_ID + ":upgradeEssentia/thermal", GoodGenerator.MOD_ID + ":upgradeEssentia/unstable",
				GoodGenerator.MOD_ID + ":upgradeEssentia/victus", GoodGenerator.MOD_ID + ":upgradeEssentia/tainted",
				GoodGenerator.MOD_ID + ":upgradeEssentia/mechanics", GoodGenerator.MOD_ID + ":upgradeEssentia/spirit",
				GoodGenerator.MOD_ID + ":upgradeEssentia/radiation", GoodGenerator.MOD_ID + ":upgradeEssentia/electric"
			)
		)
		GameRegistry.registerItem(upgradeEssentia, "upgradeEssentia", GoodGenerator.MOD_ID)

		essentiaHatch = BlockTEContainer("essentiaHatch", arrayOf(GoodGenerator.MOD_ID + ":essentiaHatch"), 1)
		GameRegistry.registerBlock(essentiaHatch, GGItemBlocks::class.java, GoodGenerator.MOD_ID + ":essentiaHatch")
	}

	override fun onFMLInit(e: FMLInitializationEvent) {
		largeEssentiaGenerator = MTELargeEssentiaGenerator(32002, "LargeEssentiaGenerator", "Large Essentia Generator")
			.getStackForm(1)
	}

	override fun onFMLPostInit(e: FMLPostInitializationEvent) {
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
			largeEssentiaGenerator.copy(),
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
