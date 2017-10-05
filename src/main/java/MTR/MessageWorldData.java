package MTR;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageWorldData implements IMessage {

	private int[] platformX = new int[1000];
	private int[] platformY = new int[1000];
	private int[] platformZ = new int[1000];
	private int[] platformAlias = new int[1000];
	private int[] platformNumber = new int[1000];
	private UUID[] arrivals = new UUID[5000];

	public MessageWorldData() {
	}

	public MessageWorldData(int[] platformX, int[] platformY, int[] platformZ, int[] platformAlias,
			int[] platformNumber, UUID[] arrivals) {
		this.platformX = platformX;
		this.platformY = platformY;
		this.platformZ = platformZ;
		this.platformAlias = platformAlias;
		this.platformNumber = platformNumber;
		this.arrivals = arrivals;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		for (int i = 0; i < 1000; i++)
			platformX[i] = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		for (int i = 0; i < 1000; i++)
			platformY[i] = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		for (int i = 0; i < 1000; i++)
			platformZ[i] = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		for (int i = 0; i < 1000; i++)
			platformAlias[i] = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		for (int i = 0; i < 1000; i++)
			platformNumber[i] = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		for (int i = 0; i < 5000; i++)
			arrivals[i] = new UUID(Long.parseLong(ByteBufUtils.readUTF8String(buf)),
					Long.parseLong(ByteBufUtils.readUTF8String(buf)));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (int i = 0; i < 1000; i++)
			ByteBufUtils.writeUTF8String(buf, String.valueOf(platformX[i]));
		for (int i = 0; i < 1000; i++)
			ByteBufUtils.writeUTF8String(buf, String.valueOf(platformY[i]));
		for (int i = 0; i < 1000; i++)
			ByteBufUtils.writeUTF8String(buf, String.valueOf(platformZ[i]));
		for (int i = 0; i < 1000; i++)
			ByteBufUtils.writeUTF8String(buf, String.valueOf(platformAlias[i]));
		for (int i = 0; i < 1000; i++)
			ByteBufUtils.writeUTF8String(buf, String.valueOf(platformNumber[i]));
		for (int i = 0; i < 5000; i++) {
			String a = "0", b = "0";
			try {
				a = String.valueOf(arrivals[i].getMostSignificantBits());
				b = String.valueOf(arrivals[i].getLeastSignificantBits());
			} catch (Exception e) {
			}
			ByteBufUtils.writeUTF8String(buf, a);
			ByteBufUtils.writeUTF8String(buf, b);
		}
	}

	public static class Handler implements IMessageHandler<MessageWorldData, IMessage> {
		@Override
		public IMessage onMessage(final MessageWorldData message, final MessageContext context) {
			PlatformData data = PlatformData.get(MTR.proxy.getWorld());
			data.platformX = message.platformX;
			data.platformY = message.platformY;
			data.platformZ = message.platformZ;
			data.platformAlias = message.platformAlias;
			data.platformNumber = message.platformNumber;
			data.arrivals = message.arrivals;
			data.markDirty();
			return null;
		}
	}
}