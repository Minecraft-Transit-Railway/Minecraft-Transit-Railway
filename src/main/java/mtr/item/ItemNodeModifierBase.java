package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockRail;
import mtr.block.IBlock;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ItemNodeModifierBase extends Item {

	protected final boolean isConnector;

	public static final String TAG_POS = "pos";

	public ItemNodeModifierBase(boolean isConnector) {
		super(new Item.Settings().group(ItemGroups.CORE).maxCount(1));
		this.isConnector = isConnector;
	}

	@Override
	public final ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		if (!world.isClient) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			final BlockPos posStart = context.getBlockPos();
			final BlockState stateStart = world.getBlockState(posStart);

			if (railwayData != null && stateStart.getBlock() instanceof BlockRail) {
				final NbtCompound nbtCompound = context.getStack().getOrCreateTag();

				if (nbtCompound.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(nbtCompound.getLong(TAG_POS));
					final BlockState stateEnd = world.getBlockState(posEnd);

					if (stateEnd.getBlock() instanceof BlockRail) {
						final PlayerEntity player = context.getPlayer();

						if (isConnector) {
							final float angle1 = (IBlock.getStatePropertySafe(world, posStart, BlockRail.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(world, posStart, BlockRail.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(world, posStart, BlockRail.IS_45) ? 45 : 0);
							final float angle2 = (IBlock.getStatePropertySafe(world, posEnd, BlockRail.FACING) ? 0 : 90) + (IBlock.getStatePropertySafe(world, posEnd, BlockRail.IS_22_5) ? 22.5F : 0) + (IBlock.getStatePropertySafe(world, posEnd, BlockRail.IS_45) ? 45 : 0);

							final float angleDifference = (float) Math.toDegrees(Math.atan2(posEnd.getZ() - posStart.getZ(), posEnd.getX() - posStart.getX()));
							final RailAngle railAngleStart = RailAngle.fromAngle(angle1);
							final RailAngle railAngleEnd = RailAngle.fromAngle(angle2);

							onConnect(world, stateStart, stateEnd, posStart, posEnd, railAngleStart, railAngleEnd, player, railwayData);

						} else {
							onRemove(world, posStart, posEnd, railwayData);
						}
					}

					nbtCompound.remove(TAG_POS);
				} else {
					nbtCompound.putLong(TAG_POS, posStart.asLong());
				}

				return ActionResult.SUCCESS;
			} else {
				return ActionResult.FAIL;
			}
		} else {
			return super.useOnBlock(context);
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		final NbtCompound nbtCompound = stack.getOrCreateTag();
		final long posLong = nbtCompound.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(new TranslatableText("tooltip.mtr.selected_block", BlockPos.fromLong(posLong).toShortString()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
		}
	}

	protected abstract void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, PlayerEntity player, RailwayData railwayData);

	protected abstract void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData);

	private static Direction getDirectionFromPos(BlockPos startPos, boolean isEastWest, BlockPos endPos) {
		if (isEastWest) {
			return endPos.getX() > startPos.getX() ? Direction.EAST : Direction.WEST;
		} else {
			return endPos.getZ() > startPos.getZ() ? Direction.SOUTH : Direction.NORTH;
		}
	}

}
