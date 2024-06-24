package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StoredMatrixTransformations {

	private final List<Consumer<PoseStack>> transformations = new ArrayList<>();

	public void add(Consumer<PoseStack> transformation) {
		transformations.add(transformation);
	}

	public void transform(PoseStack matrices) {
		matrices.pushPose();
		transformations.forEach(transformation -> transformation.accept(matrices));
	}

	public StoredMatrixTransformations copy() {
		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.transformations.addAll(transformations);
		return storedMatrixTransformations;
	}
}
