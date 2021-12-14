package mtr.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCargoLoader extends Block{

	public static final BooleanProperty UNLOAD = BooleanProperty.of("unload");
	public BlockCargoLoader() {
		super(FabricBlockSettings.of(Material.METAL, MapColor.IRON_GRAY).requiresTool().hardness(2));
		setDefaultState(stateManager.getDefaultState().with(UNLOAD, false));
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if(!world.isClient) {
			world.setBlockState(pos, state.with(UNLOAD, !IBlock.getStatePropertySafe(state, UNLOAD)));
		}
		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(UNLOAD);
	}

}
