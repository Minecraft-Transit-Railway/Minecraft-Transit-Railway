package MTR;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRouteChanger implements IMessage {

	private int x, y, z, route;

	public MessageRouteChanger() {
	}

	public MessageRouteChanger(int x, int y, int z, int route) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.route = route;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		y = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		z = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		route = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, String.valueOf(x));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(y));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(z));
		ByteBufUtils.writeVarShort(buf, route);
	}

	public static class Handler implements IMessageHandler<MessageRouteChanger, IMessage> {
		@Override
		public IMessage onMessage(final MessageRouteChanger message, final MessageContext context) {
			IThreadListener mainThread = (WorldServer) context.getServerHandler().playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity te = context.getServerHandler().playerEntity.worldObj
							.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (te instanceof TileEntityRouteChangerEntity) {
						TileEntityRouteChangerEntity te2 = (TileEntityRouteChangerEntity) te;
						te2.route = message.route;
					}
					if (te instanceof TileEntityStationNameEntity) {
						TileEntityStationNameEntity te2 = (TileEntityStationNameEntity) te;
						te2.station = message.route;
					}
				}
			});
			return null;
		}
	}
}