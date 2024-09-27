package net.orquatic.oquamod.gui;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static KeyMapping OPEN_KRONA_SCREEN;

    // Register key mappings in the game
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(KeyBindings::registerKeyMappings);
    }

    // Define key mappings
    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        OPEN_KRONA_SCREEN = new KeyMapping(
                "key.oquamod.open_krona_screen", // Description
                GLFW.GLFW_KEY_GRAVE_ACCENT,     // Key (`)
                "key.category.oquamod"          // Category
        );

        // Register the key mapping to the event
        event.register(OPEN_KRONA_SCREEN);
    }
}
