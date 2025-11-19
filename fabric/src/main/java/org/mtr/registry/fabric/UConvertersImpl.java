package org.mtr.registry.fabric;

import gg.essential.universal.UMatrixStack;
import gg.essential.universal.vertex.UBuiltBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.math.MatrixStack;

public final class UConvertersImpl {

	public static MatrixStack convert(UMatrixStack matrixStack) {
		return matrixStack.toMC();
	}

	public static UMatrixStack convert(MatrixStack matrixStack) {
		return new UMatrixStack(matrixStack);
	}

	public static UBuiltBuffer convert(BuiltBuffer builtBuffer) {
		return UBuiltBuffer.wrap(builtBuffer);
	}
}
