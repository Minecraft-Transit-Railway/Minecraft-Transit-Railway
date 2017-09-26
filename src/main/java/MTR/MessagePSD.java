package MTR;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePSD implements IMessage {

	private int x, y, z, color, number, bound, arrow;

	public MessagePSD() {
	}

	public MessagePSD(int x, int y, int z, int color, int number, int bound, int arrow) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.number = number;
		this.bound = bound;
		this.arrow = arrow;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		y = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		z = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		color = ByteBufUtils.readVarShort(buf);
		number = ByteBufUtils.readVarShort(buf);
		bound = ByteBufUtils.readVarShort(buf);
		arrow = ByteBufUtils.readVarShort(buf);
		// this class is very useful in general for writing more complex objects
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, String.valueOf(x));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(y));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(z));
		ByteBufUtils.writeVarShort(buf, color);
		ByteBufUtils.writeVarShort(buf, number);
		ByteBufUtils.writeVarShort(buf, bound);
		ByteBufUtils.writeVarShort(buf, arrow);
	}

	public static class Handler implements IMessageHandler<MessagePSD, IMessage> {
		@Override
		public IMessage onMessage(final MessagePSD message, final MessageContext context) {
			IThreadListener mainThread = (WorldServer) context.getServerHandler().playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					TileEntity te = context.getServerHandler().playerEntity.worldObj
							.getTileEntity(new BlockPos(message.x, message.y, message.z));
					if (te instanceof TileEntityPSDBase) {
						TileEntityPSDBase te2 = (TileEntityPSDBase) te;
						te2.color = message.color;
						te2.number = message.number;
						te2.bound = message.bound;
						te2.arrow = message.arrow;
						te2.markDirty();
						te2.update(EnumFacing.NORTH);
						te2.update(EnumFacing.EAST);
						te2.update(EnumFacing.SOUTH);
						te2.update(EnumFacing.WEST);
					}
				}
			});
			return null;
		}
	}
}