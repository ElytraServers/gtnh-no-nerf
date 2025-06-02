package cn.elytra.mod.gtnn.util

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList

inline fun <T> NBTTagList.forEach(getter: NBTTagList.(Int) -> T, block: (T) -> Unit) {
	for(i in 0 until tagCount()) {
		val value = getter(i)
		block(value)
	}
}

inline fun NBTTagCompound(block: NBTTagCompound.() -> Unit): NBTTagCompound {
	return NBTTagCompound().apply(block)
}

inline fun NBTTagList(block: NBTTagList.() -> Unit): NBTTagList {
	return NBTTagList().apply(block)
}
