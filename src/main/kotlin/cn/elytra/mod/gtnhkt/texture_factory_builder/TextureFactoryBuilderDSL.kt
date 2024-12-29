package cn.elytra.mod.gtnhkt.texture_factory_builder

import gregtech.api.interfaces.ITexture
import gregtech.api.interfaces.ITextureBuilder
import gregtech.api.render.TextureFactory

fun buildTexture(block: ITextureBuilder.() -> Unit): ITexture {
	return TextureFactory.builder().apply(block).build()
}
