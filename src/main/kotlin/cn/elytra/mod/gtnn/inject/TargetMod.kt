package cn.elytra.mod.gtnn.inject

import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension

enum class TargetMod(val modName: String, val jarNamePrefix: String, val shouldLoadInDev: Boolean = true) {

	GregTech("GregTech", "GregTech"),
	NewHorizonsCoreMod("GT: New Horizons", "GTNewHorizonsCoreMod"),
	GoodGenerator("GoodGenerator", "GoodGenerator"),
	TecTech("TecTech", "TecTech"),
	@Deprecated("GT++ is embedded in GT5U now", ReplaceWith("TargetMod.GregTech"))
	GTPlusPlus("GT++", "GT-PlusPlus"),

	;

	val jarNameLowerCase: String get() = jarNamePrefix.lowercase()

	fun isMatchingJar(path: Path): Boolean {
		return path.nameWithoutExtension.lowercase().startsWith(jarNameLowerCase) &&
			path.extension == "jar"
	}

}
