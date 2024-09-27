package net.orquatic.oquamod;

import net.minecraft.client.gui.components.AbstractWidget;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.orquatic.oquamod.block.Modblocks;
import net.orquatic.oquamod.gui.KeyBindings;
import net.orquatic.oquamod.item.ModCreativeModeTabs;
import net.orquatic.oquamod.item.Moditems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(OquaMod.MOD_ID)
public class OquaMod {
    public static final String MOD_ID = "oquamod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public OquaMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register mod events
        modEventBus.addListener(this::setup);

        // Register items, blocks, and creative tabs
        Moditems.ITEMS.register(modEventBus);
        Modblocks.BLOCKS.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        // Register key bindings
        KeyBindings.register(modEventBus);

        // Register server events
        NeoForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLClientSetupEvent event) {
        LOGGER.info("Setup complete for {}", OquaMod.MOD_ID);
    }
}