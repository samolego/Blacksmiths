package org.samo_lego.blacksmiths.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.fabric.platform.FabricPlatform;
import org.samo_lego.blacksmiths.fabric.platform.GrandEconomyImpl;

public class BlacksmithsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricPlatform platform = new FabricPlatform();

        VanillaEconomy economy;
        if (FabricLoader.getInstance().isModLoaded("grandeconomy"))
            economy = new GrandEconomyImpl();
        else
            economy = new VanillaEconomy();

        new Blacksmiths(platform, economy);
        CommandRegistrationCallback.EVENT.register(platform::registerCommands);
    }
}
