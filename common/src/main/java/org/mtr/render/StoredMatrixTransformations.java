package org.mtr.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class StoredMatrixTransformations {

	public final boolean useDefaultOffset;
	private final ObjectArrayList<Consumer<MatrixStack>> transformations = new ObjectArrayList<>();
	private final double initialTranslateX;
	private final double initialTranslateY;
	private final double initialTranslateZ;

	public StoredMatrixTransformations() {
		useDefaultOffset = false;
		initialTranslateX = 0;
		initialTranslateY = 0;
		initialTranslateZ = 0;
	}

	public StoredMatrixTransformations(double initialTranslateX, double initialTranslateY, double initialTranslateZ) {
		useDefaultOffset = true;
		this.initialTranslateX = initialTranslateX;
		this.initialTranslateY = initialTranslateY;
		this.initialTranslateZ = initialTranslateZ;
	}

	private StoredMatrixTransformations(boolean useDefaultOffset, double initialTranslateX, double initialTranslateY, double initialTranslateZ) {
		this.useDefaultOffset = useDefaultOffset;
		this.initialTranslateX = initialTranslateX;
		this.initialTranslateY = initialTranslateY;
		this.initialTranslateZ = initialTranslateZ;
	}

	public void add(Consumer<MatrixStack> transformation) {
		transformations.add(transformation);
	}

	public void add(StoredMatrixTransformations storedMatrixTransformations) {
		transformations.addAll(storedMatrixTransformations.transformations);
	}

	public void transform(MatrixStack matrixStack, Vec3d offset) {
		matrixStack.push();
		if (useDefaultOffset) {
			matrixStack.translate(initialTranslateX - offset.x, initialTranslateY - offset.y, initialTranslateZ - offset.z);
		}
		transformations.forEach(transformation -> transformation.accept(matrixStack));
	}

	public StoredMatrixTransformations copy() {
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(useDefaultOffset, initialTranslateX, initialTranslateY, initialTranslateZ);
		storedMatrixTransformations.transformations.addAll(transformations);
		return storedMatrixTransformations;
	}
}
