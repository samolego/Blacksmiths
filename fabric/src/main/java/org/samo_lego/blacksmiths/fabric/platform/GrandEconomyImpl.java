package org.samo_lego.blacksmiths.fabric.platform;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;

public class GrandEconomyImpl extends VanillaEconomy {
    private final CurrencyAPI currencyApi;

    public GrandEconomyImpl() {
        this.currencyApi = DIContainer.get().getInstance(CurrencyAPI.class);
    }

    @Override
    public double canAfford(double amount, ServerPlayer player) {
        return this.currencyApi.getBalance(player.getUUID(), true) - amount;
    }

    @Override
    public void withdraw(double amount, ServerPlayer player) {
        this.currencyApi.addToBalance(player.getUUID(), -amount, true);
    }

    @Override
    public double getItemConversionCost(double amount) {
        return amount;
    }

    @Override
    public MutableComponent getCurrencyFormat(double amount) {
        return new TextComponent(this.currencyApi.formatCurrency(amount));
    }
}
