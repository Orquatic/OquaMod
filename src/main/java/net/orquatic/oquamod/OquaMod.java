package net.orquatic.oquamod;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.orquatic.oquamod.block.Modblocks;
import net.orquatic.oquamod.gui.CoinWidget;
import net.orquatic.oquamod.gui.KeyBindings;
import net.orquatic.oquamod.gui.screens.KronaCount;
import net.orquatic.oquamod.item.ModCreativeModeTabs;
import net.orquatic.oquamod.item.Moditems;
import org.slf4j.Logger;

@Mod(OquaMod.MOD_ID)
public class OquaMod {
    public static final String MOD_ID = "oquamod";
    private static final Logger LOGGER = LogUtils.getLogger();
    private CoinWidget coinWidget; // Widget to display the player's krona count

    // A static instance of OquaMod to access globally
    public static OquaMod INSTANCE;

    public OquaMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register this mod instance globally
        INSTANCE = this;

        // Log instance creation
        LOGGER.info("OquaMod instance created");

        // Register mod events
        modEventBus.addListener(this::setup);

        // Register items, blocks, and creative tabs
        Moditems.ITEMS.register(modEventBus);
        Modblocks.BLOCKS.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        // Register key bindings
        KeyBindings.register(modEventBus);
    }

    private void setup(final FMLClientSetupEvent event) {
        LOGGER.info("Client setup complete for {}", OquaMod.MOD_ID);
    }

    // CoinHUDRenderer class for handling the HUD rendering event with throttling
    @EventBusSubscriber(modid = OquaMod.MOD_ID, value = Dist.CLIENT)
    public static class CoinHUDRenderer {

        private static long lastRenderTime = 0; // Tracks the last time the widget was rendered
        private static final long RENDER_COOLDOWN = 1000; // Cooldown period in milliseconds (1 second)

        @SubscribeEvent
        public static void onGuiRender(RenderGuiEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null) {
                OquaMod modInstance = OquaMod.INSTANCE;
                long currentTime = System.currentTimeMillis();

                // Throttling check: Only render if enough time has passed since the last render
                if (currentTime - lastRenderTime < RENDER_COOLDOWN) {
                    LOGGER.debug("Coin widget rendering skipped (throttled)");
                    return;
                }

                // Initialize the coin widget if it's not already set
                if (modInstance.coinWidget == null) {
                    modInstance.coinWidget = new CoinWidget(20, 20, 100, 16, Component.literal("Kronas"));
                    LOGGER.debug("Coin widget initialized");
                }

                // Get and update the widget's krona count using the KronaCount class
                int kronaCount = KronaCount.getKronaCount(player);
                LOGGER.debug("Krona count for player {}: {}", player.getName().getString(), kronaCount);

                modInstance.coinWidget.setKronas(kronaCount);

                // Render the widget
                modInstance.coinWidget.renderWidget(event.getGuiGraphics(), 0, 0, event.getPartialTick());
                LOGGER.debug("Coin widget rendered with {} kronas", kronaCount);

                // Update last render time to throttle future renders
                lastRenderTime = currentTime;
            } else {
                LOGGER.warn("Player not found. Skipping coin HUD rendering.");
            }
        }
    }
}
