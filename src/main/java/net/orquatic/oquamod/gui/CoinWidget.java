package net.orquatic.oquamod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.orquatic.oquamod.OquaMod;

public class CoinWidget extends AbstractWidget {

    // Correct ResourceLocation format (no leading ':')
    private static final ResourceLocation KRONA_TEXTURE = ResourceLocation.fromNamespaceAndPath(OquaMod.MOD_ID, "textures/item/krona.png");

    private int kronas; // Holds the krona count

    // Constructor for the widget
    public CoinWidget(int x, int y, int width, int height, Component title) {
        super(x, y, width, height, title);
        this.kronas = 0;  // Initialize krona count to 0
    }

    // Rendering the widget
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        // Bind the texture for the krona icon
        RenderSystem.setShaderTexture(0, KRONA_TEXTURE);

        // Blit the krona texture (draw the image) at the widget's position
        guiGraphics.blit(KRONA_TEXTURE, this.getX(), this.getY(), 0, 0, 16, 16);

        // Draw the krona count text next to the icon
        String kronaText = "Kronas: " + kronas;
        guiGraphics.drawString(mc.font, kronaText, this.getX() + 20, this.getY() + 5, 0xFFFFFF); // White text
    }

    // Narration method, required for accessibility
    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // Not used but required for the widget
    }

    // Method to update the number of kronas
    public void setKronas(int kronas) {
        this.kronas = kronas;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, DeltaTracker partialTick) {

    }
}
