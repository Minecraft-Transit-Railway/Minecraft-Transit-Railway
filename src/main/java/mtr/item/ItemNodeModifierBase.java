package mtr.item;

import mtr.ItemGroups;
import mtr.block.BlockRail;
import mtr.block.IBlock;
import mtr.data.Rail;
import mtr.data.RailAngle;
import mtr.data.RailType;
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
import net.minecraft.world.World;

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
							final RailAngle railAngleStart = RailAngle.fromAngle(angle1 + (RailAngle.similarFacing(angleDifference, angle1) ? 0 : 180));
							final RailAngle railAngleEnd = RailAngle.fromAngle(angle2 + (RailAngle.similarFacing(angleDifference, angle2) ? 180 : 0));

							if (isValidStart(posStart, railAngleStart, posEnd, railAngleEnd)) {
								onConnect(world, stateStart, stateEnd, posStart, posEnd, railAngleStart, railAngleEnd, player, railwayData);
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
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		final NbtCompound nbtCompound = stack.getOrCreateTag();
		final long posLong = nbtCompound.getLong(TAG_POS);
		if (posLong != 0) {
			tooltip.add(new TranslatableText("tooltip.mtr.selected_block", BlockPos.fromLong(posLong).toShortString()).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
		}
	}

	protected abstract void onConnect(World world, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, PlayerEntity player, RailwayData railwayData);

	protected abstract void onRemove(World world, BlockPos posStart, BlockPos posEnd, RailwayData railwayData);

	private static boolean isValidStart(BlockPos startPos, RailAngle startFacing, BlockPos endPos, RailAngle endFacing) {
		final Rail rail1 = new Rail(startPos, startFacing, endPos, endFacing, RailType.NONE);
		final Rail rail2 = new Rail(startPos, startFacing, endPos, endFacing, RailType.NONE);
		return rail1.isValid() && rail2.isValid();
	}
}
