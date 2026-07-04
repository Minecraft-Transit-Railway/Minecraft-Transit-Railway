package org.mtr.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import org.mtr.MTR;

public final class DataComponentTypes {

	public static final ObjectHolder<DataComponentType<BlockPos>> START_POS = RegistryServer.registerDataComponentType("start_pos", () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).build());
	public static final ObjectHolder<DataComponentType<String>> TRANSPORT_MODE = RegistryServer.registerDataComponentType("transport_mode", () -> DataComponentType.<String>builder().persistent(Codec.STRING).build());
	public static final ObjectHolder<DataComponentType<Integer>> BLOCK_ID = RegistryServer.registerDataComponentType("block_id", () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build());
	public static final ObjectHolder<DataComponentType<Long>> DEPOT_ID = RegistryServer.registerDataComponentType("depot_id", () -> DataComponentType.<Long>builder().persistent(Codec.LONG).build());
	public static final ObjectHolder<DataComponentType<Long>> EXPIRY_TIME = RegistryServer.registerDataComponentType("expiry_time", () -> DataComponentType.<Long>builder().persistent(Codec.LONG).build());

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway data component types");
	}
}
