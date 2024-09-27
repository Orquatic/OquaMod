package net.orquatic.oquamod.gui.screens;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.orquatic.oquamod.item.Moditems;

public class KronaCount {

    // Method to calculate the total number of kronas in a player's inventory
    public static int getKronaCount(Player player) {
        int totalKronas = 0;

        // Iterate through the player's inventory to count all the krona items
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == Moditems.KRONA.get()) { // Check if the item is a krona
                totalKronas += itemStack.getCount(); // Add the number of kronas in the stack
            }
        }

        return totalKronas; // Return the total kronas the player is holding
    }
}
