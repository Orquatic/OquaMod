package net.orquatic.oquamod.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.orquatic.oquamod.OquaMod;

public class Moditems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(OquaMod.MOD_ID);

    public static final DeferredItem<Item> OQUANITE = ITEMS.register("oquanite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> RAW_OQUANITE = ITEMS.register("raw_oquanite",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SICKLE = ITEMS.register("sickle",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> KRONA = ITEMS.register("krona",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> OQUANITE_INGOT = ITEMS.register("oquanite_ingot",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
