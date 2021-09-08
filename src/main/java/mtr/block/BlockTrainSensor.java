package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;

public class BlockTrainSensor extends Block {
    public static final BooleanProperty REDSTONE = BooleanProperty.of("redstone");

    public BlockTrainSensor(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(REDSTONE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(REDSTONE);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(REDSTONE, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            if(world.getBlockState(pos).get(REDSTONE) == false) {
                world.setBlockState(pos, state.with(REDSTONE, true));
                player.sendMessage(new LiteralText("Redstone On"), false);
            } else {
                world.setBlockState(pos, state.with(REDSTONE, false));
                player.sendMessage(new LiteralText("Redstone Off"), false);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return state.get(REDSTONE);
    }


    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if(state.get(REDSTONE) == true) {
            return 9;
        } else {
            return 0;
        }
    }

}
