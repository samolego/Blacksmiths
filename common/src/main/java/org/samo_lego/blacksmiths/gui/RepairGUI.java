package org.samo_lego.blacksmiths.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.samo_lego.blacksmiths.inventory.RepairInventory;
import org.samo_lego.blacksmiths.inventory.RepairingSlot;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.gui.ListItemsGUI;

import java.util.List;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class RepairGUI extends ListItemsGUI {

    private final List<RepairInventory> items;
    private final BlacksmithProfession profession;

    /**
     * Constructs a new simple container gui for the supplied player.
     *
     * @param player              the player to server this gui to.
     * @param npcName             player's taterzen.
     */
    public RepairGUI(ServerPlayer player, BlacksmithProfession profession, Component npcName, List<RepairInventory> items) {
        super(player, npcName, "container.repair");

        this.items = items;
        this.profession = profession;

        if (CONFIG.liveUpdate) {
            long now = System.currentTimeMillis();
            this.items.forEach(inv -> inv.getItem(now));
        }


        int i = 9;
        do {
            // - 9 as first row is occupied but we want to have index 0 at first element
            this.setSlotRedirect(i, new RepairingSlot(this, i - 9));
        } while (++i < this.getSize());

    }

    @Override
    public int getMaxPages() {
        if (this.items == null)
            return 0;
        return this.items.size() / this.getSize();

    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        ItemStack itemStack;
        index = getSlot2MessageIndex(index);

        if(index < this.items.size()) {
            RepairInventory inv = this.items.get(index);
            if (CONFIG.liveUpdate) {
                itemStack = inv.getItem(System.currentTimeMillis());
            } else {
                itemStack = inv.peek();
            }
        } else {
            itemStack = ItemStack.EMPTY;
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItem(int index, int j) {
        return this.removeItemNoUpdate(index);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemStack;
        index = getSlot2MessageIndex(index);

        if(index < this.items.size()) {
            itemStack = this.items.remove(index).getItem(System.currentTimeMillis());
        } else {
            itemStack = ItemStack.EMPTY;
        }

        return itemStack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!stack.isEmpty() && stack.isDamaged()) {
            index = getSlot2MessageIndex(index);
            if (index > this.items.size())
                index = this.items.size();

            RepairInventory inv = new RepairInventory(this.profession);
            boolean canRepair = inv.startRepairing(stack);
            if (canRepair)
                this.items.add(index, inv);
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
