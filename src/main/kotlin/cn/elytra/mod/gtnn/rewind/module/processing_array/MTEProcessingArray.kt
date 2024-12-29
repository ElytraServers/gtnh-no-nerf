package cn.elytra.mod.gtnn.rewind.module.processing_array

import cn.elytra.mod.gtnhkt.multiblock_tooltip.buildMultiblockTooltip
import cn.elytra.mod.gtnhkt.structure_definition.buildHatchAdder
import cn.elytra.mod.gtnhkt.structure_definition.buildStructure
import cn.elytra.mod.gtnhkt.structure_definition.element
import cn.elytra.mod.gtnhkt.structure_definition.shape
import cn.elytra.mod.gtnhkt.texture_factory_builder.buildTexture
import cn.elytra.mod.gtnhkt.ui_builder.button
import cn.elytra.mod.gtnhkt.ui_builder.widget
import cn.elytra.mod.gtnhkt.utils.GTUtilityKt
import cn.elytra.mod.gtnn.GtnhNoNerf
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable
import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment
import com.gtnewhorizon.structurelib.structure.StructureUtility
import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.screen.UIBuildContext
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget
import gregtech.GTMod
import gregtech.api.GregTechAPI
import gregtech.api.enums.GTValues
import gregtech.api.enums.HatchElement
import gregtech.api.enums.HatchElement.*
import gregtech.api.enums.SoundResource
import gregtech.api.enums.Textures
import gregtech.api.enums.Textures.BlockIcons.*
import gregtech.api.gui.modularui.GTUITextures
import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.metatileentity.IMetaTileEntity
import gregtech.api.interfaces.tileentity.IGregTechTileEntity
import gregtech.api.logic.ProcessingLogic
import gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY
import gregtech.api.metatileentity.implementations.MTEBasicMachine.isValidForLowGravity
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock
import gregtech.api.recipe.RecipeMap
import gregtech.api.recipe.check.CheckRecipeResult
import gregtech.api.recipe.check.CheckRecipeResultRegistry
import gregtech.api.recipe.check.SimpleCheckRecipeResult
import gregtech.api.recipe.metadata.CompressionTierKey
import gregtech.api.util.*
import gregtech.common.blocks.ItemMachines
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

/**
 * A copy of [gregtech.common.tileentities.machines.multi.MTEProcessingArray].
 *
 * Default ID: 1199
 */
class MTEProcessingArray : MTEExtendedPowerMultiBlockBase<MTEProcessingArray>, ISurvivalConstructable {

	companion object {
		private const val STRUCTURE_MAIN_PIECE = "main"
		private val StructureDef by lazy {
			buildStructure<MTEProcessingArray> {
				shape(STRUCTURE_MAIN_PIECE) {
					StructureUtility.transpose(
						arrayOf(
							arrayOf("hhh", "hhh", "hhh"),
							arrayOf("h~h", "h-h", "hhh"),
							arrayOf("hhh", "hhh", "hhh"),
						)
					)
				}

				element('h') {
					chain {
						lazy {
							buildHatchAdder {
								atLeastList(it.getAllowedHatches())
								casingIndex(48)
								dot(1)
							}
						}
						onElementPass(StructureUtility.ofBlock(GregTechAPI.sBlockCasings4, 0)) {
							it.mCasingAmount++
						}
					}
				}
			}
		}
	}

	var mCasingAmount: Int = 0

	var mLastRecipeMap: RecipeMap<*>? = null
	var lastControllerStack: ItemStack? = null
	var tTier: Int = 0
	var mMultiply: Int = 0
	var downtierUEV: Boolean = true

	constructor(id: Int, name: String, nameRegional: String) : super(id, name, nameRegional)
	constructor(name: String) : super(name)

	fun getMaxParallel(): Int {
		return controllerSlot?.stackSize?.shl(mMultiply) ?: 0
	}

	fun setTierAndMultiply() {
		val machine = ItemMachines.getMetaTileEntity(controllerSlot)
		tTier = if(machine is MTETieredMachineBlock) machine.mTier.toInt() else 0
		mMultiply = 0
		if(downtierUEV && tTier > 9) {
			tTier-- // lowers down the tier by 1 to allow for bigger parallel
			mMultiply = 2 // multiplies Parallels by 4x, keeping the energy cost
		}
	}

	override fun newMetaEntity(aTileEntity: IGregTechTileEntity?): IMetaTileEntity? {
		return MTEProcessingArray(this.mName)
	}

	override fun createTooltip(): MultiblockTooltipBuilder? {
		return buildMultiblockTooltip {
			addMachineType("Processing Array")
			addInfo(GtnhNoNerf.Tooltips.ForDeprecatedMachines)
			addInfo("Runs supplied machines as if placed in the world")
			addInfo("Place up to 64 single block GT machines into the controller")
			addInfo("Note that you still need to supply power to them all")
			addInfo("Use a screwdriver to enable separate input busses")
			addInfo("Use a wire cutter to disable UEV+ downtiering")
			addInfo("Doesn't work on certain machines, deal with it")
			addInfo("Use it if you hate GT++, or want even more speed later on")
			beginStructureBlock(3, 3, 3, true)
			addController("Front center")
			addCasingInfoRange("Robust Tungstensteel Machine Casing", 14, 24, false)
			addEnergyHatch("Any casing", 1)
			addMaintenanceHatch("Any casing", 1)
			addInputBus("Any casing", 1)
			addInputHatch("Any casing", 1)
			addOutputBus("Any casing", 1)
			addOutputHatch("Any casing", 1)
			toolTipFinisher()
		}
	}

	override fun addToMachineList(aTileEntity: IGregTechTileEntity?, aBaseCasingIndex: Int): Boolean {
		return super.addToMachineList(aTileEntity, aBaseCasingIndex) || addExoticEnergyInputToMachineList(
			aTileEntity,
			aBaseCasingIndex
		)
	}

	override fun getTexture(
		baseMetaTileEntity: IGregTechTileEntity?,
		side: ForgeDirection?,
		facing: ForgeDirection?,
		colorIndex: Int,
		active: Boolean,
		redstoneLevel: Boolean,
	): Array<ITexture> {
		if(side == facing) {
			return if(active) arrayOf(
				Textures.BlockIcons.casingTexturePages[0][48],
				buildTexture {
					addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
					extFacing()
				},
				buildTexture {
					addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
					extFacing()
					glow()
				},
			)
			else arrayOf(
				Textures.BlockIcons.casingTexturePages[0][48],
				buildTexture {
					addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
					extFacing()
				},
				buildTexture {
					addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
					extFacing()
					glow()
				},
			)
		}
		return arrayOf(Textures.BlockIcons.casingTexturePages[0][48])
	}

	override fun getRecipeMap(): RecipeMap<*>? {
		return mLastRecipeMap
	}

	override fun isCorrectMachinePart(aStack: ItemStack?): Boolean {
		return aStack != null && aStack.unlocalizedName.startsWith("gt.blockmachines.")
	}

	private fun fetchRecipeMap(): RecipeMap<*>? {
		return if(isCorrectMachinePart(controllerSlot)) {
			ProcessingArrayManager.giveRecipeMap(ProcessingArrayManager.getMachineName(controllerSlot))
		} else null
	}

	override fun sendStartMultiBlockSoundLoop() {
		ProcessingArrayManager.getSoundResource(ProcessingArrayManager.getMachineName(controllerSlot))
			?.let { sendLoopStart(it.id.toByte()) }
	}

	override fun startSoundLoop(aIndex: Byte, aX: Double, aY: Double, aZ: Double) {
		super.startSoundLoop(aIndex, aX, aY, aZ)
		SoundResource.get(if(aIndex < 0) aIndex + 256 else 0)
			?.let { GTUtility.doSoundAtClient(it, timeBetweenProcessSounds, 1.0F, aX, aY, aZ) }
	}

	override fun checkProcessing(): CheckRecipeResult {
		if(GTUtility.areStacksEqual(lastControllerStack, controllerSlot)) {
			// controller slot has changed
			lastControllerStack = controllerSlot
			mLastRecipeMap = fetchRecipeMap()
			setTierAndMultiply()
		}
		if(mLastRecipeMap == null) return SimpleCheckRecipeResult.ofFailure("no_machine")
		if(mLockedToSingleRecipe && mSingleRecipeCheck != null) {
			if(mSingleRecipeCheck.recipeMap != mLastRecipeMap) {
				return SimpleCheckRecipeResult.ofFailure("machine_mismatch")
			}
		}

		return super.checkProcessing()
	}

	override fun createProcessingLogic(): ProcessingLogic? {
		return object : ProcessingLogic() {
			override fun validateRecipe(recipe: GTRecipe): CheckRecipeResult {
				if(recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0)!! > 0) {
					return CheckRecipeResultRegistry.NO_RECIPE
				}
				if(GTMod.gregtechproxy.mLowGravProcessing && recipe.mSpecialValue == -100
					&& !isValidForLowGravity(recipe, baseMetaTileEntity.world.provider.dimensionId)
				) {
					return SimpleCheckRecipeResult.ofFailure("high_gravity")
				}
				if(recipe.mEUt > availableVoltage) {
					return CheckRecipeResultRegistry.insufficientPower(recipe.mEUt.toLong())
				}
				return CheckRecipeResultRegistry.SUCCESSFUL
			}
		}.setMaxParallelSupplier { getMaxParallel() }
	}

	override fun canUseControllerSlotForRecipe(): Boolean {
		return false
	}

	override fun setProcessingLogicPower(logic: ProcessingLogic) {
		logic.setAvailableVoltage(GTValues.V[tTier] * (mLastRecipeMap?.amperage ?: 1))
		logic.setAvailableAmperage(getMaxParallel().toLong())
		logic.setAmperageOC(false)
	}

	override fun onPostTick(aBaseMetaTileEntity: IGregTechTileEntity?, aTick: Long) {
		super.onPostTick(aBaseMetaTileEntity, aTick)
		if(mMachine && aTick % 20 == 0L) {
			mInputBusses.forEach { it.mRecipeMap = mLastRecipeMap }
			mInputHatches.forEach { it.mRecipeMap = mLastRecipeMap }
		}
	}

	override fun getStructureDefinition(): IStructureDefinition<MTEProcessingArray> {
		return StructureDef
	}

	override fun construct(stackSize: ItemStack?, hintsOnly: Boolean) {
		buildPiece(STRUCTURE_MAIN_PIECE, stackSize, hintsOnly, 1, 1, 0)
	}

	override fun survivalConstruct(stackSize: ItemStack?, elementBudget: Int, env: ISurvivalBuildEnvironment?): Int {
		if(mMachine) return -1
		// there is a TYPO, lol
		return survivialBuildPiece(STRUCTURE_MAIN_PIECE, stackSize, 1, 1, 0, elementBudget, env, false, true)
	}

	fun checkHatches(): Boolean {
		return mMaintenanceHatches.size == 1
	}

	override fun saveNBTData(aNBT: NBTTagCompound) {
		super.saveNBTData(aNBT)
		aNBT.setBoolean("downtierUEV", downtierUEV)
	}

	override fun loadNBTData(aNBT: NBTTagCompound) {
		super.loadNBTData(aNBT)
		if(aNBT.hasKey("mSeparate")) {
			inputSeparation = aNBT.getBoolean("mSeparate")
		}
		if(aNBT.hasKey("mUseMultiparallelMode")) {
			batchMode = aNBT.getBoolean("mUseMultiparallelMode")
		}
		downtierUEV = aNBT.getBoolean("downtierUEV")
	}

	override fun onScrewdriverRightClick(
		side: ForgeDirection,
		aPlayer: EntityPlayer,
		aX: Float,
		aY: Float,
		aZ: Float,
	) {
		if(aPlayer.isSneaking) {
			super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ)
		} else {
			inputSeparation = !inputSeparation
			GTUtility.sendChatToPlayer(
				aPlayer,
				StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation
			)
		}
	}

	override fun onWireCutterRightClick(
		side: ForgeDirection,
		wrenchingSide: ForgeDirection,
		aPlayer: EntityPlayer,
		aX: Float,
		aY: Float,
		aZ: Float,
		aTool: ItemStack?,
	): Boolean {
		if(aPlayer.isSneaking) {
			batchMode = !batchMode
			if(batchMode) {
				GTUtility.sendChatToPlayer(aPlayer, "Batch recipes")
			} else {
				GTUtility.sendChatToPlayer(aPlayer, "Don't batch recipes")
			}
		} else {
			downtierUEV = !downtierUEV
			GTUtility.sendChatToPlayer(aPlayer, "Treat UEV+ machines as multiple UHV $downtierUEV")
		}
		return true
	}

	override fun getMaxEfficiency(aStack: ItemStack?): Int {
		return 10000
	}

	override fun getDamageToComponent(aStack: ItemStack?): Int {
		return 0
	}

	override fun explodesOnComponentBreak(aStack: ItemStack?): Boolean {
		return false
	}

	fun getAllowedHatches(): List<HatchElement> {
		return listOf(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, ExoticEnergy)
	}

	override fun checkMachine(aBaseMetaTileEntity: IGregTechTileEntity?, aStack: ItemStack?): Boolean {
		mExoticEnergyHatches.clear()
		mCasingAmount = 0
		return checkPiece(STRUCTURE_MAIN_PIECE, 1, 1, 0) && mCasingAmount >= 14 && checkHatches()
	}

	override fun getInfoData(): Array<String> {
		val storedEnergy = GTUtility.validMTEList(mExoticEnergyHatches).sumOf { it.baseMetaTileEntity.storedEU }
		val maxEnergy = GTUtility.validMTEList(mExoticEnergyHatches).sumOf { it.baseMetaTileEntity.euCapacity }

		return buildList {
			add(
				StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
					+ EnumChatFormatting.GREEN
					+ GTUtilityKt.formatNumbers(mProgresstime / 20)
					+ EnumChatFormatting.RESET
					+ " s / "
					+ EnumChatFormatting.YELLOW
					+ GTUtilityKt.formatNumbers(mMaxProgresstime / 20)
					+ EnumChatFormatting.RESET
					+ " s"
			)
			add(
				StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
					+ EnumChatFormatting.GREEN
					+ GTUtilityKt.formatNumbers(storedEnergy)
					+ EnumChatFormatting.RESET
					+ " EU / "
					+ EnumChatFormatting.YELLOW
					+ GTUtilityKt.formatNumbers(maxEnergy)
					+ EnumChatFormatting.RESET
					+ " EU"
			)
			add(
				StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
					+ EnumChatFormatting.RED
					+ GTUtilityKt.formatNumbers(-lEUt)
					+ EnumChatFormatting.RESET
					+ " EU/t"
			)
			add(
				StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
					+ EnumChatFormatting.YELLOW
					+ GTUtilityKt
					.formatNumbers(ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))
					+ EnumChatFormatting.RESET
					+ " EU/t(*"
					+ GTUtilityKt
					.formatNumbers(ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList()))
					+ "A) "
					+ StatCollector.translateToLocal("GT5U.machines.tier")
					+ ": "
					+ EnumChatFormatting.YELLOW
					+ GTValues.VN[GTUtilityKt.getTier(
					ExoticEnergyInputHelper.getMaxInputVoltageMulti(
						getExoticAndNormalEnergyHatchList()
					)
				)]
					+ EnumChatFormatting.RESET
			)
			add(
				StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
					+ EnumChatFormatting.RED
					+ (idealStatus - repairStatus)
					+ EnumChatFormatting.RESET
					+ " "
					+ StatCollector.translateToLocal("GT5U.multiblock.efficiency")
					+ ": "
					+ EnumChatFormatting.YELLOW
					+ mEfficiency / 100.0F
					+ EnumChatFormatting.RESET
					+ " %"
			)
			add(
				StatCollector.translateToLocal("GT5U.PA.machinetier") + ": "
					+ EnumChatFormatting.GREEN
					+ tTier
					+ EnumChatFormatting.RESET
					+ " "
					+ StatCollector.translateToLocal("GT5U.PA.discount")
					+ ": "
					+ EnumChatFormatting.GREEN
					+ 1
					+ EnumChatFormatting.RESET
					+ " x"
			)
			add(
				StatCollector.translateToLocal("GT5U.PA.parallel") + ": "
					+ EnumChatFormatting.GREEN
					+ GTUtilityKt.formatNumbers(getMaxParallel())
					+ EnumChatFormatting.RESET
			)
		}.toTypedArray()
	}

	override fun supportsInputSeparation(): Boolean {
		return true
	}

	override fun supportsBatchMode(): Boolean {
		return true
	}

	override fun supportsSingleRecipeLocking(): Boolean {
		return true
	}

	override fun supportsVoidProtection(): Boolean {
		return true
	}

	override fun supportsSlotAutomation(aSlot: Int): Boolean {
		return aSlot == controllerSlotIndex
	}

	override fun addUIWidgets(builder: ModularWindow.Builder, buildContext: UIBuildContext) {
		super.addUIWidgets(builder, buildContext)

		builder.button {
			setOnClick { clickData, widget ->
				downtierUEV = !downtierUEV
				setTierAndMultiply()
			}
			setPlayClickSound(true)
			setBackground {
				if(downtierUEV) {
					arrayOf(GTUITextures.BUTTON_STANDARD_PRESSED, GTUITextures.OVERLAY_BUTTON_DOWN_TIERING_ON)
				} else {
					arrayOf(GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_DOWN_TIERING_OFF)
				}
			}
			setPos(80, 91)
			setSize(16, 16)
			addTooltip(StatCollector.translateToLocal("GT5U.gui.button.down_tier"))
			setTooltipShowUpDelay(TOOLTIP_DELAY)
		}

		builder.widget {
			FakeSyncWidget.BooleanSyncer({ downtierUEV }, { downtierUEV = it })
		}
	}

	override fun getWailaNBTData(
		player: EntityPlayerMP?,
		tile: TileEntity?,
		tag: NBTTagCompound,
		world: World?,
		x: Int,
		y: Int,
		z: Int
	) {
		super.getWailaNBTData(player, tile, tag, world, x, y, z)
		if(mLastRecipeMap != null && controllerSlot != null) {
			tag.setString("type", controllerSlot.displayName)
		}
	}

	override fun getWailaBody(
		itemStack: ItemStack?,
		currentTip: MutableList<String>,
		accessor: IWailaDataAccessor,
		config: IWailaConfigHandler?
	) {
		super.getWailaBody(itemStack, currentTip, accessor, config)
		val tag = accessor.nbtData
		if(tag.hasKey("type")) {
			currentTip.add("Machine: ${EnumChatFormatting.YELLOW}${tag.getString("type")}")
		} else {
			currentTip.add("Machine: ${EnumChatFormatting.YELLOW}None")
		}
	}
}
