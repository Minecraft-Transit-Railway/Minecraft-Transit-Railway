package org.mtr.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.tool.Drawing;

import java.util.function.Consumer;

public abstract class SpecialSignRendererBase<T> {

	public abstract void render(
			Drawing textureDrawing, ObjectArrayList<Consumer<Drawing>> deferredRenders,
			float x, float y, float zOffset,
			float signSize, ObjectArrayList<T> dataList,
			boolean flipTexture, boolean flipText, boolean small, String customText,
			float totalSpace, boolean renderPlaceholder
	);
}
