package org.mtr.registry;

import java.util.function.Supplier;

public final class ObjectHolder<T> {

	private T object;
	private final Supplier<T> supplier;

	public ObjectHolder(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public void create() {
		if (object == null) {
			object = supplier.get();
		}
	}

	public boolean exists() {
		return object != null;
	}

	public T get() {
		create();
		return object;
	}
}
