package mapper;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public abstract class BlockEntityRendererMapper<T extends BlockEntity> implements BlockEntityRenderer<T> {

	public BlockEntityRendererMapper(BlockEntityRenderDispatcher dispatcher) {
	}
}
