package MTR;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityLightRail1 extends EntityTrainBase {

	public EntityLightRail1(World world) {
		super(world);
		name = "LightRail1";
	}

	public EntityLightRail1(World world, double x, double y, double z) {
		super(world, x, y, z);
		name = "LightRail1";
	}

	@Override
	protected void onStationRailPass(BlockPos pos, boolean powered) {
		super.onStationRailPass(pos, powered);
		// set route
		try {
			TileEntityRouteChangerEntity te = (TileEntityRouteChangerEntity) worldObj
					.getTileEntity(getPosition().add(0, -1, 0));
			setRoute(te.route - 2000);
			setRouteBehind();
		} catch (Exception e) {
		}
	}

	private void setRouteBehind() {
		try {
			EntityLightRail1 entityBack = (EntityLightRail1) MinecraftServer.getServer().getEntityFromUuid(carBack);
			entityBack.setRoute(getRoute());
			entityBack.setRouteBehind();
		} catch (Exception e) {
		}
	}

	private void setRoute(int route) {
		dataWatcher.updateObject(19, route);
	}

	public int getRoute() {
		return dataWatcher.getWatchableObjectInt(19);
	}

	@Override
	protected int getTrainLength() {
		return 23;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(19, new Integer(0)); // route number
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		setRoute(compound.getInteger("route"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("route", getRoute());
	}
}