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

    // CoinHUDRenderer class for handling the HUD rendering event
    @EventBusSubscriber(modid = OquaMod.MOD_ID, value = Dist.CLIENT)
    public static class CoinHUDRenderer {

        // Use the GuiRenderEvent to render the coin HUD
        @SubscribeEvent
        public static void onGuiRender(RenderGuiEvent.Pre event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            // Make sure the player exists
            if (player != null) {
                OquaMod modInstance = OquaMod.INSTANCE; // Access the static instance of OquaMod

                // Lazily initialize the coin widget if it's not already set
                if (modInstance.coinWidget == null) {
                    modInstance.coinWidget = new CoinWidget(10, 10, 100, 16, Component.literal("Kronas"));
                }

                // Get and update the widget's krona count using the KronaCount class
                int kronaCount = KronaCount.getKronaCount(player);
                modInstance.coinWidget.setKronas(kronaCount);

                // Render the widget
                modInstance.coinWidget.renderWidget(event.getGuiGraphics(), 0, 0, event.getPartialTick());
            }
        }
    }
}
