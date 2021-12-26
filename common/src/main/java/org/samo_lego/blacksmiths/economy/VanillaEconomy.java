package org.samo_lego.blacksmiths.economy;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class VanillaEconomy {
    /**
     * Returns how much more money player needs.
     * @param amount amount of money to check.
     * @param player player to check.
     * @return negative number if player needs more money, positive number if player has enough money, 0 if player has exact amount of money.
     */
    public double canAfford(double amount, ServerPlayer player) {
        ResourceLocation itemId = new ResourceLocation(CONFIG.costs.paymentItem);
        Item item = Registry.ITEM.get(itemId);
        int minNeeded = (int) Math.ceil(amount / CONFIG.costs.itemWorth);

        return player.getInventory().countItem(item) - minNeeded;
    }

    /**
     * Withdraws items from player, depending on config.
     * @param amount amount of money to withdraw (converted to items).
     * @param player player to withdraw from.
     */
    public void withdraw(double amount, ServerPlayer player) {
        ResourceLocation itemId = new ResourceLocation(CONFIG.costs.paymentItem);
        Item item = Registry.ITEM.get(itemId);
        int minNeeded = (int) Math.ceil(amount / CONFIG.costs.itemWorth);

        player.getInventory().clearOrCountMatchingItems(stack -> stack.getItem().equals(item), minNeeded, player.inventoryMenu.getCraftSlots());

        // Update the player's inventory
        player.containerMenu.broadcastChanges();
        player.inventoryMenu.slotsChanged(player.getInventory());
    }
}
