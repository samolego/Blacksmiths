package org.samo_lego.blacksmiths.fabric.platform;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class GrandEconomyImpl extends VanillaEconomy {
    private final CurrencyAPI currencyApi;

    public GrandEconomyImpl() {
        this.currencyApi = DIContainer.get().getInstance(CurrencyAPI.class);
    }

    @Override
    public double canAfford(double amount, ServerPlayer player) {
        if (CONFIG.costs.ignoreEconomyMod) {
            return super.canAfford(amount, player);
        }
        return this.currencyApi.getBalance(player.getUUID(), true) - amount;
    }

    @Override
    public void withdraw(double amount, ServerPlayer player) {
        if (CONFIG.costs.ignoreEconomyMod) {
            super.withdraw(amount, player);
        }
        this.currencyApi.addToBalance(player.getUUID(), -amount, true);
    }
}
