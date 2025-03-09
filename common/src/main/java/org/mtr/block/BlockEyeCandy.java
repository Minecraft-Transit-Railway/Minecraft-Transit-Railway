package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;
import org.mtr.registry.Registry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEyeCandy extends BlockWaterloggable implements BlockEntityProvider {

	public BlockEyeCandy(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return super.getPlacementState(itemPlacementContext).with(Properties.FACING, itemPlacementContext.getHorizontalPlayerFacing());
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Nonnull
	@Override
	protected VoxelShape getCullingShape(BlockState state) {
		// Prevents culling optimization mods from culling our fully transparent block
		return VoxelShapes.empty();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
	}

	@Nonnull
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof EyeCandyBlockEntity) {
				entity.markDirty();
				Registry.sendPacketToClient((ServerPlayerEntity) player, new PacketOpenBlockEntityScreen(pos));
			}
		});
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new EyeCandyBlockEntity(blockPos, blockState);
	}

	public static class EyeCandyBlockEntity extends BlockEntity {

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

		public EyeCandyBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.EYE_CANDY.createAndGet(), pos, state);
		}

		@Override
		protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			modelId = nbt.getString(KEY_MODEL_ID);
			translateX = nbt.getFloat(KEY_TRANSLATE_X);
			translateY = nbt.getFloat(KEY_TRANSLATE_Y);
			translateZ = nbt.getFloat(KEY_TRANSLATE_Z);
			rotateX = nbt.getFloat(KEY_ROTATE_X);
			rotateY = nbt.getFloat(KEY_ROTATE_Y);
			rotateZ = nbt.getFloat(KEY_ROTATE_Z);
			fullBrightness = nbt.getBoolean(KEY_FULL_BRIGHTNESS);
		}

		@Override
		protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
			nbt.putString(KEY_MODEL_ID, modelId);
			nbt.putFloat(KEY_TRANSLATE_X, translateX);
			nbt.putFloat(KEY_TRANSLATE_Y, translateY);
			nbt.putFloat(KEY_TRANSLATE_Z, translateZ);
			nbt.putFloat(KEY_ROTATE_X, rotateX);
			nbt.putFloat(KEY_ROTATE_Y, rotateY);
			nbt.putFloat(KEY_ROTATE_Z, rotateZ);
			nbt.putBoolean(KEY_FULL_BRIGHTNESS, fullBrightness);
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
			markDirty();
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
