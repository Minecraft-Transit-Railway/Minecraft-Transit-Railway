package mtr.item;

import mtr.EntityTypes;
import mtr.ItemGroups;
import mtr.block.BlockLiftTrack;
import mtr.entity.EntityLift;
import mtr.mappings.Text;
import mtr.mappings.Utilities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ItemLift extends Item {

	final EntityTypes.LiftType liftType;

	public ItemLift(EntityTypes.LiftType liftType) {
		super(new Properties().tab(ItemGroups.ESCALATORS_LIFTS).stacksTo(1));
		this.liftType = liftType;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level world = context.getLevel();
		final BlockPos clickedPos = context.getClickedPos();
		final BlockPos pos = world.getBlockState(clickedPos).getBlock() instanceof BlockLiftTrack ? clickedPos : clickedPos.relative(context.getClickedFace());
		final float rotation = -context.getHorizontalDirection().toYRot();
		final Vec3 offset = new Vec3(0.5 - liftType.width / 2F, 0, liftType.depth / 2F - 0.5).yRot((float) Math.toRadians(rotation)).add(pos.getX() + 0.5, 0, pos.getZ() + 0.5);
		final EntityLift entity = liftType.liftSupplier.liftSupplier(world, Math.round(offset.x * 2) / 2D, pos.getY(), Math.round(offset.z * 2) / 2D);
		Utilities.setYaw(entity, rotation);
		world.addFreshEntity(entity);
		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
		tooltip.add(Text.translatable("tooltip.mtr.rail_action_width", liftType.width).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
		tooltip.add(Text.translatable("tooltip.mtr.rail_action_depth", liftType.depth).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
	}
}
