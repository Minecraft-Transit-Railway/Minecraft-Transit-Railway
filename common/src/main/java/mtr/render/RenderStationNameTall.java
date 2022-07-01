package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameTallBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RenderStationNameTall<T extends BlockStationNameTallBase.TileEntityStationNameTallBase> extends RenderStationNameBase<T> {

	private static final float WIDTH = 0.6875F;
	private static final float HEIGHT = 1.5F;

	public RenderStationNameTall(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int stationColor, int color, int light) {
		if (IBlock.getStatePropertySafe(state, BlockStationNameTallBase.THIRD) == IBlock.EnumThird.MIDDLE) {
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getTallStationName(color, stationName, stationColor, WIDTH / HEIGHT).resourceLocation)), -WIDTH / 2, -HEIGHT / 2, WIDTH, HEIGHT, 0, 0, 1, 1, facing, ARGB_WHITE, light);
		}
	}
}
