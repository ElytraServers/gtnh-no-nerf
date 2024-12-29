package cn.elytra.mod.gtnhkt.ui_builder

import com.gtnewhorizons.modularui.api.screen.ModularWindow
import com.gtnewhorizons.modularui.api.widget.Widget
import com.gtnewhorizons.modularui.common.widget.ButtonWidget

fun ModularWindow.Builder.widget(block: () -> Widget) {
	widget(block())
}

fun ModularWindow.Builder.button(block: ButtonWidget.() -> Unit) = widget { ButtonWidget().apply(block) }
