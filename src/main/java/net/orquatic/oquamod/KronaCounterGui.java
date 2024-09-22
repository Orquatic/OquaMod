package net.orquatic.oquamod;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent; // Updated for rendering HUD
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.SubscribeEventListener;
import net.orquatic.oquamod.item.Moditems; // Import your custom item class

public class KronaCounterGui {

    // Path to your Krona texture
    private static final ResourceLocation KRONA_TEXTURE = ResourceLocation.tryParse("oquamod:textures/item/krona.png");



    // Subscribe to the RenderGuiEvent.Post event that renders HUD elements
    public static void onRenderGuiOverlay(RenderGuiLayerEvent.Post event) {
        Player player = Minecraft.getInstance().player; // Get the player entity

        if (player == null) {
            return; // Safeguard if player is null
        }

        int kronaCount = getKronaCount(player); // Get the count of Kronas in the player's inventory
        renderKronaCounter(event.getGuiGraphics(), kronaCount); // Render the counter
    }

    // Method to count Kronas in player inventory
    private static int getKronaCount(Player player) {
        return player.getInventory().countItem(Moditems.KRONA.get()); // Count Kronas in the player's inventory
    }

    // Render the Krona icon and counter text on the HUD
    private static void renderKronaCounter(GuiGraphics guiGraphics, int kronaCount) {
        assert KRONA_TEXTURE != null;
        RenderSystem.setShaderTexture(0, KRONA_TEXTURE); // Bind Krona texture
        guiGraphics.blit(KRONA_TEXTURE, 10, 10, 0, 0, 16, 16); // Draw Krona icon at x=10, y=10
        guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(kronaCount), 30, 15, 0xFFFFFF); // Render Krona count next to the icon
    }
}
