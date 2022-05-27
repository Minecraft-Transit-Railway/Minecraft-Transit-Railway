package mtr.item;

import mtr.EntityTypes;
import mtr.ItemGroups;
import mtr.entity.EntityLift;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemLift extends Item {

	final EntityTypes.LiftType liftType;

	public ItemLift(EntityTypes.LiftType liftType) {
		super(new Properties().tab(ItemGroups.RAILWAY_FACILITIES).stacksTo(1));
		this.liftType = liftType;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		final Level world = context.getLevel();
		final BlockPos pos = context.getClickedPos();
		final float rotation = -context.getHorizontalDirection().toYRot();
		final Vec3 offset = new Vec3(liftType.width % 2 == 1 ? 0 : -0.5, 0, liftType.depth % 2 == 1 ? 0 : 0.5).yRot((float) Math.toRadians(rotation)).add(pos.getX() + 0.5, 0, pos.getZ() + 0.5);
		final EntityLift entity = liftType.liftSupplier.liftSupplier(world, Math.round(offset.x * 2) / 2D, pos.getY() + 1, Math.round(offset.z * 2) / 2D);
		Utilities.setYaw(entity, rotation);
		world.addFreshEntity(entity);
		return InteractionResult.SUCCESS;
	}
}
