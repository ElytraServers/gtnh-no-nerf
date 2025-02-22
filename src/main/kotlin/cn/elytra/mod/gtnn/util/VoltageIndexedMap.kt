package cn.elytra.mod.gtnn.util

import gregtech.api.enums.VoltageIndex
import org.intellij.lang.annotations.MagicConstant
import java.util.*

/**
 * A special [EnumMap] designed for GregTech voltages.
 *
 * You can directly access the elements by [VoltageIndex], also using the [Voltage] enum defined in [VoltageIndexedMap].
 *
 * @sample cn.elytra.mod.gtnn.modules.simple.module.disassembler.ModDisassembler.Disassembler
 */
class VoltageIndexedMap<T>() : EnumMap<VoltageIndexedMap.Voltage, T>(Voltage::class.java) {

	/**
	 * @see gregtech.api.enums.VoltageIndex
	 */
	enum class Voltage {
		ULV, LV, MV, HV, EV, IV, LuV, ZPM, UV, UHV, UEV, UIV, UMV, UXV, MAX,
		;

		companion object {
			private val VALUES = hashMapOf<Int, Voltage>()

			init {
				entries.forEach { v ->
					VALUES.put(v.ordinal, v)
				}
			}

			fun get(@MagicConstant(valuesFromClass = VoltageIndex::class) voltage: Int): Voltage? {
				return VALUES[voltage]
			}
		}
	}

	fun put(@MagicConstant(valuesFromClass = VoltageIndex::class) voltageIndex: Int, value: T): T? {
		val voltage = Voltage.get(voltageIndex) ?: error("invalid voltage tier $voltageIndex")
		return this.put(voltage, value)
	}

	fun get(@MagicConstant(valuesFromClass = VoltageIndex::class) voltageIndex: Int): T? {
		val voltage = Voltage.get(voltageIndex) ?: error("invalid voltage tier $voltageIndex")
		return this.get(voltage)
	}
}
