package cn.elytra.mod.gtnn.rewind.module.large_processing_factory

import cn.elytra.mod.gtnn.GtnhNoNerf
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility.*
import gregtech.api.enums.HatchElement.*
import gregtech.api.enums.TAE
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.IIconContainer
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.RecipeMaps
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.CheckRecipeResultRegistry
import gregtech.api.util.GTLanguageManager
import gregtech.api.util.GTRecipe
import gregtech.api.util.GTStructureUtility.buildHatchAdder
import gregtech.api.util.GTUtility
import gregtech.api.util.GTUtility.filterValidMTEs
import gregtech.api.util.MultiblockTooltipBuilder
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME
import gregtech.common.tileentities.machines.MTEHatchInputME
import gtPlusPlus.core.block.ModBlocks
import gtPlusPlus.core.lib.GTPPCore
import gtPlusPlus.core.util.minecraft.PlayerUtils
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock
import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidStack
import java.util.stream.Stream

class MTELargeProcessingFactory : GTPPMultiBlockBase<MTELargeProcessingFactory>, ISurvivalConstructable {

	companion object {
		const val MACHINE_MODE_METAL = 0
		const val MACHINE_MODE_FLUID = 1
		const val MACHINE_MODE_MISC = 2

		const val MODE_COMPRESSOR = 0
		const val MODE_LATHE = 1
		const val MODE_MAGNETIC = 2
		const val MODE_FERMENTER = 3
		const val MODE_FLUID_EXTRACT = 4
		const val MODE_EXTRACTOR = 5
		const val MODE_LASER = 6
		const val MODE_AUTOCLAVE = 7
		const val MODE_FLUID_SOLIDIFY = 8

		val MODE_MAP = arrayOf(arrayOf(0, 1, 2), arrayOf(3, 4, 5), arrayOf(6, 7, 8))
		val TOOLTIP_MAP = arrayOf<String>()

		val TEXTURE_INDEX get() = TAE.getIndexFromPage(2, 2)

		init {
			for(i in 0..<9) {
				val recipeMap = getRecipeMap(i)
				if(recipeMap != null) {
					val nei = GTLanguageManager.getTranslation(recipeMap.unlocalizedName)
					TOOLTIP_MAP[i] = nei ?: "BAD NEI NAME (Report to Github)"
				}
			}
		}

		private fun getRecipeMap(aMode: Int): RecipeMap<*>? {
			return when(aMode) {
				MODE_COMPRESSOR -> RecipeMaps.compressorRecipes
				MODE_LATHE -> RecipeMaps.latheRecipes
				MODE_MAGNETIC -> RecipeMaps.polarizerRecipes
				MODE_FERMENTER -> RecipeMaps.fermentingRecipes
				MODE_FLUID_EXTRACT -> RecipeMaps.fluidExtractionRecipes
				MODE_EXTRACTOR -> RecipeMaps.extractorRecipes
				MODE_LASER -> RecipeMaps.laserEngraverRecipes
				MODE_AUTOCLAVE -> RecipeMaps.autoclaveRecipes
				MODE_FLUID_SOLIDIFY -> RecipeMaps.fluidSolidifierRecipes
				else -> error("Unknown Mode: $aMode")
			}
		}
	}

	private var mCasing: Int = 0
	private lateinit var structureDef: IStructureDefinition<MTELargeProcessingFactory>

	constructor(id: Int, name: String, nameRegional: String) : super(id, name, nameRegional)

	constructor(name: String) : super(name)

	override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity? {
		return MTELargeProcessingFactory(mName)
	}

	override fun getMachineType(): String {
		return "Nine in One (Rewind)"
	}

	override fun createTooltip(): MultiblockTooltipBuilder? {
		val aBuiltStrings = buildList {
			add("${TOOLTIP_MAP[0]}, ${TOOLTIP_MAP[1]}, ${TOOLTIP_MAP[2]}")
			add("${TOOLTIP_MAP[3]}, ${TOOLTIP_MAP[4]}, ${TOOLTIP_MAP[5]}")
			add("${TOOLTIP_MAP[6]}, ${TOOLTIP_MAP[7]}, ${TOOLTIP_MAP[8]}")
		}

		return MultiblockTooltipBuilder().apply {
			addMachineType(machineType)
			addInfo("Controller Block for the Industrial Multi-Machine")
			addInfo(GtnhNoNerf.Tooltips.ForDeprecatedMachines)
			addInfo("250% faster than using single block machines of the same voltage")
			addInfo("Only uses 80% of the EU/t normally required")
			addInfo("Processes two items per voltage tier")
			addInfo("Machine Type: Metal - ${EnumChatFormatting.YELLOW}${aBuiltStrings[0]}${EnumChatFormatting.RESET}")
			addInfo("Machine Type: Fluid - ${EnumChatFormatting.YELLOW}${aBuiltStrings[1]}${EnumChatFormatting.RESET}")
			addInfo("Machine Type: Misc - ${EnumChatFormatting.YELLOW}${aBuiltStrings[2]}${EnumChatFormatting.RESET}")
			addInfo("Read Multi-Machine Manual for extra information")
			addInfo("${EnumChatFormatting.AQUA}You can use Solidifier Hatch to solidify multiple liquids.${EnumChatFormatting.RESET}")
			addPollutionAmount(getPollutionPerSecond(null))
			addSeparator()
			beginStructureBlock(3, 3, 3, true)
			addController("Front Center")
			addCasingInfoMin("Multi-Use Casings", 6, false)
			addInputBus("Any Casing", 1)
			addOutputBus("Any Casing", 1)
			addInputHatch("Any Casing", 1)
			addOutputHatch("Any Casing", 1)
			addEnergyHatch("Any Casing", 1)
			addMaintenanceHatch("Any Casing", 1)
			addMufflerHatch("Any Casing", 1)
			toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get())
		}
	}

	override fun getStructureDefinition(): IStructureDefinition<MTELargeProcessingFactory> {
		if(!::structureDef.isInitialized) {
			structureDef = StructureDefinition.builder<MTELargeProcessingFactory>().apply {
				addShape(
					mName,
					transpose(
						arrayOf(
							arrayOf("CCC", "CCC", "CCC"),
							arrayOf("C~C", "C-C", "CCC"),
							arrayOf("CCC", "CCC", "CCC")
						)
					)
				)
				addElement(
					'C',
					buildHatchAdder(MTELargeProcessingFactory::class.java)
						.atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
						.casingIndex(TEXTURE_INDEX)
						.dot(1)
						.buildAndChain(onElementPass({ x -> ++x.mCasing }, ofBlock(ModBlocks.blockCasings3Misc, 2)))
				)
			}.build()
		}

		return structureDef
	}

	override fun construct(stackSize: ItemStack, hintsOnly: Boolean) {
		buildPiece(mName, stackSize, hintsOnly, 1, 1, 0)
	}

	override fun survivalConstruct(stackSize: ItemStack, elementBudget: Int, env: ISurvivalBuildEnvironment): Int {
		if(mMachine) return -1
		return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true)
	}

	override fun checkMachine(aBaseMetaTileEntity: IGregTechTileEntity, aStack: ItemStack): Boolean {
		mCasing = 0
		return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch()
	}

	override fun getActiveOverlay(): IIconContainer? {
		return TexturesGtBlock.oMCAIndustrialMultiMachineActive
	}

	override fun getInactiveOverlay(): IIconContainer? {
		return TexturesGtBlock.oMCAIndustrialMultiMachine
	}

	override fun getCasingTextureId(): Int {
		return TEXTURE_INDEX
	}

	override fun getMaxParallelRecipes(): Int {
		return 2 * GTUtility.getTier(maxInputVoltage)
	}

	override fun getMaxEfficiency(aStack: ItemStack?): Int {
		return 100_00 // 100%
	}

	override fun getPollutionPerSecond(aStack: ItemStack?): Int {
		return 0
	}

	private fun getCircuit(t: Array<ItemStack>): ItemStack? {
		for(j in t) {
			if(j.item == GTUtility.getIntegratedCircuit(0).item) {
				if(j.itemDamage in 20..22) {
					return j
				}
			}
		}
		return null
	}

	private fun getCircuitID(circuit: ItemStack): Int {
		val h = circuit.itemDamage
		val t = when(h) {
			20 -> 0
			21 -> 1
			22 -> 2
			else -> -1
		}
		return MODE_MAP[machineMode][t]
	}

	override fun getAvailableRecipeMaps(): Collection<RecipeMap<*>> {
		return listOf(
			RecipeMaps.compressorRecipes,
			RecipeMaps.latheRecipes,
			RecipeMaps.polarizerRecipes,
			RecipeMaps.fermentingRecipes,
			RecipeMaps.fluidExtractionRecipes,
			RecipeMaps.extractorRecipes,
			RecipeMaps.laserEngraverRecipes,
			RecipeMaps.autoclaveRecipes,
			RecipeMaps.fluidSolidifierRecipes,
		)
	}

	override fun getRecipeCatalystPriority(): Int {
		return -10
	}

	override fun createProcessingLogic(): ProcessingLogic? {
		return object : ProcessingLogic() {
			var lastCircuit: ItemStack? = null
			var lastMode: Int = -1

			override fun findRecipeMatches(map: RecipeMap<*>?): Stream<GTRecipe?> {
				val circuit = getCircuit(inputItems)
				if(circuit == null) {
					return Stream.empty()
				}
				if(!GTUtility.areStacksEqual(circuit, lastCircuit)) {
					lastRecipe = null
					lastCircuit = circuit
				}
				if(machineMode != lastMode) {
					lastRecipe = null
					lastMode = machineMode
				}
				val foundMap = getRecipeMap(getCircuitID(circuit))
					?: return Stream.empty()
				return super.findRecipeMatches(foundMap)
			}
		}.apply {
			setSpeedBonus(1 / 3.5)
			setEuModifier(0.8)
			setMaxParallelSupplier(this@MTELargeProcessingFactory::getMaxParallelRecipes)
		}
	}

	override fun getMachineModeName(): String? {
		return StatCollector.translateToLocal("GT5U.GTPP_MULTI_INDUSTRIAL_MULTI_MACHINE.mode.$machineMode")
	}

	override fun getInfoData(): Array<out String?>? {
		val data = super.getInfoData()
		return buildList {
			addAll(data)
			when(machineMode) {
				MACHINE_MODE_METAL -> add(StatCollector.translateToLocal("GTPP.multiblock.multimachine.metal"))
				MACHINE_MODE_FLUID -> add(StatCollector.translateToLocal("GTPP.multiblock.multimachine.fluid"))
				MACHINE_MODE_MISC -> add(StatCollector.translateToLocal("GTPP.multiblock.multimachine.misc"))
			}
		}.toTypedArray()
	}

	override fun loadNBTData(nbt: NBTTagCompound) {
		if(nbt.hasKey("mInternalMode")) {
			machineMode = nbt.getInteger("mInternalMode")
		}
		super.loadNBTData(nbt)
	}

	override fun isInputSeparationEnabled(): Boolean {
		return true
	}

	override fun doCheckRecipe(): CheckRecipeResult {
		if(machineMode != MACHINE_MODE_MISC || !isInputSeparationEnabled) {
			return super.doCheckRecipe()
		}

		var result = CheckRecipeResultRegistry.NO_RECIPE

		if(supportsCraftingMEBuffer()) {
			for(dualInputHatch in mDualInputHatches) {
				val inv = dualInputHatch.inventories()
				while(inv.hasNext()) {
					val slot = inv.next()
					processingLogic.setInputItems(*slot.itemInputs)
					processingLogic.setInputFluids(*slot.fluidInputs)
					val foundResult = processingLogic.process()
					if(foundResult.wasSuccessful()) {
						return foundResult
					}
					if(foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
						result = foundResult
					}
				}
			}
		}

		for(solidifierHatch in mInputHatches) {
			if(solidifierHatch is MTEHatchSolidifier) {
				val mold = solidifierHatch.mold
				val fluid = solidifierHatch.fluid

				if(mold != null && fluid != null) {
					val inputItems = arrayListOf<ItemStack>(mold, GTUtility.getIntegratedCircuit(22))

					processingLogic.setInputItems(inputItems)
					processingLogic.setInputFluids(fluid)

					val foundResult = processingLogic.process()
					if(foundResult.wasSuccessful()) {
						return foundResult
					}
					if(foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
						result = foundResult
					}
				}
			}
		}

		processingLogic.clear()
		processingLogic.setInputFluids(storedFluids)
		for(bus in mInputBusses) {
			if(bus is MTEHatchCraftingInputME) continue

			val inputItems = arrayListOf<ItemStack>()
			for(i in bus.sizeInventory - 1 downTo 0) {
				val stored = bus.getStackInSlot(i)
				if(stored != null) {
					inputItems.add(stored)
				}
			}
			if(canUseControllerSlotForRecipe() && controllerSlot != null) {
				inputItems.add(controllerSlot)
			}
			processingLogic.setInputItems(inputItems)

			val foundResult = processingLogic.process()
			if(foundResult.wasSuccessful()) {
				return foundResult
			}
			if(foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
				result = foundResult
			}
		}

		return result
	}

	override fun getStoredFluids(): ArrayList<FluidStack> {
		val list = arrayListOf<FluidStack>()
		for(hatch in filterValidMTEs(mInputHatches)) {
			if(hatch is MTEHatchSolidifier) continue

			setHatchRecipeMap(hatch)
			if(hatch is MTEHatchMultiInput) {
				for(fluid in hatch.storedFluid) {
					if(fluid != null) {
						list.add(fluid)
					}
				}
			} else if(hatch is MTEHatchInputME) {
				if(hatch.isValid) {
					for(fluid in hatch.storedFluids) {
						if(fluid != null) {
							list.add(fluid)
						}
					}
				}
			} else {
				if(hatch.fillableStack != null) {
					list.add(hatch.fillableStack)
				}
			}
		}

		return list
	}

	override fun getWailaNBTData(
		player: EntityPlayerMP?,
		tile: TileEntity?,
		tag: NBTTagCompound,
		world: World?,
		x: Int,
		y: Int,
		z: Int,
	) {
		super.getWailaNBTData(player, tile, tag, world, x, y, z)
		tag.setInteger("mode", machineMode)
	}

	override fun getWailaBody(
		itemStack: ItemStack?,
		currentTip: MutableList<String>,
		accessor: IWailaDataAccessor,
		config: IWailaConfigHandler?,
	) {
		super.getWailaBody(itemStack, currentTip, accessor, config)
		val tag = accessor.nbtData
		if(tag.hasKey("mode")) {
			currentTip.add(
				"${StatCollector.translateToLocal("GT5U.machines.oreprocessor1")} ${EnumChatFormatting.WHITE}${
					StatCollector.translateToLocal(
						"GT5U.GTPP_MULTI_INDUSTRIAL_MULTI_MACHINE.mode." + tag.getInteger("mode")
					)
				}${EnumChatFormatting.RESET}"
			)
		}
	}

	override fun supportsMachineModeSwitch(): Boolean {
		return true
	}

	override fun onModeChangeByScrewdriver(
		side: ForgeDirection?,
		aPlayer: EntityPlayer?,
		aX: Float,
		aY: Float,
		aZ: Float,
	) {
		setMachineMode(nextMachineMode())
		PlayerUtils.messagePlayer(
			aPlayer,
			String.format(StatCollector.translateToLocal("GT5U.MULTI_MACHINE_CHANGE"), machineModeName)
		)
	}

	override fun nextMachineMode(): Int {
		mLastRecipe = null

		return when(machineMode) {
			MACHINE_MODE_METAL -> MACHINE_MODE_FLUID
			MACHINE_MODE_FLUID -> MACHINE_MODE_MISC
			MACHINE_MODE_MISC -> MACHINE_MODE_METAL
			else -> MACHINE_MODE_METAL
		}
	}

	override fun setMachineModeIcons() {
		machineModeIcons.clear()
		machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_METAL)
		machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_LPF_FLUID)
		machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT)
	}
}
