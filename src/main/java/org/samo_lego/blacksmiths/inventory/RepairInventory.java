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

    /**
     * Gets the repairing item.
     * @return repairing item.
     */
    public ItemStack peek() {
        return this.repairingItem;
    }


    /**
     * Gets item with up-to-date damage value.
     * @param now current time.
     * @return item with up-to-date damage value.
     */
    public ItemStack getItem(long now) {
        if (!repairingItem.isEmpty() && !this.finished) {
            int dmg = this.getLatestDamage(now);
            this.repairingItem.setDamageValue(dmg);
            return this.repairingItem;
        }
        return this.repairingItem;
    }


    /**
     * Gets the latest damage value.
     * @param now current time.
     * @return latest damage value.
     */
    private int getLatestDamage(long now) {
        if (!repairingItem.isEmpty() && !this.finished) {
            long dmgDecrease = (long) ((now - this.startTime) * this.profession.getDurabilityPerSecond() / 1000);
            long dmg = this.startDamage - dmgDecrease;
            if (dmg <= 0) {
                finished = true;
                dmg = 0;
            }
            return (int) dmg;
        }
        return 0;
    }


    /**
     * Gets price for the repair at the current time.
     * @param now current time.
     * @return price for the repair at the current time.
     */
    public double getPrice(long now) {
        int repaired = this.startDamage - this.getLatestDamage(now);
        return repaired * this.profession.getCostPerDamage();
    }

    public void fromTag(CompoundTag tag) {
        this.repairingItem = ItemStack.of(tag.getCompound("Item"));
        if (this.profession.canWorkInUnloadedChunks()) {
            this.startTime = tag.getLong("StartTime");
        } else {
            this.startTime = System.currentTimeMillis();
        }

        this.startDamage = tag.getInt("StartDamage");
        this.finished = tag.getBoolean("Finished");
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
        main.putBoolean("Finished", this.finished);

        return main;
    }

    public int getInitialDamage() {
        return this.startDamage;
    }
}
