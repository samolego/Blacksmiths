package org.samo_lego.blacksmiths.fabric.platform;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
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
        if (CONFIG.costs.ignoreEconomyMod)
            super.withdraw(amount, player);
        else
            this.currencyApi.addToBalance(player.getUUID(), -amount, true);
    }

    @Override
    public double getItemConversionCost(double amount) {
        return amount;
    }

    @Override
    public MutableComponent getCurrencyFormat(double amount) {
        if (CONFIG.costs.ignoreEconomyMod) {
            return super.getCurrencyFormat(amount);
        }
        return new TextComponent(this.currencyApi.formatCurrency(amount));
    }
}
