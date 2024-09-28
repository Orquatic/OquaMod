package net.orquatic.oquamod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.network.SimpleChannel;
import net.neoforged.neoforge.common.network.NetworkRegistry;
import net.neoforged.neoforge.network.message.NetworkDirection;
import net.orquatic.oquamod.OquaMod;

public class OquaModNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(OquaMod.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    // Register the packets
    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, KronaCountSyncPacket.class, KronaCountSyncPacket::encode, KronaCountSyncPacket::decode, KronaCountSyncPacket::handle);
    }

    // Send Krona count sync packet to client
    public static void sendKronaSyncPacket(ServerPlayer player, int kronaCount) {
        KronaCountSyncPacket packet = new KronaCountSyncPacket(kronaCount);
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
