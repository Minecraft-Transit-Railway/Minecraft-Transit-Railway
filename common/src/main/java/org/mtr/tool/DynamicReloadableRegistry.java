package org.mtr.tool;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Represents a registry that can be dynamically added to. Registered instances can be recreated by calling {@link DynamicReloadableRegistry#reload()}, for example during a resource reload event.
 * A static instance should be created for any extending classes.
 *
 * @param <T> the actual type of the data
 * @param <U> the input type used to create the data instance
 * @param <V> the holder type (extends {@link Holder})
 */
public abstract class DynamicReloadableRegistry<T, U, V extends DynamicReloadableRegistry.Holder<T, U>> {

	private final ObjectArrayList<V> registry = new ObjectArrayList<>();

	/**
	 * Reload the registry, recreating all the values.
	 */
	public final void reload() {
		registry.forEach(Holder::reload);
	}

	/**
	 * New records should be added to the registry with this method.
	 *
	 * @param input the object used to create the record
	 * @return the created {@link Holder}
	 */
	public final V create(U input) {
		final V holder = createHolderInstance(input);
		registry.add(holder);
		return holder;
	}

	protected abstract V createHolderInstance(U input);

	public static abstract class Holder<T, U> {

		private T data;
		private final U input;

		protected Holder(U input) {
			this.input = input;
			reload();
		}

		public final T get() {
			return data;
		}

		protected abstract T create(U input);

		private void reload() {
			data = create(input);
		}
	}
}
