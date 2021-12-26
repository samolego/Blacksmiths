package org.samo_lego.blacksmiths.forge.platform;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.fml.loading.FMLPaths;
import org.samo_lego.blacksmiths.PlatformType;

import java.nio.file.Path;

public class ForgePlatform extends PlatformType {
    @Override
    public Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean hasPermission(CommandSourceStack src, String permission, int fallback) {
        return src.hasPermission(fallback);
    }
}
