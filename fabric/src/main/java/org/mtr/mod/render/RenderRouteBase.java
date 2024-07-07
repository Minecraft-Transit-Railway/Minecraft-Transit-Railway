package org.mtr.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPSDTop;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;

public abstract class RenderRouteBase<T extends BlockPSDTop.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, IBlock {

	protected final float topPadding;
	protected final float bottomPadding;
	protected final float sidePadding;
	private final float z;
	private final boolean transparentWhite;
	private final int platformSearchYOffset;
	private final IntegerProperty arrowDirectionProperty;

	public RenderRouteBase(Argument dispatcher, float z, float topPadding, float bottomPadding, float sidePadding, boolean transparentWhite, int platformSearchYOffset, IntegerProperty arrowDirectionProperty) {
		super(dispatcher);
		this.z = z / 16;
		this.topPadding = topPadding / 16;
		this.bottomPadding = bottomPadding / 16;
		this.sidePadding = sidePadding / 16;
		this.transparentWhite = transparentWhite;
		this.platformSearchYOffset = platformSearchYOffset;
		this.arrowDirectionProperty = arrowDirectionProperty;
	}

	@Override
	public final void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
		final World world = entity.getWorld2();
		if (world == null) {
			return;
		}

		final BlockPos blockPos = entity.getPos2();
		final BlockState state = world.getBlockState(blockPos);
		final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
		storedMatrixTransformations.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(-facing.asRotation()));

		renderAdditionalUnmodified(storedMatrixTransformations.copy(), state, facing, light);

		InitClient.findClosePlatform(blockPos.down(platformSearchYOffset), 5, platform -> {
			final long platformId = platform.getId();

			storedMatrixTransformations.add(graphicsHolderNew -> {
				graphicsHolderNew.translate(0, 1, 0);
				graphicsHolderNew.rotateZDegrees(180);
				graphicsHolderNew.translate(-0.5, -getAdditionalOffset(state), z);
			});

			final int leftBlocks = getTextureNumber(world, blockPos, facing, true);
			final int rightBlocks = getTextureNumber(world, blockPos, facing, false);
			final int color = getShadingColor(facing, ARGB_WHITE);
			final RenderType renderType = getRenderType(world, blockPos.offset(facing.rotateYCounterclockwise(), leftBlocks), state);

			if ((renderType == RenderType.ARROW || renderType == RenderType.ROUTE) && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
				final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
				final float height = 1 - topPadding - bottomPadding;
				final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

				final Identifier identifier;
				if (renderType == RenderType.ARROW) {
					identifier = DynamicTextureCache.instance.getDirectionArrow(platformId, (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
				} else {
					identifier = DynamicTextureCache.instance.getRouteMap(platformId, false, arrowDirection == 2, width / height, transparentWhite).identifier;
				}

				MainRenderer.scheduleRender(identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
					storedMatrixTransformations.transform(graphicsHolderNew, offset);
					IDrawing.drawTexture(graphicsHolderNew, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
					graphicsHolderNew.pop();
				});
			}

			renderAdditional(storedMatrixTransformations, platformId, state, leftBlocks, rightBlocks, facing.getOpposite(), color, light);
		});
	}

	@Override
	public boolean rendersOutsideBoundingBox2(T blockEntity) {
		return true;
	}

	protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
	}

	protected float getAdditionalOffset(BlockState state) {
		return 0;
	}

	protected boolean isLeft(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.LEFT;
	}

	protected boolean isRight(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.RIGHT;
	}

	protected abstract RenderType getRenderType(World world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light);

	private int getTextureNumber(World world, BlockPos pos, Direction facing, boolean searchLeft) {
		int number = 0;
		final Block thisBlock = world.getBlockState(pos).getBlock();

		while (true) {
			final BlockState state = world.getBlockState(pos.offset(searchLeft ? facing.rotateYCounterclockwise() : facing.rotateYClockwise(), number));

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
		final int colorByte = Math.round((grayscaleColorByte & 0xFF) * (facing.getAxis() == Axis.X ? 0.75F : 1));
		return ARGB_BLACK | ((colorByte << 16) + (colorByte << 8) + colorByte);
	}

	protected enum RenderType {ARROW, ROUTE, NONE}
}
