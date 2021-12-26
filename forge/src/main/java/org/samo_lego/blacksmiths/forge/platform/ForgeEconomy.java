package org.samo_lego.blacksmiths.forge.platform;

import net.minecraft.server.level.ServerPlayer;
import org.samo_lego.blacksmiths.economy.EconomyUtil;

public class ForgeEconomy extends EconomyUtil {
    @Override
    public boolean canAfford(double amount, ServerPlayer player) {
        return false;
    }

    @Override
    public void withdraw(double amount, ServerPlayer player) {

    }
}
