package net.orquatic.oquamod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
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
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.orquatic.oquamod.network.OquaModNetwork;

@Mod(OquaMod.MOD_ID)
public class OquaMod {
    public static final String MOD_ID = "oquamod";
    private static ResourceLocation KRONA_TEXTURE;
    private static boolean textureLoaded = false;


    // EntityDataAccessor for Krona count
    public static final EntityDataAccessor<Integer> KRONA_COUNT_KEY = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    public OquaMod() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerPayloadHandlers);  // Register payload handlers
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Removed OquaModNetwork.registerPackets(); as it no longer exists
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.register(new OquaModClientEvents());
            // Use tryParse to avoid private constructor error
            KRONA_TEXTURE = ResourceLocation.tryParse(OquaMod.MOD_ID + ":textures/item/krona.png");  // Corrected ResourceLocation creation
            textureLoaded = true;
        }
    }

    @SubscribeEvent
    public void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        OquaModNetwork.registerPayloadHandlers(event);
    }


    // Method to initialize the Krona count on a new player
    public static void initKronaCount(Player player) {
        player.getEntityData().set(KRONA_COUNT_KEY, 0);  // Initialize Krona count to 0
    }

    // Method to sync Krona count between server and client
    public static void syncKronaCount(Player player) {
        if (!player.getCommandSenderWorld().isClientSide()) {
            int kronaCount = player.getEntityData().get(OquaMod.KRONA_COUNT_KEY);  // Fetch Krona count
            OquaModNetwork.sendKronaSync((ServerPlayer) player, kronaCount);  // Send packet to sync with client
        }
    }



    // Class to handle client-side GUI rendering
    public static class OquaModClientEvents {

        @SubscribeEvent
        public void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;

            if (player == null || !textureLoaded) return;

            int kronaCount = player.getEntityData().get(KRONA_COUNT_KEY);  // Fetch Krona count
            GuiGraphics guiGraphics = event.getGuiGraphics();
            RenderSystem.setShaderTexture(0, KRONA_TEXTURE);

            // Render the Krona icon and count
            guiGraphics.blit(KRONA_TEXTURE, 10, 10, 0, 0, 16, 16, 16, 16);
            guiGraphics.drawString(minecraft.font, "Kronas: " + kronaCount, 30, 15, 0xFFFFFF);
        }
    }
}
