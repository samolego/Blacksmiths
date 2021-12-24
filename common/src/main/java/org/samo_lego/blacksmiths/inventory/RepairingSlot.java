package org.samo_lego.blacksmiths.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

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

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        System.out.println("onTake");
        //this.set(ItemStack.EMPTY);
        this.setChanged();
    }

    @Override
    public ItemStack safeTake(int i, int j, Player player) {
        Optional<ItemStack> optional = this.tryRemove(i, j, player);
        optional.ifPresent((itemStack) -> this.onTake(player, itemStack));
        return optional.orElse(ItemStack.EMPTY);
    }

    @Override
    protected void onQuickCraft(ItemStack itemStack, int i) {
        System.out.println("onQuickCraft " + i);
        //this.set(ItemStack.EMPTY);
        this.setChanged();
    }

    @Override
    protected void onSwapCraft(int i) {
        System.out.println("onSwapCraft " + i); // todo fix item shadowing
        this.container.removeItemNoUpdate(i);
        //this.setChanged();  // doesn't work

        this.set(ItemStack.EMPTY);
        this.setChanged();
    }

    /**
     * Also detects shift clicking the item.
     * @param player player taking the item.
     * @return true if item is allowed to be taken.
     */
    @Override
    public boolean mayPickup(Player player) {
        System.out.println("mayPickup" + this.container.getItem(this.slot));
        return true;
    }
}
