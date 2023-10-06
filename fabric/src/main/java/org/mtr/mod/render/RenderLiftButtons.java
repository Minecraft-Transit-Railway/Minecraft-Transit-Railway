package org.mtr.mod.render;

import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;

public class RenderLiftButtons extends BlockEntityRenderer<BlockLiftButtons.BlockEntity> implements IGui, IBlock {

	public RenderLiftButtons(Argument dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockLiftButtons.BlockEntity entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		// TODO
	}
}
