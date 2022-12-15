package mtr.packet;

import mtr.data.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class PacketTrainDataBase implements IPacket {

	protected static <T extends NameColorDataBase, U extends ReentrantBlockableEventLoop<? extends Runnable>> void updateData(Set<T> dataSet, Map<Long, T> cacheMap, U minecraft, FriendlyByteBuf packet, PacketCallback packetCallback, BiFunction<Long, TransportMode, T> createDataWithId, BiConsumer<T, List<String>> dataCallback) {
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
					if (dataCallback != null) {
						dataCallback.accept(newData, new ArrayList<>());
					}
				}
			} else {
				final List<String> oldData = dataCallback == null ? new ArrayList<>() : RailwayDataLoggingModule.getData(data);
				data.update(key, packetCopy);
				if (dataCallback != null) {
					dataCallback.accept(data, oldData);
				}
			}
			packetCallback.packetCallback(packetCopy, packetFullCopy);
		});
	}

	protected static <T extends NameColorDataBase, U extends ReentrantBlockableEventLoop<? extends Runnable>> void deleteData(Set<T> dataSet, Map<Long, T> cacheMap, U minecraft, FriendlyByteBuf packet, PacketCallback packetCallback, Consumer<T> dataCallback) {
		final FriendlyByteBuf packetFullCopy = new FriendlyByteBuf(packet.copy());
		final long id = packet.readLong();
		minecraft.execute(() -> {
			final T data = cacheMap.get(id);
			if (data != null) {
				if (dataCallback != null) {
					dataCallback.accept(data);
				}
				dataSet.remove(data);
			}
			packetCallback.packetCallback(null, packetFullCopy);
		});
	}

	@FunctionalInterface
	protected interface PacketCallback {
		void packetCallback(FriendlyByteBuf updatePacket, FriendlyByteBuf fullPacket);
	}
}
