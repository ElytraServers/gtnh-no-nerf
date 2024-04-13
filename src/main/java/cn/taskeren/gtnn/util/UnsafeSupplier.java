package cn.taskeren.gtnn.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface UnsafeSupplier<T> extends Supplier<T> {

	@Override
	default T get() {
		try {
			return getUnsafe();
		} catch(Exception ex) {
			throw new UnsafeSupplierException(ex);
		}
	}

	T getUnsafe() throws Exception;

	class UnsafeSupplierException extends RuntimeException {
		public UnsafeSupplierException(Throwable cause) {
			super(cause);
		}
	}
}
