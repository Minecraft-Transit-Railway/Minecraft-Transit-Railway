package mtr.item;

import mtr.block.BlockRail;
import mtr.block.IBlock;
import mtr.data.Rail;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

import java.util.List;

public class ItemRailModifier extends Item {

	private final boolean isConnector;
	private final RailType railType;

	public static final String TAG_POS = "pos";

	public ItemRailModifier(boolean isConnector, RailType railType) {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
		this.isConnector = isConnector;
		this.railType = railType;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
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
								if (railType.hasSavedRail && (railwayData.hasSavedRail(posStart) || railwayData.hasSavedRail(posEnd))) {
									if (player != null) {
										player.sendMessage(new TranslatableText("gui.mtr.platform_or_siding_exists"), true);
									}
								} else {
									final Rail rail1 = new Rail(posStart, facingStart, posEnd, facingEnd, railType);
									final Rail rail2 = new Rail(posEnd, facingEnd, posStart, facingStart, railType);
									railwayData.addRail(posStart, posEnd, rail1, false);
									final long newId = railwayData.addRail(posEnd, posStart, rail2, true);
									world.setBlockState(posStart, stateStart.with(BlockRail.IS_CONNECTED, true));
									world.setBlockState(posEnd, stateEnd.with(BlockRail.IS_CONNECTED, true));
									PacketTrainDataGuiServer.createRailS2C(world, posStart, posEnd, rail1, rail2, newId);
								}
							} else {
								if (player != null) {
									player.sendMessage(new TranslatableText("gui.mtr.invalid_orientation"), true);
								}
							}
						} else {
							railwayData.removeRailConnection(posStart, posEnd);
							PacketTrainDataGuiServer.removeRailConnectionS2C(world, posStart, posEnd);
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
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (isConnector && railType != null) {
			tooltip.add(new TranslatableText("tooltip.mtr.rail_speed_limit", railType.speedLimit).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		}

		final NbtCompound nbtCompound = stack.getOrCreateTag();
		final long posLong = nbtCompound.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(new TranslatableText("tooltip.mtr.selected_block", BlockPos.fromLong(posLong).toShortString()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
		}
	}

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
