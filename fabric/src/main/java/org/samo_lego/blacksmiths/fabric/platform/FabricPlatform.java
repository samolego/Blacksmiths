package org.samo_lego.blacksmiths.fabric.platform;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import org.samo_lego.blacksmiths.PlatformType;

import java.nio.file.Path;

import static org.samo_lego.taterzens.compatibility.fabric.LoaderSpecificImpl.permissions$checkPermission;

public class FabricPlatform extends PlatformType {
    @Override
    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean hasPermission(CommandSourceStack src, String permission, int fallback) {
        return permissions$checkPermission(src, permission, fallback);
    }
}
