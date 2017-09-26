package MTR;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePlatformMaker implements IMessage {

	private int x, y, z, station, number;

	public MessagePlatformMaker() {
	}

	public MessagePlatformMaker(int x, int y, int z, int station, int number) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.station = station;
		this.number = number;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		y = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		z = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		station = ByteBufUtils.readVarShort(buf);
		number = ByteBufUtils.readVarShort(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, String.valueOf(x));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(y));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(z));
		ByteBufUtils.writeVarShort(buf, station);
		ByteBufUtils.writeVarShort(buf, number);
	}

	public static class Handler implements IMessageHandler<MessagePlatformMaker, IMessage> {
		@Override
		public IMessage onMessage(final MessagePlatformMaker message, final MessageContext context) {
			IThreadListener mainThread = (WorldServer) context.getServerHandler().playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World worldIn = context.getServerHandler().playerEntity.worldObj;
					PlatformData data = PlatformData.get(worldIn);
					int c = 0;
					if (message.number == 0) {
						while (!(data.platformX[c] == message.x && data.platformY[c] == message.y
								&& data.platformZ[c] == message.z) && c < 999)
							c++;
						data.platformX[c] = 0;
						data.platformY[c] = 0;
						data.platformZ[c] = 0;
						data.platformAlias[c] = 0;
						data.platformNumber[c] = 0;
					} else {
						while (data.platformNumber[c] != 0 && c < 999)
							c++;
						data.platformX[c] = message.x;
						data.platformY[c] = message.y;
						data.platformZ[c] = message.z;
						data.platformAlias[c] = message.station;
						data.platformNumber[c] = message.number;
					}
					data.markDirty();
				}
			});
			return null;
		}
	}
}