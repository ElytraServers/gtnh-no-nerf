package cn.elytra.mod.gtnhkt.multiblock_tooltip

import gregtech.api.util.MultiblockTooltipBuilder

fun buildMultiblockTooltip(block: MultiblockTooltipBuilder.() -> Unit): MultiblockTooltipBuilder {
	return MultiblockTooltipBuilder().apply(block)
}
