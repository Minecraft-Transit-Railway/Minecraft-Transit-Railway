package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class BlockTactileMap extends HorizontalFacingBlock {

    public BlockTactileMap() {
            super(FabricBlockSettings.of(Material.METAL, MapColor.OFF_WHITE).requiresTool().hardness(2));
        }

        @Override
        public BlockState getPlacementState(ItemPlacementContext ctx) {
            return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
        }

        @Override
        protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
            builder.add(FACING);
        }

        public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
            Stream.of(
                    VoxelShapes.cuboid(3, 0, 6, 12, 1, 9),
                    VoxelShapes.cuboid(6, 1, 7, 9, 9.5, 8),
                    VoxelShapes.cuboid(1.5, 1, 7, 13.5, 2, 17)
            ).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR);});
            return VoxelShapes.cuboid(0.75f, 0f, 0.15f, 1.0f, 1.0f, 0.85f);
        }
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity placedBy,Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            world.playSound(null, blockPos, MTR.TACTILE_MAP_MUSIC, SoundCategory.BLOCKS, 1f, 1f);
            //player.sendMessage(new TranslatableText("Right click again to stop playback"), true);
        }

        return ActionResult.success(false);
    }
    }

