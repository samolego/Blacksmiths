package org.samo_lego.blacksmiths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.samo_lego.blacksmiths.config.SmithyConfig;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.api.TaterzensAPI;

import java.io.File;
import java.nio.file.Path;

public class Blacksmiths {

    public static final String MOD_ID = "blacksmiths";
    public static SmithyConfig CONFIG = null;
    private final PlatformType platform;

    private static Blacksmiths INSTANCE;
    private final Path configPath;
    public static final Logger LOGGER = LogManager.getLogger("Blacksmiths");
    private final VanillaEconomy economy;

    public Blacksmiths(PlatformType platform, VanillaEconomy economy) {
        INSTANCE = this;
        this.platform = platform;
        this.economy = economy;
        this.configPath = Path.of(platform.getConfigPath() + "/Taterzens/blacksmiths.json");
        CONFIG = SmithyConfig.loadConfigFile(INSTANCE.getConfigFile());

        TaterzensAPI.registerProfession(BlacksmithProfession.ID, BlacksmithProfession::new);
    }

    public static Blacksmiths getInstance() {
        return INSTANCE;
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public File getConfigFile() {
        return this.configPath.toFile();
    }

    public VanillaEconomy getEconomy() {
        return economy;
    }
}
