package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;

import java.util.function.Consumer;

public class StoredMatrixTransformations {

	public final boolean useDefaultOffset;
	private final ObjectArrayList<Consumer<GraphicsHolder>> transformations = new ObjectArrayList<>();
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

	public void add(Consumer<GraphicsHolder> transformation) {
		transformations.add(transformation);
	}

	public void add(StoredMatrixTransformations storedMatrixTransformations) {
		transformations.addAll(storedMatrixTransformations.transformations);
	}

	public void transform(GraphicsHolder graphicsHolder, Vector3d offset) {
		graphicsHolder.push();
		if (useDefaultOffset) {
			graphicsHolder.translate(initialTranslateX - offset.getXMapped(), initialTranslateY - offset.getYMapped(), initialTranslateZ - offset.getZMapped());
		}
		transformations.forEach(transformation -> transformation.accept(graphicsHolder));
	}

	public StoredMatrixTransformations copy() {
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(useDefaultOffset, initialTranslateX, initialTranslateY, initialTranslateZ);
		storedMatrixTransformations.transformations.addAll(transformations);
		return storedMatrixTransformations;
	}
}
