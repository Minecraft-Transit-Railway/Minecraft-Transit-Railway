package mtr.render;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDAPGGlassBase;
import mtr.block.BlockPSDTop;
import mtr.block.BlockPlatformRail;
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

public class RenderPSDTop extends BlockEntityRenderer<BlockPSDTop.TileEntityPSDTop> {

	private static final boolean RIGHT_TO_LEFT = false;

	private static final int BASE_SCALE = 320;
	private static final int DESTINATION_SCALE = 108;

	private static final float SIDE_AND_BOTTOM_PADDING = 0.125F;
	private static final float TOP_PADDING = 0.5F;
	private static final float ARROW_PADDING = 0.0625F;

	private static final float COLOR_STRIP_START = 0.90625F;
	private static final float COLOR_STRIP_END = 0.9375F;

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

		final BlockPos platformPos = findPlatformPos(world, pos);
		if (platformPos == null) {
			return;
		}

		final BlockState state = world.getBlockState(pos);
		final Direction facing = state.get(BlockPSDTop.FACING);
		final boolean airLeft = state.get(BlockPSDTop.AIR_LEFT);
		final boolean airRight = state.get(BlockPSDTop.AIR_RIGHT);
		final boolean isDoor = world.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGDoorBase;

		final RouteRenderer routeRenderer = new RouteRenderer(matrices, vertexConsumers, platformPos, false);

		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(-0.5, 0, 0.125 - IGui.SMALL_OFFSET * 2);

		routeRenderer.renderColorStrip(airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, light);
		if (airLeft) {
			routeRenderer.renderColorStrip(PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - PSDTopModel.END_FRONT_OFFSET, 0.75F + PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - PSDTopModel.END_FRONT_OFFSET, light);
		}
		if (airRight) {
			routeRenderer.renderColorStrip(0.25F - PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - PSDTopModel.END_FRONT_OFFSET, 1 - PSDTopModel.END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - PSDTopModel.END_FRONT_OFFSET, light);
		}

		if (state.get(BlockPSDTop.SIDE) == BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.LEFT) {
			if (isDoor) {
				routeRenderer.renderArrow(SIDE_AND_BOTTOM_PADDING, 2 - SIDE_AND_BOTTOM_PADDING, TOP_PADDING + ARROW_PADDING, 1 - SIDE_AND_BOTTOM_PADDING - ARROW_PADDING, RIGHT_TO_LEFT, DESTINATION_SCALE, light);
			} else if (!airLeft && !airRight) {
				final int glassLength = getGlassLength(world, pos, facing);
				if (glassLength > 1) {
					routeRenderer.renderLine(SIDE_AND_BOTTOM_PADDING * 2, glassLength - SIDE_AND_BOTTOM_PADDING * 2, TOP_PADDING, 1 - SIDE_AND_BOTTOM_PADDING, BASE_SCALE, light);
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
