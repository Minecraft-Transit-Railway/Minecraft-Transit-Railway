package mtr.render;

import mtr.block.BlockPSDAPGGlassBase;
import mtr.block.BlockPSDTop;
import mtr.block.BlockPlatformRail;
import mtr.gui.ClientData;
import mtr.gui.IGui;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

public class RenderPSDTop extends BlockEntityRenderer<BlockPSDTop.TileEntityPSDTop> {

	private static final int BASE_SCALE = 384;
	private static final int PASSED_STATION_COLOR = 0x999999;

	private static final int STATION_CIRCLE_SIZE = 16;
	private static final int LINE_HEIGHT = 10;

	private static final float SIDE_PADDING = 0.25F;
	private static final float HORIZONTAL_SPACE = 2 - SIDE_PADDING * 2;
	private static final float TOP_PADDING = 0.5F;
	private static final float BOTTOM_PADDING = 0.125F;
	private static final float VERTICAL_SPACE = 1 - TOP_PADDING - BOTTOM_PADDING;

	private static final float BOTTOM_LINE_POSITION = 0.90625F;
	private static final float BOTTOM_LINE_HEIGHT = 0.03125F;


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
		final BlockState state = world.getBlockState(pos);
		final Direction facing = state.get(BlockPSDTop.FACING);
		final boolean isLeft = state.get(BlockPSDTop.SIDE) == BlockPSDAPGGlassBase.EnumPSDAPGGlassSide.LEFT;

		final BlockPos platformPos = findPlatformPos(world, pos);
		if (platformPos == null) {
			return;
		}
		final List<Triple<Integer, Integer, List<String>>> routeData = ClientData.platformToRoute.get(platformPos);
		if (routeData == null) {
			return;
		}
		final int routeCount = routeData.size();

		matrices.push();
		matrices.translate(0.5, 1, 0.5);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-facing.asRotation()));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		matrices.translate(-0.5, 0, 0.125 - IGui.SMALL_OFFSET * 2);

		matrices.push();
		final VertexConsumer vertexConsumerBottomLine = vertexConsumers.getBuffer(RenderLayer.getLeash());
		final float lineHeightSmall = BOTTOM_LINE_HEIGHT / routeCount;
		for (int i = 0; i < routeCount; i++) {
			final int routeColor = routeData.get(i).getLeft();
			IGui.drawRectangle(matrices.peek().getModel(), vertexConsumerBottomLine, 0, BOTTOM_LINE_POSITION + lineHeightSmall * i, 1, BOTTOM_LINE_POSITION + lineHeightSmall * (i + 1), 0, routeColor, light);
		}
		matrices.pop();

		if (isLeft) {
			final int scaleSmaller = BASE_SCALE * routeCount;
			matrices.scale(1F / scaleSmaller, 1F / scaleSmaller, 1F / scaleSmaller);

			final MinecraftClient minecraftClient = MinecraftClient.getInstance();
			final TextRenderer textRenderer = minecraftClient.textRenderer;

			for (int i = 0; i < routeCount; i++) {
				final int routeColor = routeData.get(i).getLeft();
				final int currentStationIndex = routeData.get(i).getMiddle();
				final List<String> route = routeData.get(i).getRight();
				final int routeLength = route.size();
				final float y = ((i + 0.5F) * VERTICAL_SPACE / routeCount + TOP_PADDING) * scaleSmaller;

				matrices.push();
				final VertexConsumer vertexConsumerLine = vertexConsumers.getBuffer(RenderLayer.getLeash());
				IGui.drawRectangle(matrices.peek().getModel(), vertexConsumerLine, SIDE_PADDING * scaleSmaller, y - LINE_HEIGHT / 2F, getStationX(currentStationIndex, routeLength, scaleSmaller), y + LINE_HEIGHT / 2F, IGui.SMALL_OFFSET * scaleSmaller, PASSED_STATION_COLOR, light);
				IGui.drawRectangle(matrices.peek().getModel(), vertexConsumerLine, getStationX(currentStationIndex, routeLength, scaleSmaller), y - LINE_HEIGHT / 2F, (HORIZONTAL_SPACE + SIDE_PADDING) * scaleSmaller, y + LINE_HEIGHT / 2F, IGui.SMALL_OFFSET * scaleSmaller, routeColor, light);
				matrices.pop();

				for (int j = 0; j < routeLength; j++) {
					final float x = getStationX(j, routeLength, scaleSmaller);
					final boolean onOrAfterStation = j >= currentStationIndex;
					final boolean onStation = j == currentStationIndex;

					matrices.push();
					final VertexConsumer vertexConsumerText = vertexConsumers.getBuffer(RenderLayer.getText(new Identifier(onOrAfterStation ? "mtr:textures/block/station_circle.png" : "mtr:textures/block/station_circle_passed.png")));
					IGui.drawTexture(matrices.peek().getModel(), vertexConsumerText, light, x - STATION_CIRCLE_SIZE / 2F, y - STATION_CIRCLE_SIZE / 2F, STATION_CIRCLE_SIZE, STATION_CIRCLE_SIZE);
					matrices.pop();

					final boolean bottomText = (j % 2) == 0;
					IGui.drawStringWithFont(matrices, onStation ? vertexConsumers : null, textRenderer, route.get(j), 1, bottomText ? 0 : 2, x, y + (bottomText ? 1 : -1) * STATION_CIRCLE_SIZE, IGui.SMALL_OFFSET * scaleSmaller, onStation ? IGui.ARGB_WHITE : onOrAfterStation ? IGui.ARGB_BLACK : PASSED_STATION_COLOR, IGui.ARGB_BLACK, false);
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

	private float getStationX(int stationIndex, int routeLength, int scaleSmaller) {
		return (stationIndex * HORIZONTAL_SPACE / (routeLength - 1) + SIDE_PADDING) * scaleSmaller;
	}
}
