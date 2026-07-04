package org.mtr.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.mtr.block.BlockNode;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketUpdateLastRailStyles;

public class ItemBrush extends Item {

	public ItemBrush(Item.Properties settings) {
		super(settings.stacksTo(1));
	}

	/**
	 * Behaviour for shift-clicking on a block can't be defined in the {@link Block#useWithoutItem(BlockState, Level, BlockPos, Player, BlockHitResult)} method, so that behaviour is defined here instead.
	 */
	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level world = context.getLevel();
		final Player playerEntity = context.getPlayer();
		if (world.isClientSide() && playerEntity != null && world.getBlockState(context.getClickedPos()).getBlock() instanceof BlockNode) {
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos != null) {
				return PacketUpdateLastRailStyles.CLIENT_CACHE.canApplyStylesToRail(playerEntity.getUUID(), railAndBlockPos.left(), true) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
			}
		}
		return super.useOn(context);
	}
}
