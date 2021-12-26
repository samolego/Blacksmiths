package org.samo_lego.blacksmiths.economy;

import net.minecraft.server.level.ServerPlayer;

public abstract class EconomyUtil {
    public abstract boolean canAfford(double amount, ServerPlayer player);

    public abstract void withdraw(double amount, ServerPlayer player);
}
