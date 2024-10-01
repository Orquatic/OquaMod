package net.orquatic.oquamod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.orquatic.oquamod.krona.KronaDataCapability;

public class KronaCountSyncPacket implements CustomPacketPayload {

    private final int kronaCount;

    public KronaCountSyncPacket(int kronaCount) {
        this.kronaCount = kronaCount;
    }

    public static void encode(FriendlyByteBuf buf, KronaCountSyncPacket packet) {
        buf.writeInt(packet.kronaCount);
    }

    public static KronaCountSyncPacket decode(FriendlyByteBuf buf) {
        return new KronaCountSyncPacket(buf.readInt());
    }

    public static void handle(KronaCountSyncPacket packet, IPayloadContext context) {
        Player player = context.player();

        if (player instanceof ServerPlayer serverPlayer) {
            // Update server-side krona count using capabilities
            player.getCapability(KronaDataCapability.KRONA_CAPABILITY).ifPresent(krona -> {
                krona.setKronaCount(packet.kronaCount);
            });
        } else {
            // Client-side update for UI or display purposes
            player.getCapability(IKronaData.h).ifPresent(krona -> {
                krona.setKronaCount(packet.kronaCount);
            });
        }
    }
}
