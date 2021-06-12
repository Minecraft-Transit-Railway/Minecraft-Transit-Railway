package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.block.IPropagateBlock;
import mtr.data.IGui;
import mtr.gui.IDrawing;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class RenderStationNameEntrance extends RenderStationNameBase<BlockStationNameEntrance.TileEntityStationNameEntrance> implements IPropagateBlock {

	public RenderStationNameEntrance(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumerProvider.Immediate immediate, String stationName, int color, int light) {
		final WorldAccess world = entity.getWorld();
		final BlockPos pos = entity.getPos();

		if (world == null) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
		final int propagateProperty = IBlock.getStatePropertySafe(world, pos, PROPAGATE_PROPERTY);
		final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
		final int length = getLength(world, pos);
		IDrawing.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, immediate, IGui.addToStationName(stationName, "", "", "站", " Station"), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (length + logoSize) / 2 - 0.5F, 0, length - logoSize, logoSize - 0.125F, 40 / logoSize, propagateProperty < 2 ? ARGB_WHITE : ARGB_BLACK, false, MAX_LIGHT_GLOWING, ((x1, y1, x2, y2) -> {
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new Identifier("mtr:textures/sign/logo.png")));
			IDrawing.drawTexture(matrices, vertexConsumer, x1 - logoSize, -logoSize / 2, logoSize, logoSize, facing, MAX_LIGHT_GLOWING);
		}));
	}

	private int getLength(WorldAccess world, BlockPos pos) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.FACING);

		int length = 1;
		while (true) {
			final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), length));
			if (state.getBlock() instanceof BlockStationNameEntrance) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
