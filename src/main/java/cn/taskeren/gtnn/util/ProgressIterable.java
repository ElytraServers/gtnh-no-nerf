package cn.taskeren.gtnn.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ProgressIterable<T> implements Iterable<T> {

	public class ProgressIterator implements Iterator<T> {

		private final Iterator<T> delegate;

		public Predicate<Integer> shouldNotify;
		public Consumer<Integer> onNotify;

		private transient int nextCount;

		public ProgressIterator(Iterator<T> delegate) {
			this(delegate, (i) -> false, (i) -> {
			});
		}

		public ProgressIterator(Iterator<T> delegate, Predicate<Integer> shouldNotify, Consumer<Integer> onNotify) {
			this.delegate = delegate;
			this.shouldNotify = shouldNotify;
			this.onNotify = onNotify;
		}

		@Override
		public boolean hasNext() {
			return delegate.hasNext();
		}

		@Override
		public T next() {
			var count = nextCount++;
			if(shouldNotify.test(count)) {
				onNotify.accept(count);
			}

			return delegate.next();
		}

		@Override
		public void forEachRemaining(Consumer<? super T> action) {
			delegate.forEachRemaining(action);
		}

		@Override
		public void remove() {
			delegate.remove();
		}
	}

	private final Iterable<T> delegate;

	public Predicate<Integer> shouldNotify;
	public Consumer<Integer> onNotify;

	public ProgressIterable(Iterable<T> delegate) {
		this(delegate, (i) -> false, (i) -> {});
	}

	public ProgressIterable(Iterable<T> delegate, Predicate<Integer> shouldNotify, Consumer<Integer> onNotify) {
		this.delegate = delegate;
		this.shouldNotify = shouldNotify;
		this.onNotify = onNotify;
	}

	@NotNull
	@Override
	public Iterator<T> iterator() {
		return new ProgressIterator(delegate.iterator(), shouldNotify, onNotify);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		delegate.forEach(action);
	}

	@Override
	public Spliterator<T> spliterator() {
		return delegate.spliterator();
	}

	public static <T> ProgressIterable<T> ofCollection(Collection<T> collection, int invokeGap, Consumer<Integer> callback) {
		return new ProgressIterable<>(collection, (i) -> i % invokeGap == 0, callback);
	}
}
