package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockNode;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.UtilitiesClient;
import mtr.path.PathData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public abstract class RenderSignalBase<T extends BlockEntityMapper> extends BlockEntityRendererMapper<T> implements IBlock, IGui {

	protected final boolean isSingleSided;
	protected final int aspects;

	public RenderSignalBase(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided, int aspects) {
		super(dispatcher);
		this.isSingleSided = isSingleSided;
		this.aspects = aspects;
	}

	// TODO backwards compatibility
	@Deprecated
	public RenderSignalBase(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided) {
		this(dispatcher, isSingleSided, 2);
	}

	@Override
	public final void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final BlockGetter world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockSignalLightBase || state.getBlock() instanceof BlockSignalSemaphoreBase)) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			return;
		}

		final BlockPos startPos = getNodePos(world, pos, facing);
		if (startPos == null) {
			return;
		}

		matrices.pushPose();
		matrices.translate(0.5, 0, 0.5);

		for (int i = 0; i < 2; i++) {
			final Direction newFacing = (i == 1 ? facing.getOpposite() : facing);
			final int occupiedAspect = getOccupiedAspect(startPos, newFacing.toYRot() + 90);

			if (occupiedAspect >= 0) {
				matrices.pushPose();
				UtilitiesClient.rotateYDegrees(matrices, -newFacing.toYRot());
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false));
				render(matrices, vertexConsumers, vertexConsumer, entity, tickDelta, newFacing, occupiedAspect, i == 1);
				// TODO temporary code
				render(matrices, vertexConsumers, vertexConsumer, entity, tickDelta, newFacing, occupiedAspect == 1, i == 1);
				// TODO temporary code end
				matrices.popPose();
			}

			if (isSingleSided) {
				break;
			}
		}

		matrices.popPose();
	}

	// TODO make abstract later
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, int occupiedAspect, boolean isBackSide) {
	}

	// TODO temporary code
	protected void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide) {
	}
	// TODO temporary code end

	private int getOccupiedAspect(BlockPos startPos, float facing) {
		Map<BlockPos, Float> nodesToScan = new HashMap<>();
		nodesToScan.put(startPos, facing);
		int occupiedAspect = -1;

		for (int j = 1; j < aspects; j++) {
			final Map<BlockPos, Float> newNodesToScan = new HashMap<>();

			for (final Map.Entry<BlockPos, Float> checkNode : nodesToScan.entrySet()) {
				final Map<BlockPos, Rail> railMap = ClientData.RAILS.get(checkNode.getKey());

				if (railMap != null) {
					for (final BlockPos endPos : railMap.keySet()) {
						final Rail rail = railMap.get(endPos);
						if (rail.facingStart.similarFacing(checkNode.getValue())) {
							if (ClientData.SIGNAL_BLOCKS.isOccupied(PathData.getRailProduct(checkNode.getKey(), endPos))) {
								return j;
							} else {
								final Boolean isOccupied = ClientData.OCCUPIED_RAILS.get(PathData.getRailProduct(checkNode.getKey(), endPos));
								if (isOccupied != null && isOccupied) {
									return j;
								}
							}

							newNodesToScan.put(endPos, rail.facingEnd.getOpposite().angleDegrees);
							occupiedAspect = 0;
						}
					}
				}
			}

			nodesToScan = newNodesToScan;
		}

		return occupiedAspect;
	}

	private static BlockPos getNodePos(BlockGetter world, BlockPos pos, Direction facing) {
		final int[] checkDistance = {0, 1, -1, 2, -2, 3, -3, 4, -4};
		for (final int z : checkDistance) {
			for (final int x : checkDistance) {
				for (int y = -5; y <= 0; y++) {
					final BlockPos checkPos = pos.above(y).relative(facing.getClockWise(), x).relative(facing, z);
					final BlockState checkState = world.getBlockState(checkPos);
					if (checkState.getBlock() instanceof BlockNode) {
						return checkPos;
					}
				}
			}
		}
		return null;
	}
}
