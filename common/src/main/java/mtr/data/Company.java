package mtr.data;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class Company extends NameColorDataBase {

	public final Map<String, List<String>> employees = new HashMap<>();

	private static final String KEY_ROLES = "roles";
	private static final String KEY_ROLES_EMPLOYEES = "roles_employees";

	public Company() {
		super();
	}

	public Company(long id) {
		super(id);
	}

	public Company(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);

		messagePackHelper.iterateMapValue(KEY_ROLES, entry -> {
			final List<String> roles = new ArrayList<>(entry.getValue().asArrayValue().size());
			for (final Value role : entry.getValue().asArrayValue()) {
				roles.add(role.asStringValue().asString());
			}
			employees.put(entry.getKey().asStringValue().asString(), roles);
		});
	}

	@Deprecated
	public Company(CompoundTag compoundTag) {
		super(compoundTag);

		final CompoundTag tagExits = compoundTag.getCompound(KEY_ROLES);
		for (final String keyParent : tagExits.getAllKeys()) {
			final List<String> roles = new ArrayList<>();
			final CompoundTag tagRoles = tagExits.getCompound(keyParent);
			for (final String keyRole : tagRoles.getAllKeys()) {
				roles.add(tagRoles.getString(keyRole));
			}
			employees.put(keyParent, roles);
		}
	}

	public Company(FriendlyByteBuf packet) {
		super(packet);
		final int exitCount = packet.readInt();
		for (int i = 0; i < exitCount; i++) {
			final String parent = packet.readUtf(PACKET_STRING_READ_LENGTH);
			final List<String> roles = new ArrayList<>();
			final int roleCount = packet.readInt();
			for (int j = 0; j < roleCount; j++) {
				roles.add(packet.readUtf(PACKET_STRING_READ_LENGTH));
			}
			employees.put(parent, roles);
		}
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_ROLES);
		messagePacker.packMapHeader(employees.size());
		for (final Map.Entry<String, List<String>> entry : employees.entrySet()) {
			final String key = entry.getKey();
			final List<String> roles = entry.getValue();
			messagePacker.packString(key);
			messagePacker.packArrayHeader(roles.size());
			for (String role : roles) {
				messagePacker.packString(role);
			}
		}
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 1;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(employees.size());
		employees.forEach((parent, roles) -> {
			packet.writeUtf(parent);
			packet.writeInt(roles.size());
			roles.forEach(packet::writeUtf);
		});
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		switch (key) { //Ignore warning. More cases may come!
			case KEY_ROLES_EMPLOYEES:
				final String parent = packet.readUtf(PACKET_STRING_READ_LENGTH);
				if (parentExists(parent)) {
					employees.get(parent).clear();
					final int roleCount = packet.readInt();
					for (int i = 0; i < roleCount; i++) {
						employees.get(parent).add(packet.readUtf(PACKET_STRING_READ_LENGTH));
					}
				}
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	@Override
	protected boolean hasTransportMode() {
		return false;
	}

	public void setEmployees(String parent, Consumer<FriendlyByteBuf> sendPacket) {
		if (parentExists(parent)) {
			final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
			packet.writeLong(id);
			packet.writeUtf(transportMode.toString());
			packet.writeUtf(KEY_ROLES);
			packet.writeUtf(parent);
			packet.writeInt(employees.get(parent).size());
			employees.get(parent).forEach(packet::writeUtf);
			sendPacket.accept(packet);
		}
	}

	private void setEmployeeParent(String oldParent, String newParent) {
		if (parentExists(oldParent)) {
			final List<String> existingEmployees = employees.get(oldParent);
			existingEmployees.remove(oldParent);
			employees.put(newParent, existingEmployees == null ? new ArrayList<>() : existingEmployees);
		} else {
			employees.put(newParent, new ArrayList<>());
		}
	}

	private boolean parentExists(String parent) {
		return parent != null && employees.containsKey(parent);
	}

	public static long serializeEmployee(String employee) {
		final char[] characters = employee.toCharArray();
		long code = 0;
		for (final char character : characters) {
			code = code << 8;
			code += character;
		}
		return code;
	}

	public static String deserializeEmployee(long code) {
		StringBuilder employee = new StringBuilder();
		long charCodes = code;
		while (charCodes > 0) {
			employee.insert(0, (char) (charCodes & 0xFF));
			charCodes = charCodes >> 8;
		}
		return employee.toString();
	}
}
