package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mod.render.RenderVehicles;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;

public final class OptimizedModelWrapper {

	@Nullable
	final OptimizedModel optimizedModel;

	public OptimizedModelWrapper(ObjectArrayList<MaterialGroupWrapper> materialGroupList) {
		optimizedModel = RenderVehicles.useOptimizedRendering() ? new OptimizedModel(materialGroupList.stream().map(materialGroup -> materialGroup.materialGroup).filter(Objects::nonNull).collect(Collectors.toList())) : null;
	}

	public static final class MaterialGroupWrapper {

		@Nullable
		private final OptimizedModel.MaterialGroup materialGroup;

		@MappedMethod
		public MaterialGroupWrapper(OptimizedModel.ShaderType shaderType, Identifier texture) {
			materialGroup = RenderVehicles.useOptimizedRendering() ? new OptimizedModel.MaterialGroup(shaderType, texture) : null;
		}

		@MappedMethod
		public void addCube(ModelPartExtension modelPart, double x, double y, double z, boolean flipped, int light) {
			if (materialGroup != null) {
				materialGroup.addCube(modelPart, x, y, z, flipped, light);
			}
		}
	}
}
