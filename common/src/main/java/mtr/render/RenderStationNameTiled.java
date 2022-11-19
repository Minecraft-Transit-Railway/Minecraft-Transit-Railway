package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class RenderStationNameTiled<T extends BlockStationNameBase.TileEntityStationNameBase> extends RenderStationNameBase<T> {

	private final boolean showLogo;

	public RenderStationNameTiled(BlockEntityRenderDispatcher dispatcher, boolean showLogo) {
		super(dispatcher);
		this.showLogo = showLogo;
	}

	@Override
	protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int stationColor, int color, int light) {
		final int lengthLeft = getLength(world, pos, false);
		final int lengthRight = getLength(world, pos, true);

		if (showLogo) {
			if (lengthLeft == 1) {
				final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
				final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
				IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, stationName), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (lengthRight + logoSize) / 2 - 0.5F, 0, lengthRight - logoSize, logoSize - 0.125F, 40 / logoSize, propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, false, MAX_LIGHT_GLOWING, ((x1, y1, x2, y2) -> {
					final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new ResourceLocation("mtr:textures/sign/logo.png")));
					IDrawing.drawTexture(matrices, vertexConsumer, x1 - logoSize, -logoSize / 2, logoSize, logoSize, facing, MAX_LIGHT_GLOWING);
				}));
			}
		} else {
			final int totalLength = lengthLeft + lengthRight - 1;
			IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getStationName(stationName, totalLength).resourceLocation)), -0.5F, -0.5F, 1, 1, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
		}
	}

	private int getLength(BlockGetter world, BlockPos pos, boolean lookRight) {
		if (world == null) {
			return 1;
		}
		final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
		final Block thisBlock = world.getBlockState(pos).getBlock();

		int length = 1;
		while (true) {
			final Block checkBlock = world.getBlockState(pos.relative(lookRight ? facing.getClockWise() : facing.getCounterClockWise(), length)).getBlock();
			if (checkBlock instanceof BlockStationNameBase && checkBlock == thisBlock) {
				length++;
			} else {
				break;
			}
		}

		return length;
	}
}
