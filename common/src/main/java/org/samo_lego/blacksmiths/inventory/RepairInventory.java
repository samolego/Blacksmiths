package org.samo_lego.blacksmiths.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class RepairInventory {

    private ItemStack repairingItem = ItemStack.EMPTY;
    private boolean finished = false;
    private long startTime = 0;
    private int startDamage = 0;

    public boolean startRepairing(ItemStack item) {
        boolean empty = repairingItem.isEmpty();
        if (empty && item.isDamaged()) {
            this.repairingItem = item;
            this.startTime = System.currentTimeMillis();  // Allows for "repairing" even if unloaded
            this.startDamage = item.getDamageValue();
        }
        return empty;
    }

    public ItemStack peek() {
        return this.repairingItem;
    }

    public ItemStack getItem(long now) {
        if (!repairingItem.isEmpty() && !this.finished) {
            long time = (now - this.startTime) / 1000;
            long dmg = Math.max(0, this.startDamage - time);
            if (dmg == 0)
                finished = true;
            this.repairingItem.setDamageValue((int) dmg);
            return this.repairingItem;
        }
        return repairingItem;
    }

    public void fromTag(CompoundTag tag) {
        this.repairingItem = ItemStack.of(tag.getCompound("Item"));
        if (CONFIG.workInUnloadedChunks) {
            this.startTime = tag.getLong("StartTime");
        } else {
            this.startTime = System.currentTimeMillis();
        }

        this.startDamage = tag.getInt("StartDamage");
    }

    public CompoundTag toTag() {
        CompoundTag item = new CompoundTag();
        CompoundTag main = new CompoundTag();

        // Gets the item with up-to-date damage value
        this.getItem(System.currentTimeMillis()).save(item);

        main.put("Item", item);
        main.putLong("StartTime", this.startTime);
        main.putInt("StartDamage", this.startDamage);

        return main;
    }
}
