package cn.taskeren.gtnn.util;

import java.util.function.Supplier;

public class Lazy<T> {

	private final Supplier<T> sup;

	private volatile int state;

	private T value;
	private Exception lastException;

	private static final int S_PENDING = 0;
	private static final int S_COMPLETE = 1;
	private static final int S_FAILURE = 2;

	public Lazy(Supplier<T> sup) {
		this.sup = sup;
	}

	public Lazy(UnsafeSupplier<T> sup) {
		this((Supplier<T>) sup);
	}

	private void internalUpdateValue() {
		try {
			value = sup.get();
			state = S_COMPLETE;
		} catch(Exception ex) {
			lastException = ex;
			state = S_FAILURE;
		}
	}

	public T get() {
		if(state == S_PENDING) {
			synchronized(this) {
				if(state == S_PENDING) {
					internalUpdateValue();
				}
			}
		}

		return value;
	}

	public boolean isSuccess() {
		if(state == S_PENDING) {
			synchronized(this) {
				if(state == S_PENDING) {
					internalUpdateValue();
				}
			}
		}

		return state == S_COMPLETE;
	}

	public Exception getLastException() {
		return lastException;
	}

}
