package cn.elytra.mod.gtnn

import cn.elytra.mod.gtnn.modules.mixins.MixinLoader
import com.gtnewhorizon.gtnhmixins.ILateMixinLoader
import com.gtnewhorizon.gtnhmixins.LateMixin
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@LateMixin
class GTNNLateMixin : ILateMixinLoader {

	@JvmField
	val logger: Logger = LogManager.getLogger("GTNN-LateMixin-Loader")

	override fun getMixinConfig(): String {
		return "mixins.gtnn.late.json"
	}

	override fun getMixins(loadedMods: Set<String>): List<String> {
		logger.info("Loaded Mods: {}", loadedMods)
		// load configuration before gathering the mixin classes
		MixinLoader.readConfig()
		// gather mixin classes
		return MixinLoader.getMixins(loadedMods)
	}
}
