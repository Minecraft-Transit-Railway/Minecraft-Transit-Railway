package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.tool.Drawing;

import java.util.function.Consumer;

public abstract class SpecialSignRendererBase<T> {

	public abstract void render(
		Drawing textureDrawing, ObjectArrayList<Consumer<PoseStack>> deferredRenders,
		float x, float y, float zOffset,
		float signSize, ObjectArrayList<T> dataList,
		boolean flipTexture, boolean flipText, boolean small, String customText, ResourceLocation font,
		float totalSpace, boolean renderPlaceholder
	);
}
