package org.mtr.mod.render;

import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.BlockLiftPanelBase;

public class RenderLiftPanel<T extends BlockLiftPanelBase.BlockEntityBase> extends BlockEntityRenderer<T> {

	private final boolean isOdd;
	private final boolean isFlat;

	public RenderLiftPanel(Argument dispatcher, boolean isOdd, boolean isFlat) {
		super(dispatcher);
		this.isOdd = isOdd;
		this.isFlat = isFlat;
	}

	@Override
	public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		// TODO
	}
}
