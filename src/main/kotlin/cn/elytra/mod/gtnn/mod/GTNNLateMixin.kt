package cn.elytra.mod.gtnn.mod

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader
import com.gtnewhorizon.gtnhmixins.LateMixin
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@LateMixin
class GTNNLateMixin : ILateMixinLoader {

	@JvmField
	val logger: Logger = LogManager.getLogger("GTNN-Mixin")

	override fun getMixinConfig(): String {
		return "mixins.gtnn.late.json"
	}

	override fun getMixins(loadedMods: Set<String>): List<String> {
		println(loadedMods)
		logger.info("Loaded Mods: {}", loadedMods)
		return listOf() // TODO
	}
}
