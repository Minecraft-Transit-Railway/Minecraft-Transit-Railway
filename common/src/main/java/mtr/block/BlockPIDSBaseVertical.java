package mtr.block;

import mtr.data.IPIDS;
import mtr.mappings.EntityBlockMapper;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockPIDSBaseVertical extends BlockDirectionalDoubleBlockBase implements EntityBlockMapper, IPIDS {
    public BlockPIDSBaseVertical() {
        super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockPos finalPos = isUpper ? pos : pos.relative(Direction.Axis.Y, 1);
            final BlockEntity entity1 = world.getBlockEntity(finalPos);

            if (entity1 instanceof BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) {
                ((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).syncData();
                PacketTrainDataGuiServer.openPIDSConfigScreenS2C((ServerPlayer) player, finalPos, finalPos, ((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).getMaxArrivals(), ((BlockPIDSBaseVertical.TileEntityBlockPIDSBaseVertical) entity1).getLinesPerArrival());
            }
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    public abstract static class TileEntityBlockPIDSBaseVertical extends TileEntityPIDS {
        public TileEntityBlockPIDSBaseVertical(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public int getLinesPerArrival() {
            return 2;
        }
    }
}
