package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.block.BlockNode;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.data.RailAngle;
import mtr.data.SignalBlocks;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.path.PathData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
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

			int aspect = 0;
			boolean render = false;

			Map<BlockPos, Float> nodeMap = new HashMap<>();
			nodeMap.put(startPos, newFacing.toYRot() + 90);

			for (int a = 0; a < aspects - 1; a++) {
				Map<BlockPos, Float> newMap = new HashMap<>();
				boolean occupied = false;
				for (BlockPos nodePos : nodeMap.keySet()) {
					final Map<BlockPos, Rail> railMap = ClientData.RAILS.get(nodePos);
					if (railMap != null) {
						for (final BlockPos endPos : railMap.keySet()) {
							if (railMap.get(endPos).facingStart.similarFacing(nodeMap.get(nodePos))) {
								newMap.put(endPos, railMap.get(endPos).facingStart.angleDegrees);
								render = true;
								if (ClientData.SIGNAL_BLOCKS.isOccupied(PathData.getRailProduct(nodePos, endPos))) {
									occupied = true;
									break;
								}
							}
						}
					} else {
						occupied = true;
					}
				}
				if (occupied || (a == aspects - 1 && !occupied) {
					aspect = a;
					break;
				}
				nodeMap = new HashMap<>(newMap);
			}

			if (render) {
				matrices.pushPose();
				matrices.mulPose(Vector3f.YN.rotationDegrees(newFacing.toYRot()));
				final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false));
				render(matrices, vertexConsumers, vertexConsumer, entity, tickDelta, newFacing, aspect, i == 1);
				matrices.popPose();
			}

			if (isSingleSided) {
				break;
			}
		}

		matrices.popPose();
	}

	protected abstract void render(PoseStack matrices, MultiBufferSource vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, int aspect, boolean isBackSide);

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
