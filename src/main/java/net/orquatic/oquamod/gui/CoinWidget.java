package net.orquatic.oquamod.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.orquatic.oquamod.OquaMod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.FontUIResource;

public class CoinWidget extends AbstractWidget {
    // Logger for debugging
    private static final Logger LOGGER = LoggerFactory.getLogger(CoinWidget.class);

    // Use ResourceLocation directly without DeferredRegister
    private ResourceLocation KRONA_TEXTURE = new ResourceLocation("oquamod" + ":textures/item/krona.png");
    private int kronas;

    public CoinWidget(int x, int y, int width, int height, Component title) {
        super(x, y, width, height, title);
        // Log when widget is initialized
        LOGGER.info("CoinWidget initialized with position ({}, {}) and size ({}, {})", x, y, width, height);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        try {
            // Debug texture loading
            LOGGER.info("Attempting to bind Krona texture: {}", KRONA_TEXTURE);

            // Bind the texture
            RenderSystem.setShaderTexture(0, KRONA_TEXTURE);

            // Render the Krona icon
            guiGraphics.blit(KRONA_TEXTURE, this.getX(), this.getY(), 0, 0, 16, 16);

            // Render the Krona count next to the icon
            String kronaText = "Kronas: " + kronas;
            Minecraft mc = Minecraft.getInstance();
            guiGraphics.drawString(mc.font, kronaText, this.getX() + 20, this.getY() + 5, 0xFFFFFF);

            // Log successful render
            LOGGER.info("Rendered Krona texture and Krona count successfully.");

        } catch (Exception e) {
            // Log any errors during rendering
            LOGGER.error("Error while rendering CoinWidget", e);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // Placeholder for narration (unused in this context)
    }

    // Method to update the number of Kronas
    public void setKronas(int kronas) {
        this.kronas = kronas;
        // Log Krona update
        LOGGER.info("Updated Kronas to: {}", kronas);
    }
}
