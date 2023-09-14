package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.packet.PacketOpenPIDSConfigScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPIDSBaseVertical extends BlockDirectionalDoubleBlockBase implements BlockWithEntity {

	public BlockPIDSBaseVertical() {
		super(BlockHelper.createBlockSettings(true, blockState -> 5));
	}

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final boolean isUpper = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		return IBlock.checkHoldingBrush(world, player, () -> {
			final BlockPos finalPos = isUpper ? pos : pos.offset(Axis.Y, 1);
			final BlockEntity entity1 = world.getBlockEntity(finalPos);

			if (entity1 != null && entity1.data instanceof BlockEntityVerticalBase) {
				((BlockEntityVerticalBase) entity1.data).markDirty2();
				Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenPIDSConfigScreen(finalPos, finalPos, ((BlockEntityVerticalBase) entity1.data).getMaxArrivals(), ((BlockEntityVerticalBase) entity1.data).getLinesPerArrival()));
			}
		});
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		final BlockEntityExtension blockEntity = createBlockEntity(new BlockPos(0, 0, 0), Blocks.getAirMapped().getDefaultState());
		if (blockEntity instanceof BlockEntityPIDS) {
			tooltip.add(TextHelper.translatable("tooltip.mtr.arrivals", ((BlockEntityPIDS) blockEntity).getMaxArrivals()).formatted(TextFormatting.GRAY));
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
	}

	public abstract static class BlockEntityVerticalBase extends BlockEntityPIDS {

		public BlockEntityVerticalBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}

		@Override
		public abstract int getLinesPerArrival();
	}
}
