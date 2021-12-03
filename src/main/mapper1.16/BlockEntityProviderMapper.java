package mapper;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BlockEntityProviderMapper extends BlockEntityProvider {

	@Override
	default BlockEntity createBlockEntity(BlockView world) {
		return createBlockEntity(null, null);
	}

	BlockEntity createBlockEntity(BlockPos pos, BlockState state);

	default <T extends BlockEntity> void tick(World world, BlockPos pos, T blockEntity) {
	}

	default BlockEntityType<? extends BlockEntity> getType() {
		return null;
	}
}
