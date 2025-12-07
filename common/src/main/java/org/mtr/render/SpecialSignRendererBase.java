package org.mtr.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.tool.Drawing;

import java.util.function.Consumer;

public abstract class SpecialSignRendererBase<T> {

	public abstract void render(
			Drawing textureDrawing, ObjectArrayList<Consumer<MatrixStack>> deferredRenders,
			float x, float y, float zOffset,
			float signSize, ObjectArrayList<T> dataList,
			boolean flipTexture, boolean flipText, boolean small, String customText, Identifier font,
			float totalSpace, boolean renderPlaceholder
	);
}
