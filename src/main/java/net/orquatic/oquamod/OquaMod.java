package net.orquatic.oquamod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.orquatic.oquamod.block.Modblocks;
import net.orquatic.oquamod.item.ModCreativeModeTabs;
import net.orquatic.oquamod.item.Moditems;
import net.orquatic.oquamod.network.KronaCountSyncPacket;
import net.orquatic.oquamod.network.OquaModNetwork;

@Mod(OquaMod.MOD_ID)
public class OquaMod {
    public static final String MOD_ID = "oquamod";
    private static ResourceLocation KRONA_TEXTURE;
    private static boolean textureLoaded = false; // Boolean flag to check if texture is loaded

    public OquaMod() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        ModCreativeModeTabs.register(modEventBus);
        Moditems.ITEMS.register(modEventBus);
        Modblocks.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        OquaModNetwork.registerPackets(); // Register network packets
        registerCapabilities();  // Register capability attacher
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new OquaModClientEvents());
            KRONA_TEXTURE = ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "textures/item/krona.png");
            textureLoaded = true; // Set the boolean to true once the texture is properly loaded
        }
    }

    // Register entity capabilities
    private void registerCapabilities() {
        EntityCapability.createVoid(
                ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "player_coin_capability"), PlayerCoinCapability.class
        );
    }

    // Player Coin Capability class
    public static class PlayerCoinCapability {
        public static final EntityCapability<PlayerCoinCapability, Void> INSTANCE = EntityCapability.createVoid(
                ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "player_coin_capability"), PlayerCoinCapability.class
        );

        private int coins;

        public void addCoins(int amount) {
            this.coins += amount;
        }

        public int getCoins() {
            return coins;
        }

        public void setCoins(int amount) {
            this.coins = amount;
        }
    }

    // Class to handle client-side GUI rendering events.
    public static class OquaModClientEvents {

        @SubscribeEvent
        public void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;

            if (player == null || !textureLoaded) return; // Check if texture is loaded

            // Use EntityCapability to access the player's capability
            PlayerCoinCapability coinsCap = player.getCapability(PlayerCoinCapability.INSTANCE, null);
            if (coinsCap != null) {
                int kronaCount = coinsCap.getCoins(); // Get the player's current coin count
                int x = 10; // X-coordinate for rendering
                int y = 10; // Y-coordinate for rendering

                GuiGraphics guiGraphics = event.getGuiGraphics();
                RenderSystem.setShaderTexture(0, KRONA_TEXTURE);

                // Render the Krona icon at the given coordinates if the texture is loaded
                guiGraphics.blit(KRONA_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);

                // Render the Krona count next to the icon
                guiGraphics.drawString(minecraft.font, "Kronas: " + kronaCount, x + 20, y + 5, 0xFFFFFF);
            }
        }
    }

    // Event listener to detect when a Krona is crafted and add coins to the player's data.
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();  // Get the player who crafted the item
        ItemStack craftedItem = event.getCrafting();  // Get the crafted item

        // Check if the crafted item is the Krona
        if (craftedItem.getItem().equals(Moditems.KRONA.get())) {
            int kronaAmount = craftedItem.getCount();  // Get the number of Kronas crafted

            // Access the player's capability
            PlayerCoinCapability coinsCap = player.getCapability(PlayerCoinCapability.INSTANCE, null);
            if (coinsCap != null) {
                coinsCap.addCoins(kronaAmount);  // Add crafted Kronas to the player's coin data
                // Optional: Print to console for debugging
                System.out.println("Player crafted " + kronaAmount + " Kronas. Total: " + coinsCap.getCoins());
            }
        }
    }

    // Method to synchronize the Krona count with the client (if needed)
    public static void syncKronaCount(Player player) {
        // Use getCommandSenderWorld() to check the client/server side
        if (!player.getCommandSenderWorld().isClientSide()) {
            PlayerCoinCapability coinsCap = player.getCapability(PlayerCoinCapability.INSTANCE, null);
            if (coinsCap != null) {
                int kronaCount = coinsCap.getCoins();
                OquaModNetwork.CHANNEL.sendToPlayer(
                        player, new KronaCountSyncPacket(kronaCount)
                );
            }
        }
    }
}
