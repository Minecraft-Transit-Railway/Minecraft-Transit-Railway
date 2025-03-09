package org.mtr.packet;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class PacketBufferSender {

	private int capacity;
	private ByteBuf currentByteBuf;
	private final ObjectArrayList<ByteBuf> byteBufObjects = new ObjectArrayList<>();
	private final Supplier<ByteBuf> createInstance;

	private static final int HEADER_BYTES = 8 + 4 + 4;
	private static final int MAX_PACKET_BYTES = 0x7FFF - HEADER_BYTES;

	public PacketBufferSender(Supplier<ByteBuf> createInstance) {
		this.createInstance = createInstance;
		create();
	}

	public void writeBoolean(boolean data) {
		write(1);
		currentByteBuf.writeBoolean(data);
	}

	public void writeChar(char data) {
		write(2);
		currentByteBuf.writeChar(data);
	}

	public void writeInt(int data) {
		write(4);
		currentByteBuf.writeInt(data);
	}

	public void writeFloat(float data) {
		write(4);
		currentByteBuf.writeFloat(data);
	}

	public void writeLong(long data) {
		write(8);
		currentByteBuf.writeLong(data);
	}

	public void writeDouble(double data) {
		write(8);
		currentByteBuf.writeDouble(data);
	}

	public void writeString(String data) {
		final char[] characters = data.toCharArray();
		writeInt(characters.length);
		for (char character : characters) {
			writeChar(character);
		}
	}

	public void send(Consumer<ByteBuf> consumer, Consumer<Runnable> scheduler) {
		byteBufObjects.add(currentByteBuf);
		final long id = new Random().nextLong();
		final ObjectArrayList<Runnable> queue = new ObjectArrayList<>();
		for (int i = 0; i < byteBufObjects.size(); i++) {
			final ByteBuf byteBuf = byteBufObjects.get(i);
			final int writerIndex = byteBuf.writerIndex();
			byteBuf.resetWriterIndex();
			byteBuf.writeLong(id);
			byteBuf.writeInt(i);
			byteBuf.writeInt(byteBufObjects.size());
			byteBuf.writerIndex(writerIndex);
			queue.add(() -> consumer.accept(byteBuf));
		}
		schedule(queue, scheduler);
	}

	private void write(int size) {
		if (capacity + size >= MAX_PACKET_BYTES) {
			byteBufObjects.add(currentByteBuf);
			create();
			capacity = 0;
		}
		capacity += size;
	}

	private void create() {
		currentByteBuf = createInstance.get();
		currentByteBuf.writeLong(0);
		currentByteBuf.writeInt(0);
		currentByteBuf.writeInt(0);
	}

	private static void schedule(ObjectArrayList<Runnable> queue, Consumer<Runnable> scheduler) {
		if (!queue.isEmpty()) {
			scheduler.accept(() -> {
				queue.remove(0).run();
				schedule(queue, scheduler);
			});
		}
	}
}
