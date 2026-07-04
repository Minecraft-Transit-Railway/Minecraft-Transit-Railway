package org.mtr.registry;

import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.vertex.UBuiltBuffer;

public final class UConverters {

	public static PoseStack convert(UMatrixStack matrixStack) {
		return matrixStack.toMC();
	}

	public static UMatrixStack convert(PoseStack matrixStack) {
		return new UMatrixStack(matrixStack);
	}

	public static UBuiltBuffer convert(MeshData builtBuffer) {
		return UBuiltBuffer.wrap(builtBuffer);
	}
}
