package mtr.packet;

import mtr.data.NameColorDataBase;
import mtr.data.SerializedDataBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.thread.ReentrantThreadExecutor;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public abstract class PacketTrainDataBase implements IPacket {

	protected static <T extends NameColorDataBase, U extends ReentrantThreadExecutor<? extends Runnable>> void updateData(Set<T> dataSet, Map<Long, T> cacheMap, U minecraft, PacketByteBuf packet, PacketCallback packetCallback, Function<Long, T> createDataWithId) {
		final PacketByteBuf packetFullCopy = new PacketByteBuf(packet.copy());
		final long id = packet.readLong();
		final String key = packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		final PacketByteBuf packetCopy = new PacketByteBuf(packet.copy());
		minecraft.execute(() -> {
			final T data = cacheMap.get(id);
			if (data == null) {
				if (createDataWithId != null) {
					final T newData = createDataWithId.apply(id);
					dataSet.add(newData);
					newData.update(key, packetCopy);
				}
			} else {
				data.update(key, packetCopy);
			}
			packetCallback.packetCallback(packetCopy, packetFullCopy);
		});
	}

	protected static <T extends NameColorDataBase, U extends ReentrantThreadExecutor<? extends Runnable>> void deleteData(Set<T> dataSet, U minecraft, PacketByteBuf packet, PacketCallback packetCallback) {
		final PacketByteBuf packetFullCopy = new PacketByteBuf(packet.copy());
		final long id = packet.readLong();
		minecraft.execute(() -> {
			dataSet.removeIf(data -> data.id == id);
			packetCallback.packetCallback(null, packetFullCopy);
		});
	}

	@FunctionalInterface
	protected interface PacketCallback {
		void packetCallback(PacketByteBuf updatePacket, PacketByteBuf fullPacket);
	}
}
