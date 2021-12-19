package org.samo_lego.blacksmiths.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class RepairingSlot extends Slot {
    public RepairingSlot(Container container, int i) {
        super(container, i, 0, 0);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return itemStack.isDamaged();
    }
}
