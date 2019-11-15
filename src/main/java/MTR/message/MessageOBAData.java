package mtr.message;

import io.netty.buffer.ByteBuf;
import mtr.tile.TileEntityOBAController;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageOBAData implements IMessage {

	private BlockPos tileEntityPos;
	private EnumFacing direction;
	private String title;
	private String[] routes, destinations, arrivals, vehicleIds, statuses;

	public MessageOBAData() {
	}

	public MessageOBAData(BlockPos pos, EnumFacing displayBlock, String titleIn, String[] routesIn, String[] destinationsIn, String[] arrivalsIn, String[] vehicleIdsIn, String[] statusIn) {
		tileEntityPos = pos;
		direction = displayBlock;
		title = titleIn;
		routes = routesIn;
		destinations = destinationsIn;
		arrivals = arrivalsIn;
		vehicleIds = vehicleIdsIn;
		statuses = statusIn;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(tileEntityPos.getX());
		buf.writeInt(tileEntityPos.getY());
		buf.writeInt(tileEntityPos.getZ());
		buf.writeInt(direction == null ? -1 : direction.getIndex());
		ByteBufUtils.writeUTF8String(buf, title);
		buf.writeInt(routes.length);
		for (int i = 0; i < routes.length; i++) {
			ByteBufUtils.writeUTF8String(buf, routes[i]);
			ByteBufUtils.writeUTF8String(buf, destinations[i]);
			ByteBufUtils.writeUTF8String(buf, arrivals[i]);
			ByteBufUtils.writeUTF8String(buf, vehicleIds[i]);
			ByteBufUtils.writeUTF8String(buf, statuses[i]);
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tileEntityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		final int directionIndex = buf.readInt();
		direction = directionIndex == -1 ? null : EnumFacing.getFront(directionIndex);
		title = ByteBufUtils.readUTF8String(buf);
		final int length = buf.readInt();
		routes = new String[length];
		destinations = new String[length];
		arrivals = new String[length];
		vehicleIds = new String[length];
		statuses = new String[length];
		for (int i = 0; i < length; i++) {
			routes[i] = ByteBufUtils.readUTF8String(buf);
			destinations[i] = ByteBufUtils.readUTF8String(buf);
			arrivals[i] = ByteBufUtils.readUTF8String(buf);
			vehicleIds[i] = ByteBufUtils.readUTF8String(buf);
			statuses[i] = ByteBufUtils.readUTF8String(buf);
		}
	}

	public static class MessageOBADataHandler implements IMessageHandler<MessageOBAData, IMessage> {

		@Override
		public IMessage onMessage(MessageOBAData message, MessageContext ctx) {
			final TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.tileEntityPos);
			if (tileEntity instanceof TileEntityOBAController) {
				final TileEntityOBAController controller = (TileEntityOBAController) tileEntity;
				controller.displayBlock = message.direction;
				controller.title = message.title;
				controller.routes = message.routes;
				controller.destinations = message.destinations;
				controller.arrivals = message.arrivals;
				controller.vehicleIds = message.vehicleIds;
				controller.statuses = message.statuses;
			}
			return null;
		}
	}
}