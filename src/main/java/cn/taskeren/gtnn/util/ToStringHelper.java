package cn.taskeren.gtnn.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public class ToStringHelper {

	private ToStringHelper() {
		throw new UnsupportedOperationException();
	}

	public static String vecToString(int x, int y, int z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

	public static String vecToString(double x, double y, double z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

	public static String vecToString(float x, float y, float z) {
		return "BlockPos(x="+x+", y="+y+", z="+z+")";
	}

	public static String stringIteratorListToString(Iterator<String> strIterator) {
		var sb = new StringBuilder("[");
		while(strIterator.hasNext()) {
			sb.append(strIterator.next());
			if(strIterator.hasNext()) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static <T> String objectArrayToString(T[] array, Function<T, String> toStringMapper) {
		return stringIteratorListToString(Arrays.stream(array).map(toStringMapper).iterator());
	}

	public static <T> String objectListToString(Collection<T> list, Function<T, String> toStringMapper) {
		return stringIteratorListToString(list.stream().map(toStringMapper).iterator());
	}

	public static String advItemStackToString(ItemStack is) {
		if(is == null) return "INVALID ITEM-STACK: NULL STACK";
		if(is.getItem() == null) return "INVALID ITEM-STACK: NULL ITEM";

		var stack = is.stackSize;
		var unl10nName = is.getItem().getUnlocalizedName();
		var id = GameData.getItemRegistry().getId(is.getItem());
		var damage = is.getItemDamage();
		var nbt = is.getTagCompound();
		var nbtStr = nbt != null ? nbt.toString() : "{NBT-NULL}";
		return stack + "x " + unl10nName + "(" + id + ") @ " + damage + nbtStr;
	}



}
