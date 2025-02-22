@file:Suppress("SpellCheckingInspection")

package cn.elytra.mod.gtnn.modules.mixins

enum class TargetMod(private val modId: String) {

	GregTech("gregtech"),
	NewHorizonsCoreMod("dreamcraft"),
	GoodGenerator("GoodGenerator"),
	TecTech("tectech"),
	GTPlusPlus("miscutils"),
	GGFab("ggfab"),
	;

	var isLoaded: Boolean = false
		private set

	internal fun init(loadedMods: Set<String>) {
		if(this.modId in loadedMods) {
			isLoaded = true
			MixinLoader.logger.info("Mod {} is loaded", modId)
		}
	}

}
