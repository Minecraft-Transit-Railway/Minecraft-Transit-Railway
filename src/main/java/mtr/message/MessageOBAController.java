package mtr.message;

import io.netty.buffer.ByteBuf;
import mtr.MTR;
import mtr.tile.TileEntityOBAController;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOBAController implements IMessage {

	private BlockPos tileEntityPos;
	private String stopIds;

	public MessageOBAController() {
	}

	public MessageOBAController(BlockPos pos, String ids) {
		tileEntityPos = pos;
		stopIds = ids;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(tileEntityPos.getX());
		buf.writeInt(tileEntityPos.getY());
		buf.writeInt(tileEntityPos.getZ());
		ByteBufUtils.writeUTF8String(buf, stopIds);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tileEntityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		stopIds = ByteBufUtils.readUTF8String(buf);
	}

	public static class MessageOBAControllerServerHandler implements IMessageHandler<MessageOBAController, IMessage> {

		@Override
		public IMessage onMessage(MessageOBAController message, MessageContext ctx) {
			final TileEntity tileEntity = ctx.getServerHandler().player.world.getTileEntity(message.tileEntityPos);
			if (tileEntity instanceof TileEntityOBAController)
				((TileEntityOBAController) tileEntity).setStops(message.stopIds);
			return null;
		}
	}

	public static class MessageOBAControllerClientHandler implements IMessageHandler<MessageOBAController, IMessage> {

		@Override
		public IMessage onMessage(MessageOBAController message, MessageContext ctx) {
			final BlockPos pos = message.tileEntityPos;

			TileEntity tileEntity;
			if (ctx.side.isServer())
				tileEntity = ctx.getServerHandler().player.world.getTileEntity(pos);
			else
				tileEntity = Minecraft.getMinecraft().world.getTileEntity(pos);

			if (tileEntity instanceof TileEntityOBAController)
				((TileEntityOBAController) tileEntity).setStops(message.stopIds);

			if (ctx.side.isClient())
				Minecraft.getMinecraft().player.openGui(MTR.instance, 2, Minecraft.getMinecraft().world, pos.getX(), pos.getY(), pos.getZ());

			return null;
		}
	}
}