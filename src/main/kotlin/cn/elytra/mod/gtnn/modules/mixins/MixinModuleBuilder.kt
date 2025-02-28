package cn.elytra.mod.gtnn.modules.mixins

import net.minecraftforge.common.config.Configuration

class MixinModuleBuilder(private val id: String) {

	private val mixinClassesToApply: MutableSet<String> = mutableSetOf()
	private val dependsOnTargetMods: MutableList<TargetMod> = mutableListOf()

	private val dynamicMixinClassesToApply: MutableList<MutableSet<String>.() -> Unit> = mutableListOf()

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

	/**
	 * Given [mixinClasses] will be constantly added when gathering Mixin classes, when this module is both
	 * [IMixinModule.isEnabled] and [IMixinModule.canApplyMixins].
	 *
	 * @see addMixinDynamic
	 */
	fun addMixin(vararg mixinClasses: String) {
		mixinClassesToApply.addAll(mixinClasses)
	}

	/**
	 * Given [block] will be called when gathering Mixin classes, when this module is both
	 * [IMixinModule.isEnabled] and [IMixinModule.canApplyMixins].
	 *
	 * You need to add the Mixin classes to the receiver.
	 */
	fun addMixinDynamic(block: MutableSet<String>.() -> Unit) {
		dynamicMixinClassesToApply.add(block)
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

	fun onConfigLoad(block: (config: Configuration, categoryName: String) -> Unit) {
		onConfigLoad = block
	}

	fun build(): IMixinModule {
		return object : IMixinModule {
			override val id: String = this@MixinModuleBuilder.id
			override val mixins: Set<String>
				get() = buildSet {
					addAll(mixinClassesToApply)
					dynamicMixinClassesToApply.forEach {
						it(this)
					}
				}

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

			override fun toString(): String {
				return "MixinModuleBuilder\$Generated(id=$id)"
			}
		}
	}
}

@Suppress("FunctionName")
fun MixinModuleBuilder(id: String, block: MixinModuleBuilder.() -> Unit): IMixinModule =
	MixinModuleBuilder(id).apply(block).build()
