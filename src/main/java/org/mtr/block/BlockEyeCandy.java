package org.mtr.block;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.packet.PacketOpenBlockEntityScreen;
import org.mtr.registry.BlockEntityTypes;

public class BlockEyeCandy extends BlockWaterloggable implements EntityBlock {

	public BlockEyeCandy(BlockBehaviour.Properties settings) {
		super(settings.noOcclusion());
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext) {
		return super.getStateForPlacement(itemPlacementContext).setValue(BlockStateProperties.HORIZONTAL_FACING, itemPlacementContext.getHorizontalDirection());
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
//? if >= 1.21.4 {
	protected VoxelShape getOcclusionShape(BlockState state) {
//? } else {
	/*protected VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
//
*///? }
		// Prevents culling optimization mods from culling our fully transparent block
		return Shapes.empty();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> PacketOpenBlockEntityScreen.sendDirectlyToServer((ServerLevel) world, (ServerPlayer) player, pos));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new EyeCandyBlockEntity(blockPos, blockState);
	}

	public static class EyeCandyBlockEntity extends BlockEntityExtension {

		private String modelId = "";
		@Getter
		private float translateX;
		@Getter
		private float translateY;
		@Getter
		private float translateZ;
		@Getter
		private float rotateX;
		@Getter
		private float rotateY;
		@Getter
		private float rotateZ;
		@Getter
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
			super(BlockEntityTypes.EYE_CANDY.get(), pos, state);
		}

		@Override
		protected void readNbt(CompoundTag nbtCompound) {
			modelId = nbtCompound.getString(KEY_MODEL_ID);
			translateX = nbtCompound.getFloat(KEY_TRANSLATE_X);
			translateY = nbtCompound.getFloat(KEY_TRANSLATE_Y);
			translateZ = nbtCompound.getFloat(KEY_TRANSLATE_Z);
			rotateX = nbtCompound.getFloat(KEY_ROTATE_X);
			rotateY = nbtCompound.getFloat(KEY_ROTATE_Y);
			rotateZ = nbtCompound.getFloat(KEY_ROTATE_Z);
			fullBrightness = nbtCompound.getBoolean(KEY_FULL_BRIGHTNESS);
		}

		@Override
		protected void writeNbt(CompoundTag nbtCompound) {
			nbtCompound.putString(KEY_MODEL_ID, modelId);
			nbtCompound.putFloat(KEY_TRANSLATE_X, translateX);
			nbtCompound.putFloat(KEY_TRANSLATE_Y, translateY);
			nbtCompound.putFloat(KEY_TRANSLATE_Z, translateZ);
			nbtCompound.putFloat(KEY_ROTATE_X, rotateX);
			nbtCompound.putFloat(KEY_ROTATE_Y, rotateY);
			nbtCompound.putFloat(KEY_ROTATE_Z, rotateZ);
			nbtCompound.putBoolean(KEY_FULL_BRIGHTNESS, fullBrightness);
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
			setChanged();
		}

		@Nullable
		public String getModelId() {
			return modelId.isEmpty() ? null : modelId;
		}
	}
}
