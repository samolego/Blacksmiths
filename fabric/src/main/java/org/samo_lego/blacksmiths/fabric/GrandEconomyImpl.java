package org.samo_lego.blacksmiths.fabric;

import dev.the_fireplace.grandeconomy.api.entrypoints.GrandEconomyEntrypoint;
import dev.the_fireplace.grandeconomy.api.injectables.EconomyRegistry;
import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.blacksmiths.economy.EconomyUtil;

public class GrandEconomyImpl extends EconomyUtil implements GrandEconomyEntrypoint {
    private EconomyRegistry economyRegistry;

    @Override
    public void init(EconomyRegistry economyRegistry) {
        this.economyRegistry = economyRegistry;
    }

    @Override
    public boolean canAfford(double amount, ServerPlayer player) {
        return false;
    }
}
