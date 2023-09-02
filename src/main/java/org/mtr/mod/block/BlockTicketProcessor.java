package mtr.block;

import mtr.SoundEvents;
import mtr.data.TicketSystem;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockTicketProcessor extends BlockDirectionalDoubleBlockBase {

	public boolean hasLight;
	public boolean canEnter;
	public boolean canExit;

	public static final EnumProperty<EnumTicketProcessorLights> LIGHTS = EnumProperty.create("lights", EnumTicketProcessorLights.class);

	public BlockTicketProcessor(boolean hasLight, boolean canEnter, boolean canExit) {
		super(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 5).noOcclusion());
		this.hasLight = hasLight;
		this.canEnter = canEnter;
		this.canExit = canExit;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if (!world.isClientSide && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			final TicketSystem.EnumTicketBarrierOpen open = TicketSystem.passThrough(world, pos, player, canEnter, canExit, SoundEvents.TICKET_PROCESSOR_ENTRY, SoundEvents.TICKET_PROCESSOR_ENTRY_CONCESSIONARY, SoundEvents.TICKET_PROCESSOR_EXIT, SoundEvents.TICKET_PROCESSOR_EXIT_CONCESSIONARY, SoundEvents.TICKET_PROCESSOR_FAIL, true);
			world.setBlockAndUpdate(pos, state.setValue(LIGHTS, open.isOpen() ? EnumTicketProcessorLights.GREEN : EnumTicketProcessorLights.RED));
			Utilities.scheduleBlockTick(world, pos, this, 20);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos) {
		if (hasLight) {
			world.setBlockAndUpdate(pos, state.setValue(LIGHTS, EnumTicketProcessorLights.NONE));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			return Shapes.or(IBlock.getVoxelShapeByDirection(4.75, 1, 0, 11.25, 13, 8, facing), IBlock.getVoxelShapeByDirection(7, 0, 2, 9, 1, 4, facing));
		} else {
			return Shapes.or(IBlock.getVoxelShapeByDirection(5, 0, 0, 11, 1, 6, facing), IBlock.getVoxelShapeByDirection(7, 1, 2, 9, 16, 4, facing));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, LIGHTS);
	}

	public enum EnumTicketProcessorLights implements StringRepresentable {

		NONE("none"), RED("red"), YELLOW_GREEN("yellow_green"), GREEN("green");
		private final String name;

		EnumTicketProcessorLights(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
