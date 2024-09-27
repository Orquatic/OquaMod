package net.orquatic.oquamod;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.NeoForge;
import net.orquatic.oquamod.gui.KeyBindings;
import net.orquatic.oquamod.gui.screens.CustomKronaScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class KeyInputHandler {

    // Initialize key input handling
    public static void init() {
        NeoForge.EVENT_BUS.register(KeyInputHandler.class);
    }

    // Event handler for client-side tick event (for checking key states)
    @SubscribeEvent
    public static void onClientTick(LevelTickEvent event) {
        // Only handle input while the game is running
        if (Minecraft.getInstance().screen == null) {
            if (KeyBindings.OPEN_KRONA_SCREEN.consumeClick()) {
                // Open the Krona Screen when the key is pressed
                Minecraft.getInstance().setScreen(new CustomKronaScreen());
            }
        }
    }
}
