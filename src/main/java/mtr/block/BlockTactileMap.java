package mtr.block;

import mtr.MTR;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class BlockTactileMap extends HorizontalFacingBlock {

    public BlockTactileMap() {
            super(FabricBlockSettings.of(Material.METAL, MapColor.OFF_WHITE).requiresTool().hardness(2));
        }
    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        // A heads-up to people that they cannot actually make a tactile map of their station, I think
        tooltip.add(new TranslatableText("tooltip.mtr.tactile_map").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
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
        // You know what? I give up, goodbye! (Please fix)
        // Never gonna give you up, never gonna let you down!
        final VoxelShape first = IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 1, 16, state.get(FACING).getOpposite());
        final VoxelShape second = IBlock.getVoxelShapeByDirection(5.5, 1, 7.5, 10.5, 20, 8.5, state.get(FACING).getOpposite());
        return VoxelShapes.union(first, second);
    }

    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player,Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            // The music might be copyrighted, please double-check before releasing
            world.playSound(null, blockPos, MTR.TACTILE_MAP_MUSIC, SoundCategory.BLOCKS, 1f, 1f);
            // I have no idea how to make the music stop, there's no method for that?
            // Or instead you can even make it loop the music to make it more realistic
            // I like to add useless things. Remove if you decide to not trying make it stop
            player.sendMessage(new TranslatableText("Press again to stop"), true);
        }
        return ActionResult.success(false);
    }
    }

