package mtr;

import java.util.function.Supplier;

public class RegistryObject<T> {

	private T object;
	private final Supplier<T> supplier;

	public RegistryObject(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	public T get() {
		if (object == null) {
			object = supplier.get();
		}
		return object;
	}
}
