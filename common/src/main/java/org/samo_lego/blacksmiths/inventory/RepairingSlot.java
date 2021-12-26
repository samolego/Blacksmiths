package org.samo_lego.blacksmiths.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.samo_lego.blacksmiths.gui.RepairGUI;

public class RepairingSlot extends Slot {

    private final int slot;

    public RepairingSlot(Container container, int i) {
        super(container, i, 0, 0);
        this.slot = i;
    }

    /**
     * Allows placing only damaged items.
     * @param itemStack item to be placed.
     * @return true if item is allowed to be placed, false otherwise.
     */
    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.isDamaged();
    }

    /**
     * Also detects shift clicking the item.
     * @param player player taking the item.
     * @return true if item is allowed to be taken.
     */
    @Override
    public boolean mayPickup(Player player) {
        double enough = ((RepairGUI) this.container).canAfford(this.slot, System.currentTimeMillis());
         if (enough < 0) {
             ((RepairGUI) this.container).close();
            player.sendMessage(((RepairGUI) this.container).notEnoughMoneyMessage(enough * -1), player.getUUID());
            return false;
        }
        return true;
    }
}
