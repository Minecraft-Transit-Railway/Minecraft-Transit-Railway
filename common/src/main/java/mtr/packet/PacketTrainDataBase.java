package mtr.packet;

import mtr.data.EnumHelper;
import mtr.data.NameColorDataBase;
import mtr.data.SerializedDataBase;
import mtr.data.TransportMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public abstract class PacketTrainDataBase implements IPacket {

	protected static <T extends NameColorDataBase, U extends ReentrantBlockableEventLoop<? extends Runnable>> void updateData(Set<T> dataSet, Map<Long, T> cacheMap, U minecraft, FriendlyByteBuf packet, PacketCallback packetCallback, BiFunction<Long, TransportMode, T> createDataWithId) {
		final FriendlyByteBuf packetFullCopy = new FriendlyByteBuf(packet.copy());
		final long id = packet.readLong();
		final TransportMode transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH));
		final String key = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		final FriendlyByteBuf packetCopy = new FriendlyByteBuf(packet.copy());
		minecraft.execute(() -> {
			final T data = cacheMap.get(id);
			if (data == null) {
				if (createDataWithId != null) {
					final T newData = createDataWithId.apply(id, transportMode);
					dataSet.add(newData);
					newData.update(key, packetCopy);
				}
			} else {
				data.update(key, packetCopy);
			}
			packetCallback.packetCallback(packetCopy, packetFullCopy);
		});
	}

	protected static <T extends NameColorDataBase, U extends ReentrantBlockableEventLoop<? extends Runnable>> void deleteData(Set<T> dataSet, U minecraft, FriendlyByteBuf packet, PacketCallback packetCallback) {
		final FriendlyByteBuf packetFullCopy = new FriendlyByteBuf(packet.copy());
		final long id = packet.readLong();
		minecraft.execute(() -> {
			dataSet.removeIf(data -> data.id == id);
			packetCallback.packetCallback(null, packetFullCopy);
		});
	}

	@FunctionalInterface
	protected interface PacketCallback {
		void packetCallback(FriendlyByteBuf updatePacket, FriendlyByteBuf fullPacket);
	}
}
