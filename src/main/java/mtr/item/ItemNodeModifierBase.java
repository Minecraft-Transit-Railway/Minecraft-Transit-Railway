package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockRail;
import mtr.block.IBlock;
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
							final boolean isEastWest1 = IBlock.getStatePropertySafe(world, posStart, BlockRail.FACING);
							final boolean isEastWest2 = IBlock.getStatePropertySafe(world, posEnd, BlockRail.FACING);
							final Direction facingStart = getDirectionFromPos(posStart, isEastWest1, posEnd);
							final Direction facingEnd = getDirectionFromPos(posEnd, isEastWest2, posStart);

							if (isValidStart(posStart, facingStart, posEnd) && isValidStart(posEnd, facingEnd, posStart)) {
								onConnect(world, stateStart, stateEnd, posStart, posEnd, facingStart, facingEnd, player, railwayData);
							} else {
								if (player != null) {
									player.sendMessage(new TranslatableText("gui.mtr.invalid_orientation"), true);
								}
							}
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

	protected abstract void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, Direction facingStart, Direction facingEnd, PlayerEntity player, RailwayData railwayData);

	protected abstract void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData);

	private static Direction getDirectionFromPos(BlockPos startPos, boolean isEastWest, BlockPos endPos) {
		if (isEastWest) {
			return endPos.getX() > startPos.getX() ? Direction.EAST : Direction.WEST;
		} else {
			return endPos.getZ() > startPos.getZ() ? Direction.SOUTH : Direction.NORTH;
		}
	}

	private static boolean isValidStart(BlockPos startPos, Direction startFacing, BlockPos endPos) {
		final BlockPos posDifference = endPos.subtract(startPos);
		final boolean sameX = startFacing.getOffsetX() == Math.signum(posDifference.getX());
		final boolean sameZ = startFacing.getOffsetZ() == Math.signum(posDifference.getZ());
		return startFacing.getAxis() == Direction.Axis.X ? sameX : sameZ;
	}
}
