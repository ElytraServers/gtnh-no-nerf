package cn.elytra.mod.gtnn.mod_v2.util

import cpw.mods.fml.common.versioning.ComparableVersion

operator fun ComparableVersion.compareTo(other: String): Int {
	return this.compareTo(ComparableVersion(other))
}

infix fun ComparableVersion.gte(other: String): Boolean {
	return this >= ComparableVersion(other)
}

infix fun ComparableVersion.lte(other: String): Boolean {
	return this <= ComparableVersion(other)
}

infix fun ComparableVersion.lt(other: String): Boolean {
	return this < ComparableVersion(other)
}
