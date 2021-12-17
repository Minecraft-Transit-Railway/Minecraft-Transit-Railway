package mapper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public interface EntityBlockMapper extends EntityBlock {

	@Override
	default BlockEntity newBlockEntity(BlockGetter blockGetter) {
		return createBlockEntity(null, null);
	}

	BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state);

	default <T extends BlockEntityMapper> void tick(Level world, BlockPos pos, T blockEntity) {
	}

	default BlockEntityType<? extends BlockEntityMapper> getType() {
		return null;
	}
}
