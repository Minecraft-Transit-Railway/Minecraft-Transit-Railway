package org.mtr.mod.packet;

import net.minecraft.nbt.NbtCompound;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public class PacketUpdateCustomRail extends PacketHandler {

    private final int speed;
    private final boolean isOneWay;

    public PacketUpdateCustomRail(PacketBufferReceiver packetBufferReceiver) {
        speed = packetBufferReceiver.readInt();
        isOneWay = packetBufferReceiver.readBoolean();
    }

    public PacketUpdateCustomRail(int speed, boolean isOneWay) {
        this.speed = speed;
        this.isOneWay = isOneWay;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeInt(speed);
        packetBufferSender.writeBoolean(isOneWay);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final ItemStack itemStack = serverPlayerEntity.getMainHandStack();

        NbtCompound nbt = itemStack.data.getOrCreateNbt();
        nbt.putInt("railSpeed", speed);
        nbt.putBoolean("isOneWay", isOneWay);
    }
}
