package org.samo_lego.blacksmiths.forge;

import net.minecraftforge.fml.loading.FMLPaths;
import org.samo_lego.blacksmiths.PlatformType;

import java.nio.file.Path;

public class ForgePlatform extends PlatformType {
    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }
}
