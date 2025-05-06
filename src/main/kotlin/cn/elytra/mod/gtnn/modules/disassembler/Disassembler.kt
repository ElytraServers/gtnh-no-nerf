package cn.elytra.mod.gtnn.modules.disassembler

import cn.elytra.mod.gtnn.mod_v2.ModuleDefinitionBase
import cn.elytra.mod.gtnn.mod_v2.util.gte
import cn.elytra.mod.gtnn.util.VoltageIndexedMap
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLLoadCompleteEvent
import net.minecraft.item.ItemStack

object Disassembler : ModuleDefinitionBase("disassembler") {

	private var t1MteId = 451
	private var t6MteId = 11160

	val disassemblers = VoltageIndexedMap<ItemStack>()

	override fun reloadConfig() {
		t1MteId = config.getInt(
			"t1MteId",
			"disassembler",
			t1MteId,
			0,
			32767,
			"the mte id of disassemblers from T1-T5; you should ensure that from value to value+4 are all available."
		)
		t6MteId = config.getInt(
			"t6MteId",
			"disassembler",
			t6MteId,
			0,
			32767,
			"the mte id of disassembler from T6-T12; you should ensure that from value to value+6 are all available."
		)
	}

	override fun onFMLInit(e: FMLInitializationEvent) {
		for((tier, mId) in (1..12) zip (t1MteId..t1MteId + 4) + (t6MteId..t6MteId + 6)) {
			val stackForm = MTEDisassembler(
				mId,
				tier,
			).getStackForm(1)
			disassemblers.put(tier, stackForm)
		}
	}

	override fun onFMLLoadComplete(e: FMLLoadCompleteEvent) {
		DisassemblerHelper.loadAssemblerRecipesToDisassembler()
		ReversedRecipeRegistry.registerAllReversedRecipes()
	}

	override fun gatherMixinsToApply(): Set<String> {
		return buildMixinClassSet {
			// shaped
			add("gt5u.DisassemblerReversedRecipe_GTShapedRecipe_Mixin")
			// shapeless
			if(isModVersionValid("gregtech") { it gte "5.09.51.155" }) {
				add("gt5u.DisassemblerReversedRecipe_GTShapelessRecipe_Mixin")
			} else {
				log.info("Ignored GregTech shapeless recipes because of GT5-Unofficial#3138 broke the mixin.")
			}
		}
	}

}
