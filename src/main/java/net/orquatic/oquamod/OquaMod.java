package net.orquatic.oquamod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.orquatic.oquamod.block.Modblocks;
import net.orquatic.oquamod.item.ModCreativeModeTabs;
import net.orquatic.oquamod.item.Moditems;

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
        // Common setup code here
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new OquaModClientEvents());
            KRONA_TEXTURE = ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "textures/item/krona.png");
            textureLoaded = true; // Set the boolean to true once the texture is properly loaded
        }
    }

    // Class to handle the player's coin (Krona) data.
    public static class PlayerCoinData {
        private static final String KRONA_TAG = "Krona";

        // Adds coins to the player's data.
        public static void addCoins(Player player, int amount) {
            CompoundTag playerData = player.getPersistentData();
            int currentCoins = playerData.getInt(KRONA_TAG);
            playerData.putInt(KRONA_TAG, currentCoins + amount);
        }

        // Gets the current number of coins the player has.
        public static int getCoins(Player player) {
            CompoundTag playerData = player.getPersistentData();
            return playerData.getInt(KRONA_TAG);
        }

        // Sets the coin count directly.
        public static void setCoins(Player player, int amount) {
            CompoundTag playerData = player.getPersistentData();
            playerData.putInt(KRONA_TAG, amount);
        }
    }

    // Class to handle client-side GUI rendering events.
    public static class OquaModClientEvents {

        @SubscribeEvent
        public void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;

            if (player == null || !textureLoaded) return; // Check if texture is loaded

            int kronaCount = PlayerCoinData.getCoins(player); // Get the player's current coin count
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

    // Event listener to detect when a Krona is crafted and add coins to the player's data.
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();  // Get the player who crafted the item
        ItemStack craftedItem = event.getCrafting();  // Get the crafted item

        // Check if the crafted item is the Krona
        if (craftedItem.getItem().equals(Moditems.KRONA.get())) {
            int kronaAmount = craftedItem.getCount();  // Get the number of Kronas crafted
            PlayerCoinData.addCoins(player, kronaAmount);  // Add crafted Kronas to the player's coin data

            // Optional: Print to console for debugging
            System.out.println("Player crafted " + kronaAmount + " Kronas. Total: " + PlayerCoinData.getCoins(player));
        }
    }

    // Method to synchronize the Krona count with the client (if needed)
    public static void syncKronaCount(Player player) {
        // Use getCommandSenderWorld() to check the client/server side
        if (!player.getCommandSenderWorld().isClientSide()) {
            int kronaCount = PlayerCoinData.getCoins(player);
            // You can use a custom packet here to send the kronaCount to the client for display
        }
    }
}
