package mtr.render;

import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class RenderStationName<T extends BlockStationNameBase.TileEntityStationNameBase> extends BlockEntityRenderer<T> implements IGui {

	public RenderStationName(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!entity.shouldRender()) {
			return;
		}

		final WorldAccess world = entity.getWorld();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getPos();
		final String stationName = ClientData.stations.stream().filter(station1 -> station1.inStation(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.name).orElse(new TranslatableText("gui.mtr.untitled").getString());

		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
		final int color;
		switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
			case 0:
				color = ARGB_WHITE;
				break;
			case 1:
				color = ARGB_LIGHT_GRAY;
				break;
			default:
				color = ARGB_BLACK;
				break;
		}

		matrices.push();
		matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
		matrices.scale(1F / entity.scale, 1F / entity.scale, 1F / entity.scale);
		final int drawStyle = entity.hasShadow && color != ARGB_BLACK ? 1 : 0;
		IGui.drawStringWithFont(matrices, MinecraftClient.getInstance().textRenderer, entity.verticalChinese ? IGui.formatVerticalChinese(stationName) : stationName, 1, 1, 0, 0, color, drawStyle, null);
		matrices.pop();
	}
}
