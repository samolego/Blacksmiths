package org.samo_lego.blacksmiths.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;

public class RepairInventory {

    private final BlacksmithProfession profession;
    private ItemStack repairingItem = ItemStack.EMPTY;
    private boolean finished = false;
    private long startTime = 0;
    private int startDamage = 0;

    public RepairInventory(BlacksmithProfession profession) {
        this.profession = profession;
    }

    /**
     * Tries to start repairing the item.
     * @param item item to repair.
     * @return true if repairing was started.
     */
    public boolean startRepairing(ItemStack item) {
        boolean empty = this.repairingItem.isEmpty();  // Current item if any
        if (empty && item.isDamaged()) {
            this.repairingItem = item;
            this.startTime = System.currentTimeMillis();  // Allows for "repairing" even if unloaded
            this.startDamage = item.getDamageValue();
        }
        return empty;  // If there was an item already, return false
    }

    public ItemStack peek() {
        return this.repairingItem;
    }

    public ItemStack getItem(long now) {
        if (!repairingItem.isEmpty() && !this.finished) {
            long dmgDecrease = (long) ((now - this.startTime) * this.profession.getDurabilityPerSecond() / 1000);
            long dmg = this.startDamage - dmgDecrease;
            if (dmg <= 0)
                finished = true;
            this.repairingItem.setDamageValue((int) dmg);
            return this.repairingItem;
        }
        return repairingItem;
    }

    public void fromTag(CompoundTag tag) {
        this.repairingItem = ItemStack.of(tag.getCompound("Item"));
        if (this.profession.canWorkInUnloadedChunks()) {
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
        ItemStack repairing = this.getItem(System.currentTimeMillis());
        repairing.save(item);

        main.put("Item", item);
        main.putLong("StartTime", this.startTime);
        main.putInt("StartDamage", repairing.getDamageValue());

        return main;
    }
}
