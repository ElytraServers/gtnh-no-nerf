package cn.taskeren.gtnn.mod.gt5u.util;

import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Locale;

import static gregtech.api.enums.GT_Values.NI;
import static gregtech.api.enums.GT_Values.W;

public enum NNItemList implements IItemContainer {

	Machine_LV_Disassembler,
	Machine_MV_Disassembler,
	Machine_HV_Disassembler,
	Machine_EV_Disassembler,
	Machine_IV_Disassembler,
	Machine_LuV_Disassembler,
	Machine_ZPM_Disassembler,
	Machine_UV_Disassembler,
	Machine_UHV_Disassembler,
	Machine_UEV_Disassembler,
	Machine_UIV_Disassembler,
	Machine_UMV_Disassembler,
	;

	private ItemStack mStack;
	private boolean mHasNotBeenSet;
	private boolean mDeprecated;
	private boolean mWarned;

	NNItemList() {
		mHasNotBeenSet = true;
	}

	@Override
	public IItemContainer set(Item aItem) {
		mHasNotBeenSet = false;
		if(aItem == null) return this;
		ItemStack aStack = new ItemStack(aItem, 1, 0);
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}

	@Override
	public IItemContainer set(ItemStack aStack) {
		mHasNotBeenSet = false;
		mStack = GT_Utility.copyAmount(1, aStack);
		return this;
	}

	@Override
	public Item getItem() {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return null;
		return mStack.getItem();
	}

	@Override
	public Block getBlock() {
		sanityCheck();
		return GT_Utility.getBlockFromItem(getItem());
	}

	@Override
	public final boolean hasBeenSet() {
		return !mHasNotBeenSet;
	}

	@Override
	public boolean isStackEqual(Object aStack) {
		return isStackEqual(aStack, false, false);
	}

	@Override
	public boolean isStackEqual(Object aStack, boolean aWildcard, boolean aIgnoreNBT) {
		if(mDeprecated && !mWarned) {
			new Exception(this + " is now deprecated").printStackTrace(GT_Log.err);
			// warn only once
			mWarned = true;
		}
		if(!(aStack instanceof ItemStack stack) || GT_Utility.isStackInvalid(stack)) return false;
		return GT_Utility.areUnificationsEqual((ItemStack) aStack, aWildcard ? getWildcard(1) : get(1), aIgnoreNBT);
	}

	@Override
	public ItemStack get(long aAmount, Object... aReplacements) {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount((int) aAmount, aReplacements);
		return GT_Utility.copyAmount((int) aAmount, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getWildcard(long aAmount, Object... aReplacements) {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount((int) aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData((int) aAmount, W, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getUndamaged(long aAmount, Object... aReplacements) {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount((int) aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData((int) aAmount, 0, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getAlmostBroken(long aAmount, Object... aReplacements) {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount((int) aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData((int) aAmount, mStack.getMaxDamage() - 1, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public ItemStack getWithName(long aAmount, String aDisplayName, Object... aReplacements) {
		ItemStack rStack = get(1, aReplacements);
		if(GT_Utility.isStackInvalid(rStack)) return NI;

		// CamelCase alphanumeric words from aDisplayName
		StringBuilder tCamelCasedDisplayNameBuilder = new StringBuilder();
		final String[] tDisplayNameWords = aDisplayName.split("\\W");
		for(String tWord : tDisplayNameWords) {
			if(tWord.length() > 0) tCamelCasedDisplayNameBuilder.append(tWord.substring(0, 1).toUpperCase(Locale.US));
			if(tWord.length() > 1) tCamelCasedDisplayNameBuilder.append(tWord.substring(1).toLowerCase(Locale.US));
		}
		if(tCamelCasedDisplayNameBuilder.length() == 0) {
			// CamelCased DisplayName is empty, so use hash of aDisplayName
			tCamelCasedDisplayNameBuilder.append(((Long) (long) aDisplayName.hashCode()));
		}

		// Construct a translation key from UnlocalizedName and CamelCased DisplayName
		final String tKey = rStack.getUnlocalizedName() + ".with." + tCamelCasedDisplayNameBuilder + ".name";

		rStack.setStackDisplayName(GT_LanguageManager.addStringLocalization(tKey, aDisplayName));
		return GT_Utility.copyAmount((int) aAmount, rStack);
	}

	@Override
	public ItemStack getWithCharge(long aAmount, int aEnergy, Object... aReplacements) {
		ItemStack rStack = get(1, aReplacements);
		if(GT_Utility.isStackInvalid(rStack)) return null;
		GT_ModHandler.chargeElectricItem(rStack, aEnergy, Integer.MAX_VALUE, true, false);
		return GT_Utility.copyAmount((int) aAmount, rStack);
	}

	@Override
	public ItemStack getWithDamage(long aAmount, long aMetaValue, Object... aReplacements) {
		sanityCheck();
		if(GT_Utility.isStackInvalid(mStack)) return GT_Utility.copyAmount((int) aAmount, aReplacements);
		return GT_Utility.copyAmountAndMetaData((int) aAmount, aMetaValue, GT_OreDictUnificator.get(mStack));
	}

	@Override
	public IItemContainer registerOre(Object... aOreNames) {
		sanityCheck();
		for(Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, get(1));
		return this;
	}

	@Override
	public IItemContainer registerWildcardAsOre(Object... aOreNames) {
		sanityCheck();
		for(Object tOreName : aOreNames) GT_OreDictUnificator.registerOre(tOreName, getWildcard(1));
		return this;
	}

	/**
	 * Returns the internal stack. This method is unsafe. It's here only for quick operations. DON'T CHANGE THE RETURNED
	 * VALUE!
	 */
	public ItemStack getInternalStack_unsafe() {
		return mStack;
	}

	private void sanityCheck() {
		if(mHasNotBeenSet)
			throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
		if(mDeprecated && !mWarned) {
			new Exception(this + " is now deprecated").printStackTrace(GT_Log.err);
			// warn only once
			mWarned = true;
		}
	}

}
