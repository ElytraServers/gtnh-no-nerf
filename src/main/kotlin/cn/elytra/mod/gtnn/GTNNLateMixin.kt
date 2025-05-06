package cn.elytra.mod.gtnn

import cn.elytra.mod.gtnn.mod_v2.ModuleManager
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader
import com.gtnewhorizon.gtnhmixins.LateMixin
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@LateMixin
class GTNNLateMixin : ILateMixinLoader {

	@JvmField
	val logger: Logger = LogManager.getLogger("GTNNLateMixin")

	override fun getMixinConfig(): String {
		return "mixins.gtnn.late.json"
	}

	override fun getMixins(loadedMods: Set<String>): List<String> {
		logger.info("Loaded Mods: {}", loadedMods)
		// gather mixin classes
		return buildList {
			addAll(ModuleManager.gatherMixinsToApply(loadedMods))
		}
	}
}
