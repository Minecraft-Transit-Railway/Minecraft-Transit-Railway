package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockRail;
import mtr.block.IBlock;
import mtr.data.Rail;
import mtr.data.RailType;
import mtr.data.RailwayData;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemRailModifier extends Item {

	private final boolean isConnector;
	private final boolean isOneWay;
	private final RailType railType;

	public static final String TAG_POS = "pos";
	public static final String TAG_BALLAST_TEXTURE = "ballast_texture";

	public ItemRailModifier() {
		super(new Item.Settings().group(ItemGroups.CORE).maxCount(1));
		isConnector = false;
		isOneWay = false;
		railType = null;
	}

	public ItemRailModifier(boolean isOneWay, RailType railType) {
		super(new Item.Settings().group(ItemGroups.CORE).maxCount(1));
		isConnector = true;
		this.isOneWay = isOneWay;
		this.railType = railType;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		final World world = context.getWorld();
		if (!world.isClient) {
			final RailwayData railwayData = RailwayData.getInstance(world);
			if (railwayData == null) return ActionResult.FAIL;

			final BlockPos posStart = context.getBlockPos();
			final BlockState stateStart = world.getBlockState(posStart);
			final NbtCompound nbtCompound = context.getStack().getOrCreateTag();
			final PlayerEntity player = context.getPlayer();

			if (stateStart.getBlock() instanceof BlockRail) {
				if (nbtCompound.contains(TAG_POS)) {
					final BlockPos posEnd = BlockPos.fromLong(nbtCompound.getLong(TAG_POS));
					final String ballastTexture = nbtCompound.getString(TAG_BALLAST_TEXTURE);
					final BlockState stateEnd = world.getBlockState(posEnd);

					if (stateEnd.getBlock() instanceof BlockRail) {
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
									final Rail rail1 = new Rail(posStart, facingStart, posEnd, facingEnd, isOneWay ? RailType.NONE : railType, ballastTexture);
									final Rail rail2 = new Rail(posEnd, facingEnd, posStart, facingStart, railType, ballastTexture);
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
				if (player != null && player.isSneaking()) {
					nbtCompound.putString(TAG_BALLAST_TEXTURE, "");
					player.sendMessage(new TranslatableText("gui.mtr.ballast_none"), true);
				} else {
					BlockState state = world.getBlockState(context.getBlockPos());
					nbtCompound.putString(TAG_BALLAST_TEXTURE, Registry.BLOCK.getId(state.getBlock()).toString());
					if (player != null) {
						player.sendMessage(new TranslatableText(state.getBlock().getTranslationKey()), true);
					}
				}
				return ActionResult.SUCCESS;
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

		final String ballastTexture = nbtCompound.getString(TAG_BALLAST_TEXTURE);
		if (!ballastTexture.isEmpty()) {
			final String[] parts = ballastTexture.split(":", 2);
			final String translationKey = "block." + parts[0] + "." + parts[1].replace('/', '.');
			tooltip.add(new TranslatableText(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
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
