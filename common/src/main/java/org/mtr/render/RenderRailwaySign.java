package org.mtr.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.block.BlockRailwaySign;
import org.mtr.block.IBlock;
import org.mtr.data.IGui;
import org.mtr.resource.SignResource;
import org.mtr.tool.Drawing;

public final class RenderRailwaySign<T extends BlockRailwaySign.RailwaySignBlockEntity> extends BlockEntityRendererExtension<T> {

	@Override
	public void render(T blockEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, float tickDelta, int light, int overlay) {
		final BlockPos pos = blockEntity.getPos();
		final BlockState state = clientWorld.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockRailwaySign blockRailwaySign) || blockEntity.getSignIds().length != blockRailwaySign.length) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		Drawing.rotateYDegrees(matrixStack, -facing.getPositiveHorizontalDegrees());
		Drawing.rotateZDegrees(matrixStack, 180);
		matrixStack.translate(blockRailwaySign.getXStart() / 16F - 0.5, -0.53125, -0.0625 - IGui.SMALL_OFFSET);
		SignResource.render(matrixStack, vertexConsumerProvider, pos, blockEntity.getSelectedIds(), blockEntity.getSignIds(), 0.5F, IGui.SMALL_OFFSET, false);
	}
}
