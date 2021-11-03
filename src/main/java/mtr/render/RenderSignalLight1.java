package mtr.render;

import mtr.block.BlockRail;
import mtr.block.BlockSignalLight1;
import mtr.block.IBlock;
import mtr.data.IGui;
import mtr.data.Rail;
import mtr.gui.ClientData;
import mtr.gui.IDrawing;
import mtr.path.PathData;
import net.minecraft.block.BlockState;
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

public class RenderSignalLight1 extends BlockEntityRenderer<BlockSignalLight1.TileEntitySignalLight1> implements IBlock, IGui {

	public RenderSignalLight1(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(BlockSignalLight1.TileEntitySignalLight1 entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockSignalLight1)) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockSignalLight1.FACING);
		if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) {
			return;
		}

		BlockPos startPos = null;
		for (int x = -2; x <= 2; x++) {
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

		boolean isOccupied = false;
		final Map<BlockPos, Rail> railMap = ClientData.RAILS.get(startPos);
		if (railMap != null) {
			for (final BlockPos endPos : railMap.keySet()) {
				if (railMap.get(endPos).facingStart == facing && ClientData.SIGNAL_BLOCKS.isOccupied(PathData.getRailProduct(startPos, endPos))) {
					isOccupied = true;
					break;
				}
			}
		}

		matrices.push();
		matrices.translate(0.5, 0, 0.5);
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new Identifier("mtr:textures/block/white.png"), false));
		final float y = isOccupied ? 0.0625F : 0.4375F;
		IDrawing.drawTexture(matrices, vertexConsumer, -0.125F, y, -0.19375F, 0.125F, y + 0.25F, -0.19375F, facing.getOpposite(), isOccupied ? 0xFFFF0000 : 0xFF00FF00, MAX_LIGHT_GLOWING);
		matrices.pop();
	}
}
