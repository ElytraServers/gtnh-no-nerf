package cn.elytra.mod.gtnn.client

import gregtech.api.GregTechAPI
import gregtech.api.enums.Mods
import gregtech.api.interfaces.IIconContainer
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.IIcon
import net.minecraft.util.ResourceLocation

private fun loadGTIcon(path: String): IIcon {
	return GregTechAPI.sBlockIcons.registerIcon(Mods.GregTech.getResourcePath("iconsets", path))
}

private fun loadGTPlusPlusIcon(path: String): IIcon {
	return GregTechAPI.sBlockIcons.registerIcon(Mods.GTPlusPlus.ID + ":" + path)
}

object NNTextures {

	enum class GT : IIconContainer {
		OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE,
		OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW,
		OVERLAY_FRONT_PROCESSING_ARRAY,
		OVERLAY_FRONT_PROCESSING_ARRAY_GLOW,
		;

		private lateinit var iconValue: IIcon

		init {
			GregTechAPI.sGTBlockIconload.add { iconValue = loadGTIcon(name) }
		}

		override fun getIcon(): IIcon? = iconValue
		override fun getOverlayIcon(): IIcon? = null
		override fun getTextureFile(): ResourceLocation? = TextureMap.locationBlocksTexture
	}

	@Suppress("EnumEntryName")
	enum class GTPlusPlus(val path: String) : IIconContainer {
		oMCAIndustrialMultiMachine("iconsets/controllerFaces/industrialMultiMachine"),
		oMCAIndustrialMultiMachineActive("iconsets/controllerFaces/industrialMultiMachineActive"),
		;

		private lateinit var iconValue: IIcon

		init {
			GregTechAPI.sGTBlockIconload.add { iconValue = loadGTPlusPlusIcon(path) }
		}

		override fun getIcon(): IIcon? = iconValue
		override fun getOverlayIcon(): IIcon? = null
		override fun getTextureFile(): ResourceLocation? = TextureMap.locationBlocksTexture
	}

}
