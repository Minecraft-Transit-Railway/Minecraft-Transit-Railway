package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassBase;
import mtr.block.BlockPSDTop;
import mtr.block.BlockPlatformRail;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import mtr.model.PSDTopModel;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public class RenderPSDTop extends BlockEntityRenderer<BlockPSDTop.TileEntityPSDTop> {

	private static final int BASE_SCALE = 320;

	private static final float SIDE_PADDING = 0.25F;
	private static final float TOP_PADDING = 0.5F;
	private static final float BOTTOM_PADDING = 0.125F;

	private static final float BOTTOM_LINE_POSITION = 0.90625F;
	private static final float BOTTOM_LINE_HEIGHT = 0.03125F;


	public RenderPSDTop(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockPSDTop.TileEntityPSDTop entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final World world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = state.get(BlockPSDTop.FACING);
		final boolean airLeft = state.get(BlockPSDTop.AIR_LEFT);
		final boolean airRight = state.get(BlockPSDTop.AIR_RIGHT);
		final boolean isDoor = world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGDoorBase;

		final BlockPos platformPos = findPlatformPos(world, pos);
		if (platformPos == null) {
			return;
		}
		final List<Triple<Integer, Integer, List<String>>> routeData = ClientData.platformToRoute.get(platformPos);
		if (routeData == null) {
			return;
		}
		final int routeCount = routeData.size();

		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(-0.5, 0, 0.125 - IGui.SMALL_OFFSET * 2);

		matrices.push();
		final float lineHeightSmall = BOTTOM_LINE_HEIGHT / routeCount;
		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).getLeft();
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, airLeft ? 0.625F : 0, BOTTOM_LINE_POSITION + lineHeightSmall * i, airRight ? 0.375F : 1, BOTTOM_LINE_POSITION + lineHeightSmall * (i + 1), 0, routeColor, light);
			if (airLeft) {
				IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, PSDTopModel.END_FRONT_OFFSET, BOTTOM_LINE_POSITION + lineHeightSmall * i, -0.625F - PSDTopModel.END_FRONT_OFFSET, 0.75F + PSDTopModel.END_FRONT_OFFSET, BOTTOM_LINE_POSITION + lineHeightSmall * (i + 1), 0.125F - PSDTopModel.END_FRONT_OFFSET, routeColor, light);
			}
			if (airRight) {
				IGui.drawRectangle(matrices.peek().getModel(), vertexConsumers, 0.25F - PSDTopModel.END_FRONT_OFFSET, BOTTOM_LINE_POSITION + lineHeightSmall * i, 0.125F - PSDTopModel.END_FRONT_OFFSET, 1 - PSDTopModel.END_FRONT_OFFSET, BOTTOM_LINE_POSITION + lineHeightSmall * (i + 1), -0.625F - PSDTopModel.END_FRONT_OFFSET, routeColor, light);
			}
		}
		matrices.pop();

		if (isDoor) {
			// TODO
		} else if (!airLeft && !airRight && state.get(BlockPSDTop.SIDE) == BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.LEFT) {
			final int glassLength = getGlassLength(world, pos, facing);
			if (glassLength > 1) {
				final int scaleSmaller = BASE_SCALE * routeCount;
				final float routeHeight = (1 - TOP_PADDING - BOTTOM_PADDING) / routeCount;
				for (int i = 0; i < routeCount; i++) {
					final int routeColor = routeData.get(i).getLeft();
					final int currentStationIndex = routeData.get(i).getMiddle();
					final List<String> stationNames = routeData.get(i).getRight();
					final RouteRenderer routeRenderer = new RouteRenderer(SIDE_PADDING, glassLength - SIDE_PADDING, TOP_PADDING + routeHeight * i, TOP_PADDING + routeHeight * (i + 1), scaleSmaller, routeColor, currentStationIndex, stationNames, false);
					routeRenderer.render(matrices, vertexConsumers, light);
				}
			}
		}

		matrices.pop();
	}

	private BlockPos findPlatformPos(WorldAccess world, BlockPos pos) {
		for (int y = 2; y <= 3; y++) {
			for (int x = 1; x <= 2; x++) {
				final BlockPos checkPos = pos.down(y).offset(world.getBlockState(pos).get(BlockPSDTop.FACING), x);
				if (world.getBlockState(checkPos).getBlock() instanceof BlockPlatformRail) {
					return BlockPlatformRail.getPlatformPos1(world, checkPos);
				}
			}
		}
		return null;
	}

	private int getGlassLength(WorldAccess world, BlockPos pos, Direction facing) {
		int glassLength = 1;

		while (true) {
			final BlockState state = world.getBlockState(pos.offset(facing.rotateYClockwise(), glassLength));
			if (state.getBlock() instanceof BlockPSDTop && !state.get(BlockPSDTop.AIR_LEFT) && !state.get(BlockPSDTop.AIR_RIGHT) && state.get(BlockPSDTop.SIDE) != BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.LEFT) {
				glassLength++;
				if (state.get(BlockPSDTop.SIDE) == BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.RIGHT) {
					break;
				}
			} else {
				break;
			}
		}

		return glassLength;
	}
}
