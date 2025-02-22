package cn.elytra.mod.gtnn.modules.mixins

import net.minecraftforge.common.config.Configuration

class MixinModuleBuilder(private val id: String) {

	private val mixinClassesToApply: MutableSet<String> = mutableSetOf()
	private val dependsOnTargetMods: MutableList<TargetMod> = mutableListOf()

	private var enabledByDefault: Boolean = false
	private var mustBeEnabled: Boolean = false

	/**
	 * The configuration category name
	 */
	private var configCategoryName: String? = null

	private var configCategoryDesc: String? = null

	/**
	 * Will be called when the configuration is loaded.
	 */
	private var onConfigLoad: ((config: Configuration, categoryName: String) -> Unit)? = null

	/**
	 * Will be called when [IMixinModule.canApplyMixins] if set.
	 */
	private var extraRequirementsCheck: (() -> Boolean)? = null

	fun addMixin(vararg mixinClasses: String) {
		mixinClassesToApply.addAll(mixinClasses)
	}

	fun dependsOn(vararg targetMods: TargetMod) {
		dependsOnTargetMods.addAll(targetMods)
	}

	fun enabledByDefault() {
		this.enabledByDefault = true
	}

	fun mustBeEnabled() {
		this.mustBeEnabled = true
	}

	fun setConfigCategoryName(categoryName: String) {
		configCategoryName = categoryName
	}

	fun setConfigCategoryDesc(categoryDesc: String) {
		configCategoryDesc = categoryDesc
	}

	fun build(): IMixinModule {
		return object : IMixinModule {
			override val id: String = this@MixinModuleBuilder.id
			override val mixins: Set<String> = mixinClassesToApply

			private var enabled: Boolean = enabledByDefault

			override fun isEnabled(): Boolean {
				return mustBeEnabled || enabled
			}

			override fun readConfig(config: Configuration) {
				val category = configCategoryName ?: "mixin-${id}"

				// get "enabled" boolean property, but ignored if "mustEnabled"
				val mustBeEnabled = mustBeEnabled
				if(!mustBeEnabled) {
					enabled = config.getBoolean("enabled", category, enabled, "true to enable the mixin module")
				}

				// add desc to the category
				var configCategoryDesc = configCategoryDesc
				if(mustBeEnabled) {
					configCategoryDesc = configCategoryDesc.orEmpty() + "\n" + "This module must be enabled!"
				}
				if(configCategoryDesc != null) {
					config.getCategory(category).comment = configCategoryDesc
				}

				// run extra configuration loading
				onConfigLoad?.invoke(config, category)
			}

			override fun canApplyMixins(): Boolean {
				val extraRequirementsCheck = extraRequirementsCheck
				return dependsOnTargetMods.all { it.isLoaded }
					&& (extraRequirementsCheck == null || extraRequirementsCheck())
			}
		}
	}
}

@Suppress("FunctionName")
fun MixinModuleBuilder(id: String, block: MixinModuleBuilder.() -> Unit): IMixinModule =
	MixinModuleBuilder(id).apply(block).build()
