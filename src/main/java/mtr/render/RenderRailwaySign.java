package mtr.render;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.gui.IGui;
import mtr.model.ModelTrainBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class RenderRailwaySign<T extends BlockRailwaySign.TileEntityRailwaySign> extends BlockEntityRenderer<T> implements IBlock, IGui {

	public RenderRailwaySign(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final BlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockRailwaySign)) {
			return;
		}
		final BlockRailwaySign block = (BlockRailwaySign) state.getBlock();
		if (entity.getSign().length != block.length) {
			return;
		}
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final BlockRailwaySign.SignType[] signTypes = entity.getSign();

		matrices.push();
		matrices.translate(0.5, 0.53125, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 2);

		for (int i = 0; i < signTypes.length; i++) {
			if (signTypes[i] != null) {
				IGui.drawRectangle(matrices, vertexConsumers, 0, 0, 0.5F * (signTypes.length), 0.5F, SMALL_OFFSET, ARGB_BLACK, light);

				final int index = i;
				drawSign(matrices, dispatcher.getTextRenderer(), signTypes[i], 0.5F * i, 0, 0.5F, i, signTypes.length - i - 1, (x, y, size, flipTexture) -> IGui.drawTexture(matrices, vertexConsumers, signTypes[index].id.toString(), x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, -1, ModelTrainBase.MAX_LIGHT));
			}
		}

		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}

	public static void drawSign(MatrixStack matrices, TextRenderer textRenderer, BlockRailwaySign.SignType signType, float x, float y, float size, float maxWidthLeft, float maxWidthRight, DrawTexture drawTexture) {
		final float signSize = (signType.small ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
		final float margin = (size - signSize) / 2;

		final boolean hasCustomText = signType.hasCustomText;
		final boolean flipped = signType.flipped;
		final boolean flipTexture = flipped && !hasCustomText;

		if (signType == BlockRailwaySign.SignType.LINE || signType == BlockRailwaySign.SignType.LINE_FLIPPED) {
		} else if (signType == BlockRailwaySign.SignType.PLATFORM || signType == BlockRailwaySign.SignType.PLATFORM_FLIPPED) {
		} else {
			drawTexture.drawTexture(x + margin, y + margin, signSize, flipTexture);

			if (hasCustomText) {
				final float maxWidth = Math.max(0, (flipped ? maxWidthLeft : maxWidthRight) * size - margin);
				IGui.drawStringWithFont(matrices, textRenderer, signType.text, flipped ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, flipped ? x : x + size, y + margin, maxWidth, signSize, 0.01F, ARGB_WHITE, false, null);
			}
		}
	}

	@FunctionalInterface
	public interface DrawTexture {

		void drawTexture(float x, float y, float size, boolean flipTexture);
	}
}
