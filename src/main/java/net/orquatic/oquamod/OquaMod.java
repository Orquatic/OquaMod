package net.orquatic.oquamod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.orquatic.oquamod.block.Modblocks;
import net.orquatic.oquamod.item.ModCreativeModeTabs;
import net.orquatic.oquamod.item.Moditems;
import net.minecraft.nbt.CompoundTag;

@Mod(OquaMod.MOD_ID)
public class OquaMod {
    public static final String MOD_ID = "oquamod";
    private static ResourceLocation KRONA_TEXTURE;
    private static boolean textureLoaded = false;  // Boolean flag to check if texture is loaded

    public OquaMod() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        assert modEventBus != null;
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
            // Register client events with the game event bus
            NeoForge.EVENT_BUS.register(new OquaModClientEvents());

            // Initialize the ResourceLocation and set the textureLoaded flag to true
            KRONA_TEXTURE = ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "textures/item/krona.png");
            textureLoaded = true;  // Set the boolean to true once the texture is properly loaded
        }
    }

    // Class to handle the player's coin (Krona) data.
    public static class PlayerCoinData {
        private static final String KRONA_TAG = "Krona";

        public static void addCoins(Player player, int amount) {
            CompoundTag playerData = player.getPersistentData();
            int currentCoins = playerData.getInt(KRONA_TAG);
            playerData.putInt(KRONA_TAG, currentCoins + amount);
        }

        public static int getCoins(Player player) {
            CompoundTag playerData = player.getPersistentData();
            return playerData.getInt(KRONA_TAG);
        }
    }

    // Class to handle client-side GUI rendering events.
    public static class OquaModClientEvents {

        @SubscribeEvent
        public void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;

            if (player == null || !textureLoaded) return;  // Check if texture is loaded

            int kronaCount = PlayerCoinData.getCoins(player);
            int x = 10;  // X-coordinate for rendering
            int y = 10;  // Y-coordinate for rendering

            GuiGraphics guiGraphics = event.getGuiGraphics();
            RenderSystem.setShaderTexture(0, KRONA_TEXTURE);

            // Render the Krona icon at the given coordinates if the texture is loaded
            guiGraphics.blit(KRONA_TEXTURE, x, y, 0, 0, 16, 16, 16, 16);

            // Render the Krona count next to the icon
            guiGraphics.drawString(minecraft.font, "Kronas: " + kronaCount, x + 20, y + 5, 0xFFFFFF);
        }
    }
}
