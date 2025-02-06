package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.BlockEntityTypes;
import org.mtr.mod.Blocks;
import org.mtr.mod.Init;
import org.mtr.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockEyeCandy extends BlockWaterloggable implements DirectionHelper, BlockWithEntity {

	public BlockEyeCandy() {
		super(Blocks.createDefaultBlockSettings(true).nonOpaque());
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		return super.getPlacementState2(itemPlacementContext).with(new Property<>(FACING.data), itemPlacementContext.getPlayerFacing().data);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Nonnull
	@Override
	public VoxelShape getCullingShape2(BlockState state, BlockView world, BlockPos pos) {
		// Prevents culling optimization mods from culling our fully transparent block
		return VoxelShapes.empty();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		super.addBlockProperties(properties);
		properties.add(FACING);
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final org.mtr.mapping.holder.BlockEntity entity = world.getBlockEntity(pos);
			if (entity != null && entity.data instanceof BlockEntity) {
				((BlockEntity) entity.data).markDirty2();
				Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType2(BlockState state) {
		return BlockRenderType.getEntityblockAnimatedMapped();
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityExtension {

		private String modelId = "";
		private float translateX, translateY, translateZ;
		private float rotateX, rotateY, rotateZ;
		private boolean fullBrightness;

		private static final String KEY_MODEL_ID = "prefabId";
		private static final String KEY_TRANSLATE_X = "translateX";
		private static final String KEY_TRANSLATE_Y = "translateY";
		private static final String KEY_TRANSLATE_Z = "translateZ";
		private static final String KEY_ROTATE_X = "rotateX";
		private static final String KEY_ROTATE_Y = "rotateY";
		private static final String KEY_ROTATE_Z = "rotateZ";
		private static final String KEY_FULL_BRIGHTNESS = "fullLight";

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.EYE_CANDY.get(), pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			modelId = compoundTag.getString(KEY_MODEL_ID);
			translateX = compoundTag.getFloat(KEY_TRANSLATE_X);
			translateY = compoundTag.getFloat(KEY_TRANSLATE_Y);
			translateZ = compoundTag.getFloat(KEY_TRANSLATE_Z);
			rotateX = compoundTag.getFloat(KEY_ROTATE_X);
			rotateY = compoundTag.getFloat(KEY_ROTATE_Y);
			rotateZ = compoundTag.getFloat(KEY_ROTATE_Z);
			fullBrightness = compoundTag.getBoolean(KEY_FULL_BRIGHTNESS);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putString(KEY_MODEL_ID, modelId);
			compoundTag.putFloat(KEY_TRANSLATE_X, translateX);
			compoundTag.putFloat(KEY_TRANSLATE_Y, translateY);
			compoundTag.putFloat(KEY_TRANSLATE_Z, translateZ);
			compoundTag.putFloat(KEY_ROTATE_X, rotateX);
			compoundTag.putFloat(KEY_ROTATE_Y, rotateY);
			compoundTag.putFloat(KEY_ROTATE_Z, rotateZ);
			compoundTag.putBoolean(KEY_FULL_BRIGHTNESS, fullBrightness);
		}

		public void setData(String modelId, float translateX, float translateY, float translateZ, float rotateX, float rotateY, float rotateZ, boolean fullLight) {
			this.modelId = modelId;
			this.fullBrightness = fullLight;
			this.translateX = translateX;
			this.translateY = translateY;
			this.translateZ = translateZ;
			this.rotateX = rotateX;
			this.rotateY = rotateY;
			this.rotateZ = rotateZ;
			markDirty2();
		}

		@Nullable
		public String getModelId() {
			return modelId.isEmpty() ? null : modelId;
		}

		public float getTranslateX() {
			return translateX;
		}

		public float getTranslateY() {
			return translateY;
		}

		public float getTranslateZ() {
			return translateZ;
		}

		public float getRotateX() {
			return rotateX;
		}

		public float getRotateY() {
			return rotateY;
		}

		public float getRotateZ() {
			return rotateZ;
		}

		public boolean getFullBrightness() {
			return fullBrightness;
		}
	}
}
