package cn.elytra.mod.gtnn.modules.simple.module.lower_lasers

import cn.elytra.mod.gtnn.modules.simple.IModule
import gregtech.api.enums.*
import gregtech.api.recipe.RecipeMaps.assemblerRecipes
import gregtech.api.util.GTOreDictUnificator
import gregtech.api.util.GTRecipeBuilder.MINUTES
import gregtech.api.util.GTRecipeBuilder.SECONDS
import gregtech.api.util.GTUtility
import net.minecraftforge.common.config.Configuration
import tectech.thing.CustomItemList

object ModLowerLaserHatches : IModule {
	override var enabled: Boolean = true

	const val NEI_DESC = "Added by GTNN"

	override fun readConfig(configuration: Configuration) {
		configuration.getBoolean("enabled", "lower_lasers", enabled, "use lower laser energy/dynamo hatches")
	}

	override fun registerRecipes() {

		// 1024 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
				ItemList.Emitter_IV.get(2),
				ItemList.Electric_Pump_IV.get(2),
				GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
				GTUtility.getIntegratedCircuit(2)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel2_IV.get(1))
			.duration(1 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 4096 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
				ItemList.Emitter_IV.get(4),
				ItemList.Electric_Pump_IV.get(4),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
				GTUtility.getIntegratedCircuit(3)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel3_IV.get(1))
			.duration(3 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
				ItemList.Emitter_LuV.get(4),
				ItemList.Electric_Pump_LuV.get(4),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
				GTUtility.getIntegratedCircuit(3)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel3_LuV.get(1))
			.duration(3 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 16384 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Emitter_IV.get(8),
				ItemList.Electric_Pump_IV.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel4_IV.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Emitter_LuV.get(8),
				ItemList.Electric_Pump_LuV.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel4_LuV.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Emitter_ZPM.get(8),
				ItemList.Electric_Pump_ZPM.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel4_ZPM.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 65536 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Emitter_IV.get(16),
				ItemList.Electric_Pump_IV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel5_IV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Emitter_LuV.get(16),
				ItemList.Electric_Pump_LuV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel5_LuV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Emitter_ZPM.get(16),
				ItemList.Electric_Pump_ZPM.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel5_ZPM.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Emitter_UV.get(16),
				ItemList.Electric_Pump_UV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel5_UV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 262144 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Emitter_IV.get(32),
				ItemList.Electric_Pump_IV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel6_IV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Emitter_LuV.get(32),
				ItemList.Electric_Pump_LuV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel6_LuV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Emitter_ZPM.get(32),
				ItemList.Electric_Pump_ZPM.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel6_ZPM.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Emitter_UV.get(32),
				ItemList.Electric_Pump_UV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel6_UV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_MAX.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Emitter_UHV.get(32),
				ItemList.Electric_Pump_UHV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel6_UHV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(2000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 1048576 Dynamo
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_IV.get(64),
				ItemList.Electric_Pump_IV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_IV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_LuV.get(64),
				ItemList.Electric_Pump_LuV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_LuV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_ZPM.get(64),
				ItemList.Electric_Pump_ZPM.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_ZPM.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_UV.get(64),
				ItemList.Electric_Pump_UV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_UV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_MAX.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_UHV.get(64),
				ItemList.Electric_Pump_UHV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_UHV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(2000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UEV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Emitter_UEV.get(64),
				ItemList.Electric_Pump_UEV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_dynamoTunnel7_UEV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(8000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 1024 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2),
				ItemList.Sensor_IV.get(2),
				ItemList.Electric_Pump_IV.get(2),
				GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
				GTUtility.getIntegratedCircuit(2)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel2_IV.get(1))
			.duration(1 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 4096 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
				ItemList.Sensor_IV.get(4),
				ItemList.Electric_Pump_IV.get(4),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
				GTUtility.getIntegratedCircuit(3)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel3_IV.get(1))
			.duration(3 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4),
				ItemList.Sensor_LuV.get(4),
				ItemList.Electric_Pump_LuV.get(4),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
				GTUtility.getIntegratedCircuit(3)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel3_LuV.get(1))
			.duration(3 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 16384 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Sensor_IV.get(8),
				ItemList.Electric_Pump_IV.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel4_IV.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Sensor_LuV.get(8),
				ItemList.Electric_Pump_LuV.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel4_LuV.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8),
				ItemList.Sensor_ZPM.get(8),
				ItemList.Electric_Pump_ZPM.get(8),
				GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
				GTUtility.getIntegratedCircuit(4)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel4_ZPM.get(1))
			.duration(6 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 65536 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Sensor_IV.get(16),
				ItemList.Electric_Pump_IV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel5_IV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Sensor_LuV.get(16),
				ItemList.Electric_Pump_LuV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel5_LuV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Sensor_ZPM.get(16),
				ItemList.Electric_Pump_ZPM.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel5_ZPM.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16),
				ItemList.Sensor_UV.get(16),
				ItemList.Electric_Pump_UV.get(16),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
				GTUtility.getIntegratedCircuit(5)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel5_UV.get(1))
			.duration(13 * MINUTES + 20 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 262144 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Sensor_IV.get(32),
				ItemList.Electric_Pump_IV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel6_IV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Sensor_LuV.get(32),
				ItemList.Electric_Pump_LuV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel6_LuV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Sensor_ZPM.get(32),
				ItemList.Electric_Pump_ZPM.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel6_ZPM.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Sensor_UV.get(32),
				ItemList.Electric_Pump_UV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel6_UV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_MAX.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32),
				ItemList.Sensor_UHV.get(32),
				ItemList.Electric_Pump_UHV.get(32),
				GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
				GTUtility.getIntegratedCircuit(6)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel6_UHV.get(1))
			.duration(26 * MINUTES + 40 * SECONDS)
			.eut(2000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)

		// 1048576 Target
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_IV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_IV.get(64),
				ItemList.Electric_Pump_IV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_IV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_IV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_LuV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_LuV.get(64),
				ItemList.Electric_Pump_LuV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_LuV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_LuV)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_ZPM.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_ZPM.get(64),
				ItemList.Electric_Pump_ZPM.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_ZPM.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(TierEU.RECIPE_ZPM)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_UV.get(64),
				ItemList.Electric_Pump_UV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_UV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(500000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_MAX.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_UHV.get(64),
				ItemList.Electric_Pump_UHV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_UHV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(2000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
		GTValues.RA.stdBuilder()
			.itemInputs(
				ItemList.Hull_UEV.get(1),
				GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
				ItemList.Sensor_UEV.get(64),
				ItemList.Electric_Pump_UEV.get(64),
				GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
				GTUtility.getIntegratedCircuit(7)
			)
			.itemOutputs(CustomItemList.eM_energyTunnel7_UEV.get(1))
			.duration(53 * MINUTES + 20 * SECONDS)
			.eut(8000000)
			.setNEIDesc(NEI_DESC)
			.addTo(assemblerRecipes)
	}
}
