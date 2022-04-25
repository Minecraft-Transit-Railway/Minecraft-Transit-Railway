package mtr;

import java.util.function.Supplier;

public class RegistryObject<T> {

	private T object;
	private final Supplier<T> supplier;

	public RegistryObject(Supplier<T> supplier) {
		if (Registry.isFabric()) {
			object = supplier.get();
		}
		this.supplier = supplier;
	}

	public T register() {
		if (!Registry.isFabric()) {
			object = supplier.get();
		}
		return object;
	}

	public T get() {
		return object;
	}
}
