package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.MTRClient;
import org.mtr.block.BlockStationNameBase;
import org.mtr.block.IBlock;
import org.mtr.client.IDrawing;
import org.mtr.core.data.Station;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.tool.Drawing;

public abstract class RenderStationNameBase<T extends BlockStationNameBase.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IDrawing {

	@Override
	public void render(T entity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		final int color = RenderRouteBase.getShadingColor(facing, entity.getColor(state));

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
		storedMatrixTransformations.add(matrixStack -> {
			Drawing.rotateYDegrees(matrixStack, -facing.toYRot());
			Drawing.rotateZDegrees(matrixStack, 180);
		});

		final Station station = MTRClient.findStation(pos);
		for (int i = 0; i < (entity.isDoubleSided ? 2 : 1); i++) {
			final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
			final boolean shouldFlip = i == 1;
			storedMatrixTransformations2.add(matrixStack -> {
				if (shouldFlip) {
					Drawing.rotateYDegrees(matrixStack, 180);
				}
				matrixStack.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
			});
			drawStationName(world, pos, state, facing, storedMatrixTransformations2, station == null ? TranslationProvider.GUI_MTR_UNTITLED.getString() : station.getName(), station == null ? 0 : station.getColor(), color, light);
		}
	}

	protected abstract void drawStationName(Level world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light);
}
