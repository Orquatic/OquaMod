package net.orquatic.oquamod.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class OquaModNetwork {

    // Register payload handlers
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1.0");  // Register payloads with version 1.0

        // Ensure the namespace is correct and follows "namespace:path" format
        registrar.playToClient(KronaCountSyncPacket.TYPE, KronaCountSyncPacket.CODEC, KronaCountSyncPacket::handle);
    }

    // Send Krona sync packet to the client
    public static void sendKronaSync(ServerPlayer player, int kronaCount) {
        player.connection.send(new KronaCountSyncPacket(kronaCount).toVanillaClientbound());  // Send sync packet to client
    }
}
