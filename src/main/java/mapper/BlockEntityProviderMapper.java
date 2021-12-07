package mapper;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface BlockEntityProviderMapper extends BlockEntityProvider {

	BlockEntity createBlockEntity(BlockPos pos, BlockState state);

	BlockEntity createBlockEntity(BlockView world);

	default <T extends BlockEntity> void tick(World world, BlockPos pos, T blockEntity) {
	}

	default BlockEntityType<? extends BlockEntity> getType() {
		return null;
	}
}
