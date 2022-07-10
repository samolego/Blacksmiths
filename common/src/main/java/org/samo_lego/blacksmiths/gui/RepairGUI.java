package org.samo_lego.blacksmiths.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.inventory.RepairInventory;
import org.samo_lego.blacksmiths.inventory.RepairingSlot;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.gui.ListItemsGUI;

import java.util.List;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class RepairGUI extends ListItemsGUI {

    private final List<RepairInventory> items;
    private final BlacksmithProfession profession;
    private int tickCounter = 0;

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

        // If live update is disabled, we need to refresh the gui upon opening.
        if (!CONFIG.forceAccurate) {
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

    /**
     * Gets item at the given index. If live update is disabled, durability will be calculated from ticks.
     * @param index index of the item to get.
     * @return the item at the given index.
     */
    @Override
    public ItemStack getItem(int index) {
        ItemStack itemStack;
        index = getSlot2MessageIndex(index);

        if(index < this.items.size()) {
            RepairInventory inv = this.items.get(index);

            int startDmg = inv.getInitialDamage();
            if (CONFIG.forceAccurate) {
                itemStack = inv.getItem(System.currentTimeMillis());
            } else {
                itemStack = inv.peek();
                // We can modify the itemstack in the inventory, as it will
                // be changed anyway when taken out of the inventory.
                int dmg = (int) (this.tickCounter * this.profession.getDurabilityPerSecond() / 20);
                ++this.tickCounter;

                if (dmg > 0) {
                    itemStack.setDamageValue(itemStack.getDamageValue() - dmg);
                    this.tickCounter = 0;
                }
            }

            // Adds lore with current cost to the item.
            if (CONFIG.costs.addLore) {
                ItemStack copy = itemStack.copy();
                CompoundTag nbt = copy.getTag();

                if (nbt != null) {
                    // Calculate cost
                    double amount = (startDmg - itemStack.getDamageValue()) * this.profession.getCostPerDamage();
                    VanillaEconomy economy = Blacksmiths.getInstance().getEconomy();

                    double cost = economy.getItemConversionCost(amount);
                    MutableComponent costLore = economy.getCurrencyFormat(cost);
                    boolean canAfford = Blacksmiths.getInstance().getEconomy().canAfford(cost, this.player) >= 0;

                    CompoundTag nbtDisplay = nbt.getCompound(ItemStack.TAG_DISPLAY);
                    ListTag nbtLore = new ListTag();

                    nbtLore.add(StringTag.valueOf(
                            Component.Serializer.toJson(
                                    costLore.withStyle(canAfford ? ChatFormatting.GREEN : ChatFormatting.RED)
                            )
                    ));

                    nbtDisplay.put(ItemStack.TAG_LORE, nbtLore);
                    nbt.put(ItemStack.TAG_DISPLAY, nbtDisplay);
                    copy.setTag(nbt);
                }

                itemStack = copy;
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

    /**
     * Removes the item at the given index, with up-to-date durability.
     * @param index index of the item to remove.
     * @return the item that was removed.
     */
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        index = getSlot2MessageIndex(index);

        if(index < this.items.size()) {
            long now = System.currentTimeMillis();

            double enough = this.canAfford(index, now);
            if (enough >= 0) {
                double price = this.items.get(index).getPrice(now);

                itemStack = this.items.remove(index).getItem(now);
                Blacksmiths.getInstance().getEconomy().withdraw(price, this.player);
            } else {
                this.close();
                this.player.sendSystemMessage(this.notEnoughMoneyMessage(enough * -1));
            }
        }

        return itemStack;
    }

    public MutableComponent notEnoughMoneyMessage(double needed) {
        final String message = CONFIG.messages.insufficentCredit;
        if (!CONFIG.costs.ignoreEconomyMod) {
            // Round needed to 2 decimals
            needed = Math.round(needed * 100) / 100.0;
        }
        final MutableComponent formatted = Blacksmiths.getInstance().getEconomy().getCurrencyFormat(needed);

        return Component.translatable(message, formatted.withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.RED);
    }

    /**
     * Whether player can afford the item at the given index.
     * @param index index of the item to check.
     * @param now current time.
     * @return negative number if player needs more money, positive number if player has enough money, 0 if player has exact amount of money.
     */
    public double canAfford(int index, long now) {
        double price = this.items.get(index).getPrice(now);
        return Blacksmiths.getInstance().getEconomy().canAfford(price, this.player);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (stack.isEmpty()) {
            this.removeItemNoUpdate(index);
        } else {
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
