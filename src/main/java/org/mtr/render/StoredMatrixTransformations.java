package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.function.Consumer;

public class StoredMatrixTransformations {

	public final boolean useDefaultOffset;
	private final ObjectArrayList<Consumer<PoseStack>> transformations = new ObjectArrayList<>();
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

	public void add(Consumer<PoseStack> transformation) {
		transformations.add(transformation);
	}

	public void add(StoredMatrixTransformations storedMatrixTransformations) {
		transformations.addAll(storedMatrixTransformations.transformations);
	}

	public void transform(PoseStack matrixStack, Vec3 offset) {
		matrixStack.pushPose();
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
