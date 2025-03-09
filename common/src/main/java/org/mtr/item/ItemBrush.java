package org.mtr.item;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.block.BlockNode;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Rail;
import org.mtr.packet.PacketUpdateLastRailStyles;

import javax.annotation.Nonnull;

public class ItemBrush extends Item {

	public ItemBrush(Item.Settings settings) {
		super(settings.maxCount(1));
	}

	/**
	 * Behaviour for shift-clicking on a block can't be defined in the {@link Block#onUse(BlockState, World, BlockPos, PlayerEntity, BlockHitResult)} method, so that behaviour is defined here instead.
	 */
	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		final PlayerEntity playerEntity = context.getPlayer();
		if (world.isClient() && playerEntity != null && world.getBlockState(context.getBlockPos()).getBlock() instanceof BlockNode) {
			final ObjectObjectImmutablePair<Rail, BlockPos> railAndBlockPos = MinecraftClientData.getInstance().getFacingRailAndBlockPos(false);
			if (railAndBlockPos != null) {
				return PacketUpdateLastRailStyles.CLIENT_CACHE.canApplyStylesToRail(playerEntity.getUuid(), railAndBlockPos.left(), true) ? ActionResult.SUCCESS : ActionResult.FAIL;
			}
		}
		return super.useOnBlock(context);
	}
}
