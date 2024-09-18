package net.orquatic.oquamod.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.orquatic.oquamod.OquaMod;
import net.orquatic.oquamod.block.Modblocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, OquaMod.MOD_ID);

    public static final Supplier<CreativeModeTab> OQUAMOD_ITEMS_TAB = CREATIVE_MODE_TAB.register("oquamod_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Moditems.OQUANITE.get()))
                    .title(Component.translatable("creativetab.oquamod.oquamod_items"))
                    .displayItems((ItemDisplayParameters, output)-> {
                        output.accept(new ItemStack(Moditems.OQUANITE.get()));
                        output.accept(new ItemStack(Moditems.RAW_OQUANITE.get()));
                    }).build());

        public static final Supplier<CreativeModeTab> OQUAMOD_BLOCK_TAB = CREATIVE_MODE_TAB.register("oquamod_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Modblocks.OQUANITE_ORE))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "oquamod_items_tab"))
                    .title(Component.translatable("creativetab.oquamod.oquamod_blocks"))
                    .displayItems((ItemDisplayParameters, output)-> {
                        output.accept(new ItemStack(Modblocks.OQUANITE_ORE.get()));
                        output.accept(new ItemStack(Modblocks.OQUANITE_DEEPSLATE_ORE.get()));
                        output.accept(new ItemStack(Modblocks.OQUANITE_BLOCK.get()));
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
