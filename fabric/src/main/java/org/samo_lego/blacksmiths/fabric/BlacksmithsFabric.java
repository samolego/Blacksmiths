package org.samo_lego.blacksmiths.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.fabric.platform.FabricPlatform;

public class BlacksmithsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricPlatform platform = new FabricPlatform();
        Blacksmiths.init(platform);

        CommandRegistrationCallback.EVENT.register(platform::registerCommands);
    }
}
