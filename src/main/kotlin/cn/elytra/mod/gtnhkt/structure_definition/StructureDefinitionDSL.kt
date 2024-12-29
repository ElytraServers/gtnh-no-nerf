package cn.elytra.mod.gtnhkt.structure_definition

import com.gtnewhorizon.structurelib.structure.IStructureDefinition
import com.gtnewhorizon.structurelib.structure.IStructureElement
import com.gtnewhorizon.structurelib.structure.StructureDefinition
import com.gtnewhorizon.structurelib.structure.StructureUtility
import gregtech.api.util.GTStructureUtility
import gregtech.api.util.HatchElementBuilder
import kotlin.properties.Delegates

/**
 * Creates a [StructureDefinition.Builder] scope.
 */
fun <T> buildStructure(block: StructureDefinition.Builder<T>.() -> Unit): IStructureDefinition<T> =
	StructureDefinition.builder<T>().apply(block).build()

private typealias StructDefBuilder<T> = StructureDefinition.Builder<T>

fun <T> StructDefBuilder<T>.shape(name: String, structurePiece: () -> Array<Array<String>>) {
	addShape(name, structurePiece())
}

fun <T> StructDefBuilder<T>.element(name: Char, structurePiece: SingleElementBuilderScope<T>.() -> Unit) {
	addElement(name, SingleElementBuilderScope<T>().apply(structurePiece).build())
}

interface IElementBuilderScope<T> {
	fun addElement(element: IStructureElement<T>)
	fun build(): IStructureElement<T>

	fun lazy(block: (T) -> IStructureElement<T>) {
		addElement(StructureUtility.lazy { it: T ->
			SingleElementBuilderScope<T>().apply {
				block(it)
			}.build()
		})
	}

	fun onElementPass(check: IStructureElement<T>, onCheckPass: (T) -> Unit) {
		 addElement(StructureUtility.onElementPass<IStructureElement<T>, T>(onCheckPass, check))
	}

	fun chain(block: ChainElementBuilderScope<T>.() -> Unit) {
		addElement(ChainElementBuilderScope<T>().apply(block).build())
	}
}

/**
 * The DSL for providing a structure definition.
 *
 * You may not add multiple definitions as only the first one will be used.
 */
class SingleElementBuilderScope<T> : IElementBuilderScope<T> {
	private var element: IStructureElement<T>? by Delegates.vetoable(null) { _, old, new ->
		old == null && new != null
	}

	override fun addElement(element: IStructureElement<T>) {
		this.element = element
	}

	override fun build(): IStructureElement<T> {
		return requireNotNull(this.element) { "element was not assigned before getting result!" }
	}
}

/**
 * The DSL for providing a list of chain to create the structure definition.
 *
 * You can add multiple elements, and they will be chained together by the order you add.
 */
class ChainElementBuilderScope<T> : IElementBuilderScope<T> {
	private val elements = mutableListOf<IStructureElement<T>>()

	override fun addElement(element: IStructureElement<T>) {
		elements.add(element)
	}

	override fun build(): IStructureElement<T> {
		return StructureUtility.ofChain(elements)
	}
}

fun <T> buildHatchAdder(block: HatchElementBuilder<T>.() -> Unit): IStructureElement<T> {
	return GTStructureUtility.buildHatchAdder<T>().apply(block).build()
}
