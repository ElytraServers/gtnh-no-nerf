package cn.elytra.mod.gtnn.util

import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

class ProgressIterable<T>(
	private val delegate: MutableIterable<T>,
	var shouldNotify: Predicate<Int>,
	var onNotify: Consumer<Int>,
) : Iterable<T> {

	class ProgressIterator<T>(
		private val delegate: MutableIterator<T>,
		var shouldNotify: Predicate<Int>,
		var onNotify: Consumer<Int>,
	) : MutableIterator<T> {
		@Transient
		private var nextCount = 0

		override fun hasNext(): Boolean {
			return delegate.hasNext()
		}

		override fun next(): T {
			val count = nextCount++
			if(shouldNotify.test(count)) {
				onNotify.accept(count)
			}

			return delegate.next()
		}

		override fun forEachRemaining(action: Consumer<in T>) {
			super.forEachRemaining(action)
		}

		override fun remove() {
			delegate.remove()
		}
	}

	override fun iterator(): MutableIterator<T> {
		return ProgressIterator(delegate.iterator(), shouldNotify, onNotify)
	}

	override fun forEach(action: Consumer<in T>) {
		delegate.forEach(action)
	}

	override fun spliterator(): Spliterator<T> {
		return delegate.spliterator()
	}

	companion object {

		@JvmStatic
		fun <T> ofCollection(
			collection: MutableCollection<T>,
			invokeGap: Int,
			callback: Consumer<Int>,
		): ProgressIterable<T> {
			return ProgressIterable<T>(collection, Predicate { i -> i % invokeGap == 0 }, callback)
		}
	}
}
