package cn.taskeren.gtnn.mod.gtPlusPlus;

import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;

public class GenericChemExt {

	public static ItemStack mLimpidWaterCatalyst;
	public static ItemStack mFlawlessWaterCatalyst;

	public static void registerItemStacks() {
		mLimpidWaterCatalyst = ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, 29, 1);
		mFlawlessWaterCatalyst = ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, 30, 1);
	}

	public static void registerOreDict() {
		ItemUtils.addItemToOreDictionary(mLimpidWaterCatalyst, "catalystLimpidWater");
		ItemUtils.addItemToOreDictionary(mFlawlessWaterCatalyst, "catalystFlawlessWater");
	}

	/**
	 * @see ItemUtils#isCatalyst(ItemStack)
	 */
	public static boolean isAdditionalCatalyst(ItemStack stack) {
		if(GTUtility.areStacksEqual(stack, mLimpidWaterCatalyst)) {
			return true;
		}
		if(GTUtility.areStacksEqual(stack, mFlawlessWaterCatalyst)) {
			return true;
		}
		return false;
	}

}
