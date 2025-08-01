package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.text.Text;
import org.mtr.client.IDrawing;
import org.mtr.config.Config;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.ObjectResourceSchema;
import org.mtr.model.ModelLoaderBase;
import org.mtr.model.NewOptimizedModel;
import org.mtr.render.StoredMatrixTransformations;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class ObjectResource extends ObjectResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final ModelLoaderBase modelLoaderBase;
	private final Supplier<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> modelSupplier;

	public ObjectResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		modelLoaderBase = VehicleModel.getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		modelSupplier = modelLoaderBase::get;
	}

	@Override
	@Nullable
	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel() {
		return modelSupplier.get();
	}

	@Override
	public void render(StoredMatrixTransformations storedMatrixTransformations, int light) {
		final StoredMatrixTransformations newStoredMatrixTransformations = storedMatrixTransformations.copy();
		newStoredMatrixTransformations.add(matrixStack -> {
					matrixStack.translate(translation.getX(), translation.getY(), translation.getZ());
					IDrawing.rotateXDegrees(matrixStack, (float) rotation.getX());
					IDrawing.rotateYDegrees(matrixStack, (float) rotation.getY());
					IDrawing.rotateZDegrees(matrixStack, (float) rotation.getZ());
					matrixStack.scale(clampNumber(scale.getX()), clampNumber(scale.getY()), clampNumber(scale.getZ()));
//					matrixStack.mirror(mirror.getX(), mirror.getY(), mirror.getZ());
				}
		);
		StoredModelResourceBase.super.render(newStoredMatrixTransformations, light);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return Text.translatable(name).getString();
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	private static float clampNumber(double value) {
		return value <= 0 ? 1 : (float) value;
	}
}
