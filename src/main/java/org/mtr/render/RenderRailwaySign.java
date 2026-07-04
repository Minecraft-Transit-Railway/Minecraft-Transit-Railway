package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.block.BlockRailwaySign;
import org.mtr.block.IBlock;
import org.mtr.data.IGui;
import org.mtr.resource.SignResource;
import org.mtr.tool.Drawing;

public final class RenderRailwaySign<T extends BlockRailwaySign.RailwaySignBlockEntity> extends BlockEntityRendererExtension<T> {

	@Override
	public void render(T blockEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, ClientLevel clientWorld, LocalPlayer clientPlayerEntity, float tickDelta, int light, int overlay) {
		final BlockPos pos = blockEntity.getBlockPos();
		final BlockState state = clientWorld.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockRailwaySign blockRailwaySign) || blockEntity.getSignIds().length != blockRailwaySign.length) {
			return;
		}

		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		Drawing.rotateYDegrees(matrixStack, -facing.toYRot());
		Drawing.rotateZDegrees(matrixStack, 180);
		matrixStack.translate(blockRailwaySign.getXStart() / 16F - 0.5, -0.53125, -0.0625 - IGui.SMALL_OFFSET);
		SignResource.render(matrixStack, vertexConsumerProvider, pos, blockEntity.getSelectedIds(), blockEntity.getSignIds(), 0.5F, IGui.SMALL_OFFSET, false);
	}
}
