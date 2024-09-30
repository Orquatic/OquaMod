package net.orquatic.oquamod.network;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.orquatic.oquamod.OquaMod;
import org.jetbrains.annotations.NotNull;

public class KronaCountSyncPacket implements CustomPacketPayload {

    // Define the packet type and codec with the correct namespace and path as a String
    public static final CustomPacketPayload.Type<KronaCountSyncPacket> TYPE = CustomPacketPayload.createType(OquaMod.MOD_ID + ":krona_sync");  // Corrected to pass the ID as a String
    public static final StreamCodec<FriendlyByteBuf, KronaCountSyncPacket> CODEC = StreamCodec.of(KronaCountSyncPacket::encode, KronaCountSyncPacket::decode);

    private final int kronaCount;

    public KronaCountSyncPacket(int kronaCount) {
        this.kronaCount = kronaCount;
    }

    // Encode the packet
    public static void encode(FriendlyByteBuf buf, KronaCountSyncPacket packet) {
        buf.writeInt(packet.kronaCount);  // Write Krona count into the buffer
    }

    // Decode the packet
    public static KronaCountSyncPacket decode(FriendlyByteBuf buf) {
        return new KronaCountSyncPacket(buf.readInt());  // Read Krona count from the buffer
    }

    // Handle the packet with IPayloadContext
    public static void handle(KronaCountSyncPacket packet, IPayloadContext context) {
        Player player = context.player();  // Get player from context

        if (context.flow() == PacketFlow.SERVERBOUND && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getEntityData().set(OquaMod.KRONA_COUNT_KEY, packet.kronaCount);  // Update server-side Krona count

            // Sync to the client
            OquaModNetwork.sendKronaSync(serverPlayer, packet.kronaCount);
        } else if (context.flow() == PacketFlow.CLIENTBOUND) {
            // Update client-side Krona count for UI
            player.getEntityData().set(OquaMod.KRONA_COUNT_KEY, packet.kronaCount);
        }

        // If tasks need to be done on the main thread, enqueue them
        context.enqueueWork(() -> {
            // Main-thread tasks if needed
        });
    }

    @Override
    public CustomPacketPayload.@NotNull Type<?> type() {
        return TYPE;  // Return the packet type
    }
}
