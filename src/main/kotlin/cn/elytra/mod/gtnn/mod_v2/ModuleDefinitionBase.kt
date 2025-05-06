package cn.elytra.mod.gtnn.mod_v2

import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.Logger
import org.jetbrains.annotations.ApiStatus
import kotlin.properties.Delegates

open class ModuleDefinitionBase(
	nameUnchcked: String,
) : ModuleDefinition {

	companion object {
		val NAME_PATTERN = Regex("^[a-z0-9_]+$")

		private fun normalize(nameUnchecked: String): String {
			if(nameUnchecked matches NAME_PATTERN) {
				return nameUnchecked
			}

			val normalized = nameUnchecked.trim().lowercase()
				.replace('-', '_')
				.replace("\\s+".toRegex(), "_")
			if(normalized matches NAME_PATTERN) {
				ModuleManager.log.warn("Invalid module name {} has been normalized to {}", nameUnchecked, normalized)
				return normalized
			}

			error("Invalid module name: $nameUnchecked, required to match the REGEX $NAME_PATTERN")
		}
	}

	override val name: String = normalize(nameUnchcked)

	override var enabled: Boolean by Delegates.observable(getInitialEnabledState()) { _, oldValue, newValue ->
		if(oldValue != newValue) {
			// update config value
			config.get("modules", name, true).set(newValue)
			config.save()

			// run callback
			when(newValue) {
				true -> onEnabled()
				false -> onDisabled()
			}
		}
	}

	private val mixinCache by lazy { gatherMixinsToApply() }

	protected val log: Logger get() = ModuleManager.log
	protected val config: Configuration get() = ModuleManager.generalConfiguration

	final override fun getMixinsToApply(): Set<String> {
		// `enabled` may be changed during gathering, so we need to run it once before enabled check.
		mixinCache
		return if(enabled) mixinCache else emptySet()
	}

	protected open fun getInitialEnabledState(): Boolean {
		return ModuleManager.generalConfiguration.getBoolean(name, "modules", true, "Is this module enabled?")
	}

	@ApiStatus.OverrideOnly
	protected open fun onEnabled() {
	}

	@ApiStatus.OverrideOnly
	protected open fun onDisabled() {
	}

	/**
	 * Gather the mixins to apply for this module.
	 * The version checks are also should be done before returning.
	 *
	 * It will be called only once.
	 */
	protected open fun gatherMixinsToApply(): Set<String> {
		return emptySet()
	}

	protected fun buildMixinClassSet(block: MixinClassGather.() -> Unit): Set<String> {
		ModuleManager.log.info("Gathering mixins for module $name")
		return MixinClassGather(this).apply(block).build()
	}

}
