package mtr.item;

import mtr.block.BlockRail;
import mtr.block.IBlock;
import mtr.data.Platform;
import mtr.data.Rail;
import mtr.data.RailwayData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
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
	private final Rail.RailType railType;

	public static final String TAG_POS = "pos";

	public ItemRailModifier(boolean isConnector, Rail.RailType railType) {
		super(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
		this.isConnector = isConnector;
		this.railType = railType;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		if (!world.isClient) {
			final BlockPos posStart = context.getBlockPos();
			final BlockEntity entity = world.getBlockEntity(posStart);

			if (entity instanceof BlockRail.TileEntityRail) {
				final CompoundTag tag = context.getStack().getOrCreateTag();

				if (tag.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(tag.getLong(TAG_POS));
					final BlockEntity entity2 = world.getBlockEntity(posEnd);

					if (entity2 instanceof BlockRail.TileEntityRail) {
						if (isConnector) {
							final boolean isEastWest1 = IBlock.getStatePropertySafe(world, posStart, BlockRail.FACING);
							final boolean isEastWest2 = IBlock.getStatePropertySafe(world, posEnd, BlockRail.FACING);
							final Direction facingStart = getDirectionFromPos(posStart, isEastWest1, posEnd);
							final Direction facingEnd = getDirectionFromPos(posEnd, isEastWest2, posStart);
							final PlayerEntity player = context.getPlayer();

							if (isValidStart(posStart, facingStart, posEnd) && isValidStart(posEnd, facingEnd, posStart)) {
								final boolean isPlatform = railType == Rail.RailType.PLATFORM;

								if (isPlatform && (((BlockRail.TileEntityRail) entity).hasPlatform() || ((BlockRail.TileEntityRail) entity2).hasPlatform())) {
									if (player != null) {
										player.sendMessage(new TranslatableText("gui.mtr.platform_exists"), true);
									}
								} else {
									((BlockRail.TileEntityRail) entity).addRail(facingStart, posEnd, facingEnd, railType);
									((BlockRail.TileEntityRail) entity2).addRail(facingEnd, posStart, facingStart, railType);

									if (isPlatform) {
										final RailwayData railwayData = RailwayData.getInstance(world);
										if (railwayData != null) {
											railwayData.setData(world, new Platform(posStart, posEnd));
										}
									}
								}

							} else {
								if (player != null) {
									player.sendMessage(new TranslatableText("gui.mtr.invalid_orientation"), true);
								}
							}
						} else {
							((BlockRail.TileEntityRail) entity).removeRail(posEnd);
							((BlockRail.TileEntityRail) entity2).removeRail(posStart);
						}
					}

					tag.remove(TAG_POS);
				} else {
					tag.putLong(TAG_POS, posStart.asLong());
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

		final CompoundTag tag = stack.getOrCreateTag();
		final long posLong = tag.getLong(TAG_POS);
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
