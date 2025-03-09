package org.mtr.model.render.shader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BooleanSupplier;

public final class ModShaderHandler {

	private static InternalHandler internalHandler;

	private static final String IRIS_PREFIX = "net.irisshaders";
	private static final String IRIS_CLASS = IRIS_PREFIX + ".iris.api.v0.IrisApi";

	private static final String OPTIFINE_PREFIX = "net.optifine";
	private static final String OPTIFINE_CLASS = OPTIFINE_PREFIX + ".shaders.Shaders";

	public static InternalHandler getInternalHandler() {
		if (internalHandler == null) {
			internalHandler = new InternalHandler();

			try {
				final Class<?> ignored = Class.forName(IRIS_CLASS);
				internalHandler = new Iris();
			} catch (Exception ignored) {
			}

			try {
				final Class<?> ignored = Class.forName(OPTIFINE_CLASS);
				internalHandler = new Optifine();
			} catch (Exception ignored) {
			}
		}

		return internalHandler;
	}

	public static boolean renderingShadows() {
		for (final StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
			final String className = stackTraceElement.getClassName();
			if (className.startsWith(IRIS_PREFIX) || className.startsWith(OPTIFINE_PREFIX)) {
				return true;
			}
		}
		return false;
	}

	public static class InternalHandler {

		public boolean noShaderPackInUse() {
			return true;
		}
	}

	private static class Iris extends InternalHandler {

		private final BooleanSupplier shadersEnabledSupplier;

		private Iris() {
			shadersEnabledSupplier = createShadersEnabledSupplier();
		}

		@Override
		public boolean noShaderPackInUse() {
			return !shadersEnabledSupplier.getAsBoolean();
		}

		private static BooleanSupplier createShadersEnabledSupplier() {
			try {
				Class<?> irisApiClass = Class.forName(IRIS_CLASS);
				Object irisApiInstance = irisApiClass.getMethod("getInstance").invoke(null);
				Method fnIsShaderPackInUse = irisApiClass.getMethod("isShaderPackInUse");
				return () -> {
					try {
						return (Boolean) fnIsShaderPackInUse.invoke(irisApiInstance);
					} catch (Exception ignored) {
						return false;
					}
				};
			} catch (Exception ignored) {
				return () -> false;
			}
		}
	}

	private static class Optifine extends InternalHandler {

		private final BooleanSupplier shadersEnabledSupplier;

		private Optifine() {
			shadersEnabledSupplier = createShadersEnabledSupplier();
		}

		@Override
		public boolean noShaderPackInUse() {
			return !shadersEnabledSupplier.getAsBoolean();
		}

		private static BooleanSupplier createShadersEnabledSupplier() {
			try {
				Class<?> ofShaders = Class.forName(OPTIFINE_CLASS);
				Field field = ofShaders.getDeclaredField("activeProgramID");
				// field.setAccessible(true);
				return () -> {
					try {
						return (int) field.get(null) != 0;
					} catch (IllegalAccessException ignored) {
						return false;
					}
				};
			} catch (Exception ignored) {
				return () -> false;
			}
		}
	}
}
