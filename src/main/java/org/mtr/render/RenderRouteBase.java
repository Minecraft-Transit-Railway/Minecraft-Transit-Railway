package org.mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.mtr.MTRClient;
import org.mtr.block.BlockPSDTop;
import org.mtr.block.IBlock;
import org.mtr.client.DynamicTextureCache;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.tool.Drawing;

public abstract class RenderRouteBase<T extends BlockPSDTop.BlockEntityBase> extends BlockEntityRendererExtension<T> implements IGui, IBlock {

	protected final float topPadding;
	protected final float bottomPadding;
	protected final float sidePadding;
	private final float z;
	private final boolean transparentWhite;
	private final int platformSearchYOffset;
	private final IntegerProperty arrowDirectionProperty;

	public RenderRouteBase(float z, float topPadding, float bottomPadding, float sidePadding, boolean transparentWhite, int platformSearchYOffset, IntegerProperty arrowDirectionProperty) {
		this.z = z / 16;
		this.topPadding = topPadding / 16;
		this.bottomPadding = bottomPadding / 16;
		this.sidePadding = sidePadding / 16;
		this.transparentWhite = transparentWhite;
		this.platformSearchYOffset = platformSearchYOffset;
		this.arrowDirectionProperty = arrowDirectionProperty;
	}

	@Override
	public void render(T entity, PoseStack matrixStack2, MultiBufferSource vertexConsumerProvider, ClientLevel world, LocalPlayer player, float tickDelta, int light, int overlay) {
		final BlockPos blockPos = entity.getBlockPos();
		final BlockState state = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getBlockPos().getX(), entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
		storedMatrixTransformations.add(matrixStack -> Drawing.rotateYDegrees(matrixStack, -facing.toYRot()));

		renderAdditionalUnmodified(storedMatrixTransformations.copy(), state, facing, light);

		MTRClient.findClosePlatform(blockPos.below(platformSearchYOffset), 5, platform -> {
			final long platformId = platform.getId();

			storedMatrixTransformations.add(matrixStack -> {
				matrixStack.translate(0, 1, 0);
				Drawing.rotateZDegrees(matrixStack, 180);
				matrixStack.translate(-0.5, -getAdditionalOffset(state), z);
			});

			final int leftBlocks = getTextureNumber(world, blockPos, facing, true);
			final int rightBlocks = getTextureNumber(world, blockPos, facing, false);
			final int color = getShadingColor(facing, ARGB_WHITE);
			final RenderType renderType = getRenderType(world, blockPos.relative(facing.getCounterClockWise(), leftBlocks), state);

			if ((renderType == RenderType.ARROW || renderType == RenderType.ROUTE) && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
				final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
				final float height = 1 - topPadding - bottomPadding;
				final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

				final ResourceLocation identifier;
				if (renderType == RenderType.ARROW) {
					identifier = DynamicTextureCache.instance.getDirectionArrow(platformId, (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
				} else {
					identifier = DynamicTextureCache.instance.getRouteMap(platformId, false, arrowDirection == 2, width / height, transparentWhite).identifier;
				}

				MainRenderer.scheduleRender(identifier, false, QueuedRenderLayer.EXTERIOR, (matrixStack, vertexConsumer, offset) -> {
					storedMatrixTransformations.transform(matrixStack, offset);
					IDrawing.drawTexture(matrixStack, vertexConsumer, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
					matrixStack.popPose();
				});
			}

			renderAdditional(storedMatrixTransformations, platformId, state, leftBlocks, rightBlocks, facing.getOpposite(), color, light);
		});
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
	}

	protected float getAdditionalOffset(BlockState state) {
		return 0;
	}

	protected boolean isLeft(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.LEFT;
	}

	protected boolean isRight(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.RIGHT;
	}

	protected abstract RenderType getRenderType(Level world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light);

	private int getTextureNumber(Level world, BlockPos pos, Direction facing, boolean searchLeft) {
		int number = 0;
		final Block thisBlock = world.getBlockState(pos).getBlock();

		while (true) {
			final BlockState state = world.getBlockState(pos.relative(searchLeft ? facing.getCounterClockWise() : facing.getClockWise(), number));

			if (state.getBlock().equals(thisBlock)) {
				final boolean isLeft = isLeft(state);
				final boolean isRight = isRight(state);

				if (number == 0 || (searchLeft ? !isRight : !isLeft)) {
					number++;
					if (searchLeft ? isLeft : isRight) {
						break;
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}

		return number - 1;
	}

	public static int getShadingColor(Direction facing, int grayscaleColorByte) {
		final int colorByte = Math.round((grayscaleColorByte & 0xFF) * (facing.getAxis() == Direction.Axis.X ? 0.75F : 1));
		return ARGB_BLACK | ((colorByte << 16) + (colorByte << 8) + colorByte);
	}

	protected enum RenderType {ARROW, ROUTE, NONE}
}
