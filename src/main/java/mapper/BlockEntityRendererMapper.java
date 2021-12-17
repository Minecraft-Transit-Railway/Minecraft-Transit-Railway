package mapper;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public abstract class BlockEntityRendererMapper<T extends BlockEntityMapper> extends BlockEntityRenderer<T> {

	public BlockEntityRendererMapper(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
}
