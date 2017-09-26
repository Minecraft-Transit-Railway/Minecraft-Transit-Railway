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

public class MessageBoosterRail implements IMessage {

	private int x, y, z, speedBoost, speedSlow;

	public MessageBoosterRail() {
	}

	public MessageBoosterRail(int x, int y, int z, int speedBoost, int speedSlow) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.speedBoost = speedBoost;
		this.speedSlow = speedSlow;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		y = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		z = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		speedBoost = ByteBufUtils.readVarShort(buf);
		speedSlow = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, String.valueOf(x));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(y));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(z));
		ByteBufUtils.writeVarShort(buf, speedBoost);
		ByteBufUtils.writeVarShort(buf, speedSlow);
	}

	public static class Handler implements IMessageHandler<MessageBoosterRail, IMessage> {
		@Override
		public IMessage onMessage(final MessageBoosterRail message, final MessageContext context) {
			IThreadListener mainThread = (WorldServer) context.getServerHandler().playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity te = context.getServerHandler().playerEntity.worldObj
							.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (te instanceof TileEntityRailBoosterEntity) {
						TileEntityRailBoosterEntity te2 = (TileEntityRailBoosterEntity) te;
						te2.speedBoost = message.speedBoost;
						te2.speedSlow = message.speedSlow;
					}
				}
			});
			return null;
		}
	}
}