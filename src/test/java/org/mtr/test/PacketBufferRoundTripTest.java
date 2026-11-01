package org.mtr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;

import java.util.Random;

/**
 * Sends a packet large enough to span several chunks and reads every value back.
 *
 * <p>The sender splits at a chunk boundary before a value would straddle it, and the receiver
 * moves to the next chunk only once the current one is fully read, so the two agree only if each
 * chunk carries exactly the bytes written into it. The mix of widths here, with strings that
 * end near the boundaries, is what a fetch of arrivals looks like on the wire.</p>
 */
public final class PacketBufferRoundTripTest {

	@Test
	public void valuesSurviveChunking() {
		final Random random = new Random(1);
		final int count = 20000;
		final long[] longs = new long[count];
		final int[] ints = new int[count];
		final String[] strings = new String[count];
		final PacketBufferSender sender = new PacketBufferSender();

		for (int i = 0; i < count; i++) {
			longs[i] = random.nextLong();
			ints[i] = random.nextInt();
			strings[i] = i % 7 == 0 ? "" : Long.toString(random.nextLong(), 36);
			sender.writeLong(longs[i]);
			sender.writeInt(ints[i]);
			sender.writeString(strings[i]);
			sender.writeBoolean(i % 3 == 0);
			sender.writeDouble(i / 3D);
		}

		final ObjectArrayList<byte[]> wire = new ObjectArrayList<>();
		sender.send(wire::add, Runnable::run);
		Assertions.assertTrue(wire.size() > 3, "the packet should have needed several chunks, got " + wire.size());

		final boolean[] completed = {false};
		for (final byte[] chunk : wire) {
			PacketBufferReceiver.receive(chunk, receiver -> {
				for (int i = 0; i < count; i++) {
					Assertions.assertEquals(longs[i], receiver.readLong(), "long " + i);
					Assertions.assertEquals(ints[i], receiver.readInt(), "int " + i);
					Assertions.assertEquals(strings[i], receiver.readString(), "string " + i);
					Assertions.assertEquals(i % 3 == 0, receiver.readBoolean(), "boolean " + i);
					Assertions.assertEquals(i / 3D, receiver.readDouble(), "double " + i);
				}
				completed[0] = true;
			}, Runnable::run);
		}

		Assertions.assertTrue(completed[0], "the receiver never saw the last chunk");
	}
}
