package org.mtr.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import gg.essential.universal.UMatrixStack;
import gg.essential.universal.vertex.UBuiltBuffer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.util.math.MatrixStack;

public final class UConverters {

	@ExpectPlatform
	public static MatrixStack convert(UMatrixStack matrixStack) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static UMatrixStack convert(MatrixStack matrixStack) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static UBuiltBuffer convert(BuiltBuffer builtBuffer) {
		throw new AssertionError();
	}
}
