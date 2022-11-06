package mtr.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class RenderRouteBase<T extends BlockPSDTop.TileEntityRouteBase> extends BlockEntityRendererMapper<T> implements IGui, IBlock {

	protected float bottomPadding;
	protected float topPadding;

	protected final float sidePadding;
	private final float z;
	private final boolean transparentWhite;
	private final Property<Integer> arrowDirectionProperty;

	public RenderRouteBase(BlockEntityRenderDispatcher dispatcher, float z, float sidePadding, boolean transparentWhite, Property<Integer> arrowDirectionProperty) {
		super(dispatcher);
		this.z = z / 16;
		this.sidePadding = sidePadding / 16;
		this.transparentWhite = transparentWhite;
		this.arrowDirectionProperty = arrowDirectionProperty;
	}

	@Override
	public final void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		final Level world = entity.getLevel();
		if (world == null) {
			return;
		}

		final BlockPos pos = entity.getBlockPos();
		final BlockState state = world.getBlockState(pos);
		final Direction facing = IBlock.getStatePropertySafe(state, HorizontalDirectionalBlock.FACING);

		final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
		storedMatrixTransformations.add(matricesNew -> {
			matricesNew.translate(0.5 + entity.getBlockPos().getX(), entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
			matricesNew.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
		});

		renderAdditionalUnmodified(storedMatrixTransformations.copy(), state, facing, light);

		if (!RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) {
			final long platformId = entity.getPlatformId(ClientData.PLATFORMS, ClientData.DATA_CACHE);

			if (platformId != 0) {
				storedMatrixTransformations.add(matricesNew -> {
					matricesNew.translate(0, 1, 0);
					matricesNew.mulPose(Vector3f.ZP.rotationDegrees(180));
					matricesNew.translate(-0.5, 0, z);
				});

				final int leftBlocks = getTextureNumber(world, pos, facing, true);
				final int rightBlocks = getTextureNumber(world, pos, facing, false);
				final int color = getShadingColor(facing, ARGB_WHITE);
				final RenderType renderType = getRenderType(world, pos.relative(facing.getCounterClockWise(), leftBlocks), state);

				if ((renderType == RenderType.ARROW || renderType == RenderType.ROUTE) && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
					final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
					final float height = 1 - topPadding - bottomPadding;
					final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

					final ResourceLocation resourceLocation;
					if (renderType == RenderType.ARROW) {
						resourceLocation = ClientData.DATA_CACHE.getDirectionArrow(platformId, (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).resourceLocation;
					} else {
						resourceLocation = ClientData.DATA_CACHE.getRouteMap(platformId, false, arrowDirection == 2, width / height, transparentWhite).resourceLocation;
					}

					RenderTrains.scheduleRender(resourceLocation, false, MoreRenderLayers::getExterior, (matricesNew, vertexConsumer) -> {
						storedMatrixTransformations.transform(matricesNew);
						IDrawing.drawTexture(matricesNew, vertexConsumer, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
						matricesNew.popPose();
					});
				}

				renderAdditional(storedMatrixTransformations, platformId, state, leftBlocks, rightBlocks, facing.getOpposite(), color, light);
			}
		}
	}

	@Override
	public boolean shouldRenderOffScreen(T blockEntity) {
		return true;
	}

	protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
	}

	protected boolean isLeft(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.LEFT;
	}

	protected boolean isRight(BlockState state) {
		return IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == IBlock.EnumSide.RIGHT;
	}

	protected abstract RenderType getRenderType(BlockGetter world, BlockPos pos, BlockState state);

	protected abstract void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light);

	private int getTextureNumber(BlockGetter world, BlockPos pos, Direction facing, boolean searchLeft) {
		int number = 0;
		final Block thisBlock = world.getBlockState(pos).getBlock();

		while (true) {
			final BlockState state = world.getBlockState(pos.relative(searchLeft ? facing.getCounterClockWise() : facing.getClockWise(), number));

			if (state.getBlock() == thisBlock) {
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
