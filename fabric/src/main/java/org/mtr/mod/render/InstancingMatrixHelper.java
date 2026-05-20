package org.mtr.mod.render;

import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public final class InstancingMatrixHelper {

	public static final Vector3d ZERO_OFFSET = new Vector3d(0, 0, 0);
	@Nullable
	private static final Field MATRIX_STACK_FIELD = findMatrixStackField();

	private InstancingMatrixHelper() {
	}

	public static Matrix4f captureMatrix(StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		final MatrixStack matrixStack = new MatrixStack();
		final Matrix4f[] matrixHolder = new Matrix4f[1];
		GraphicsHolder.createInstanceSafe(matrixStack, null, graphicsHolder -> {
			storedMatrixTransformations.transform(graphicsHolder, offset);
			matrixHolder[0] = new Matrix4f(matrixStack.peek().getPositionMatrix());
		});
		return matrixHolder[0] == null ? new Matrix4f() : matrixHolder[0];
	}

	public static Matrix4f captureMatrix(@Nullable GraphicsHolder graphicsHolder, StoredMatrixTransformations storedMatrixTransformations, Vector3d offset) {
		if (graphicsHolder == null || MATRIX_STACK_FIELD == null) {
			return captureMatrix(storedMatrixTransformations, offset);
		}

		boolean pushed = false;
		try {
			final Object matrixStackObject = MATRIX_STACK_FIELD.get(graphicsHolder);
			if (!(matrixStackObject instanceof MatrixStack)) {
				return captureMatrix(storedMatrixTransformations, offset);
			}
			final MatrixStack matrixStack = (MatrixStack) matrixStackObject;
			storedMatrixTransformations.transform(graphicsHolder, offset);
			pushed = true;
			return new Matrix4f(matrixStack.peek().getPositionMatrix());
		} catch (Exception ignored) {
			return captureMatrix(storedMatrixTransformations, offset);
		} finally {
			if (pushed) {
				graphicsHolder.pop();
			}
		}
	}

	@Nullable
	private static Field findMatrixStackField() {
		try {
			final Field field = GraphicsHolder.class.getDeclaredField("matrixStack");
			field.setAccessible(true);
			return field;
		} catch (Exception ignored) {
			return null;
		}
	}
}
