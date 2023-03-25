package org.samo_lego.blacksmiths;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.platform.FabricPlatform;

public class BlacksmithsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        var platform = new FabricPlatform();

        Blacksmiths.initConfig(platform);

        VanillaEconomy economy;
        /*if (FabricLoader.getInstance().isModLoaded("grandeconomy") && !CONFIG.costs.ignoreEconomyMod) {
            economy = new GrandEconomyImpl();
        } else {*/
        economy = new VanillaEconomy();
        //}

        new Blacksmiths(platform, economy);
        CommandRegistrationCallback.EVENT.register((dispatcher, assets, selection) -> platform.registerCommands(dispatcher));
    }
}
