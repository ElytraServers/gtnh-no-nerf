package cn.taskeren.gtnn.util;

import com.google.common.collect.ImmutableList;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KtCandy {

	/**
	 * Equals to {@code run { ... }}. But code block must not throw exceptions.
	 */
	public static <R> R run(Supplier<R> supplier) {
		return supplier.get();
	}

	/**
	 * Equals to {@code run { ... }}.
	 */
	public static <R> R run(UnsafeSupplier<R> supplier) {
		return run((Supplier<? extends R>) supplier);
	}

	/**
	 * Equals to {@code T.apply { ... }}.
	 */
	public static <T> T apply(T t, Consumer<T> consumer) {
		consumer.accept(t);
		return t;
	}

	// collections

	public static <T> ImmutableList<T> buildList(Consumer<ImmutableList.Builder<T>> builder) {
		var b = ImmutableList.<T>builder();
		builder.accept(b);
		return b.build();
	}

}
