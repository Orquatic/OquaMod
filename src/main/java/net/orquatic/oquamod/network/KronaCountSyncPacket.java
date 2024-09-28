package net.orquatic.oquamod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.api.network.message.Message;
import net.neoforged.neoforge.api.network.message.MessageContext;

public class KronaCountSyncPacket implements Message<KronaCountSyncPacket> {
    private final int kronaCount;

    // Constructor for sending the packet
    public KronaCountSyncPacket(int kronaCount) {
        this.kronaCount = kronaCount;
    }

    // Constructor for receiving the packet
    public KronaCountSyncPacket(FriendlyByteBuf buf) {
        this.kronaCount = buf.readInt();
    }

    // Serializes the packet data to send it
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(kronaCount);
    }

    // Handles the packet when it's received on the client
    @Override
    public void handle(MessageContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            player.getCapability(OquaMod.PlayerCoinCapability.INSTANCE).ifPresent(cap -> {
                cap.setCoins(kronaCount);
            });
        }
    }

    public int getKronaCount() {
        return kronaCount;
    }
}
