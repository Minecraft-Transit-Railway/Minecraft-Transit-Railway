package MTR;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageNextTrain implements IMessage {

	private int x, y, z, platform;
	private boolean remove;

	public MessageNextTrain() {
	}

	public MessageNextTrain(int x, int y, int z, int platform) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.platform = platform;
		remove = platform < 0;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		y = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		z = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
		platform = ByteBufUtils.readVarShort(buf);
		remove = ByteBufUtils.readVarShort(buf) == 1;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, String.valueOf(x));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(y));
		ByteBufUtils.writeUTF8String(buf, String.valueOf(z));
		ByteBufUtils.writeVarShort(buf, platform);
		ByteBufUtils.writeVarShort(buf, remove ? 1 : 0);
	}

	public static class Handler implements IMessageHandler<MessageNextTrain, IMessage> {
		@Override
		public IMessage onMessage(final MessageNextTrain message, final MessageContext context) {
			IThreadListener mainThread = (WorldServer) context.getServerHandler().playerEntity.worldObj;
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					World worldIn = context.getServerHandler().playerEntity.worldObj;
					BlockPos pos = new BlockPos(message.x, message.y, message.z);
					TileEntity te = worldIn.getTileEntity(pos);
					if (te instanceof TileEntityNextTrainEntity) {
						TileEntityNextTrainEntity te2 = (TileEntityNextTrainEntity) te;
						te2.platformID = change(te2.platformID, message.platform, message.remove);
					}
					if (te instanceof TileEntityPIDS1Entity) {
						TileEntityPIDS1Entity te2 = (TileEntityPIDS1Entity) te;
						BlockPos pos2 = pos.offset(EnumFacing.getHorizontal(te2.getBlockMetadata()).rotateY());
						te2.platform = message.remove ? 0 : message.platform;
						try {
							TileEntityPIDS1Entity te3 = (TileEntityPIDS1Entity) worldIn.getTileEntity(pos2);
							te3.platform = message.remove ? 0 : message.platform;
						} catch (Exception e) {
						}
					}
				}
			});
			return null;
		}
	}

	public static int[] change(int[] array, int platform, boolean remove) {
		boolean repeat = false;
		int c = 99;
		for (int i = 99; i >= 0; i--) {
			if (array[i] == platform) {
				repeat = true;
				if (remove)
					array[i] = 0;
				break;
			}
			if (array[i] == 0)
				c = i;
		}
		if (!repeat && !remove)
			array[c] = platform;
		return array;
	}
}