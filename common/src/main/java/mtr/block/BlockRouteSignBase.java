package mtr.block;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockRouteSignBase extends BlockDirectionalDoubleBlockBase implements EntityBlockMapper, IBlock {

	public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.create("propagate_property", 0, 3);

	public BlockRouteSignBase() {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15).noOcclusion());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
		final double y = hit.getLocation().y;
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			if (isUpper && y - Math.floor(y) > 0.8125) {
				world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
				propagate(world, pos, Direction.DOWN, ARROW_DIRECTION, 1);
			} else {
				final BlockEntity entity = world.getBlockEntity(pos.below(isUpper ? 1 : 0));
				if (entity instanceof TileEntityRouteSignBase) {
					PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayer) player, entity.getBlockPos());
				}
			}
		});
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, ARROW_DIRECTION);
	}

	public static abstract class TileEntityRouteSignBase extends BlockEntityClientSerializableMapper {

		private long platformId;
		private static final String KEY_PLATFORM_ID = "platform_id";

		public TileEntityRouteSignBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public void readCompoundTag(CompoundTag compoundTag) {
			platformId = compoundTag.getLong(KEY_PLATFORM_ID);
		}

		@Override
		public void writeCompoundTag(CompoundTag compoundTag) {
			compoundTag.putLong(KEY_PLATFORM_ID, platformId);
		}

		public void setPlatformId(long platformId) {
			this.platformId = platformId;
			setChanged();
			syncData();
		}

		public long getPlatformId() {
			return platformId;
		}
	}
}
