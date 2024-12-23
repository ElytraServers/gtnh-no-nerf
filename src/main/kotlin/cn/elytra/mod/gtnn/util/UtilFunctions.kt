package cn.elytra.mod.gtnn.util

internal infix fun IntRange.withOffsetStartBy(offset: Int): Iterator<Pair<Int, Int>> {
	return iterator {
		this@withOffsetStartBy.forEachIndexed { idx, i ->
			yield(Pair(i, idx + offset))
		}
	}
}
