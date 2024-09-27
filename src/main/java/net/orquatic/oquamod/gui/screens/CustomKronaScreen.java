package net.orquatic.oquamod.gui.screens;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiGraphics;

public class CustomKronaScreen extends Screen {

    public CustomKronaScreen() {
        super(Component.literal("Krona Screen"));
    }

    @Override
    protected void init() {
        // Initialize Krona screen elements
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Render Krona screen contents
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, "Krona Balance: 100", this.width / 2, 15, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
