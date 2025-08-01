package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.text.Text;
import org.mtr.config.Config;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.generated.resource.RailResourceSchema;
import org.mtr.model.ModelLoaderBase;
import org.mtr.model.NewOptimizedModel;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public final class RailResource extends RailResourceSchema implements StoredModelResourceBase {

	public final boolean shouldPreload;
	private final ModelLoaderBase modelLoaderBase;
	private final Supplier<Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> modelSupplier;

	public RailResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		modelLoaderBase = VehicleModel.getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		modelSupplier = modelLoaderBase::get;
	}

	/**
	 * Used to create the default rail
	 */
	public RailResource(String id, String name, ResourceProvider resourceProvider) {
		super(id, name, "777777", "", "", false, 0, 0, resourceProvider);
		shouldPreload = false;
		modelLoaderBase = VehicleModel.getModelLoaderBase(modelResource, textureResource, resourceProvider, flipTextureV);
		modelSupplier = modelLoaderBase::get;
	}

	@Override
	@Nullable
	public Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>> getOptimizedModel() {
		return modelSupplier.get();
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

	public double getRepeatInterval() {
		return repeatInterval;
	}

	public double getModelYOffset() {
		return modelYOffset;
	}

	public static String getIdWithoutDirection(String id) {
		return id.endsWith("_1") || id.endsWith("_2") ? id.substring(0, id.length() - 2) : id;
	}
}
