package cn.taskeren.gtnn.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class KtCandy {

	public static <R> R run(Supplier<R> supplier) {
		return supplier.get();
	}

	public static <R> R run(UnsafeSupplier<R> supplier) {
		return run((Supplier<? extends R>) supplier);
	}

	public static <T> T apply(T t, Consumer<T> consumer) {
		consumer.accept(t);
		return t;
	}

}
