package org.samo_lego.blacksmiths.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import org.samo_lego.blacksmiths.PlatformType;

import java.nio.file.Path;

public class FabricPlatform extends PlatformType {
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
