package org.samo_lego.blacksmiths.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.fabric.platform.FabricPlatform;
import org.samo_lego.blacksmiths.fabric.platform.GrandEconomyImpl;

import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class BlacksmithsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricPlatform platform = new FabricPlatform();

        Blacksmiths.initConfig(platform);

        VanillaEconomy economy;
        if (FabricLoader.getInstance().isModLoaded("grandeconomy") && !CONFIG.costs.ignoreEconomyMod) {
            economy = new GrandEconomyImpl();
        } else {
            economy = new VanillaEconomy();
        }

        new Blacksmiths(platform, economy);
        CommandRegistrationCallback.EVENT.register((dispatcher, assets, selection) -> platform.registerCommands(dispatcher));
    }
}
