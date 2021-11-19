package mtr.render;

import mtr.block.BlockRail;
import mtr.block.BlockSignalLightBase;
import mtr.block.BlockSignalSemaphoreBase;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.gui.ClientData;
import mtr.path.PathData;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.WorldAccess;

import java.util.Map;

public abstract class RenderSignalBase<T extends BlockEntity> extends BlockEntityRenderer<T> implements IBlock, IGui {

	protected final boolean isSingleSided;

	public RenderSignalBase(BlockEntityRenderDispatcher dispatcher, boolean isSingleSided) {
		super(dispatcher);
		this.isSingleSided = isSingleSided;
	}

	@Override
	public final void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockSignalLightBase || state.getBlock() instanceof BlockSignalSemaphoreBase)) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalFacingBlock.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			return;
		}

		BlockPos startPos = null;
		final int[] checkDistance = {0, 1, -1, 2, -2, 3, -3, 4, -4};
		for (final int x : checkDistance) {
			for (int y = -3; y <= 0; y++) {
				final BlockPos checkPos = pos.up(y).offset(facing.rotateYClockwise(), x);
				final BlockState checkState = world.getBlockState(checkPos);
				if (checkState.getBlock() instanceof BlockRail && IBlock.getStatePropertySafe(checkState, BlockRail.FACING) == (facing.getAxis() == Direction.Axis.X)) {
					startPos = checkPos;
					break;
				}
			}
		}
		if (startPos == null) {
			return;
		}

		matrices.push();
		matrices.translate(0.5, 0, 0.5);

		for (int i = 0; i < 2; i++) {
			final Direction newFacing = (i == 1 ? facing.getOpposite() : facing);

			boolean isOccupied = false;
			final Map<BlockPos, Rail> railMap = ClientData.RAILS.get(startPos);
			if (railMap != null) {
				for (final BlockPos endPos : railMap.keySet()) {
					if (railMap.get(endPos).facingStart.similarFacing(newFacing.asRotation() - 90) && ClientData.SIGNAL_BLOCKS.isOccupied(PathData.getRailProduct(startPos, endPos))) {
						isOccupied = true;
						break;
					}
				}
			}

			matrices.push();
			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(newFacing.asRotation()));
			final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png"), false));
			render(matrices, vertexConsumers, vertexConsumer, entity, tickDelta, newFacing, isOccupied, i == 1);
			matrices.pop();

			if (isSingleSided) {
				break;
			}
		}

		matrices.pop();
	}

	protected abstract void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, VertexConsumer vertexConsumer, T entity, float tickDelta, Direction facing, boolean isOccupied, boolean isBackSide);
}
