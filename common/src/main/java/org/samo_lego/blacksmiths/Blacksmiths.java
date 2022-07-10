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

    private static Blacksmiths INSTANCE;
    private final Path configPath;
    public static final Logger LOGGER = LogManager.getLogger("Blacksmiths");
    private VanillaEconomy economy;

    public Blacksmiths(PlatformType platform, VanillaEconomy economy) {
        INSTANCE = this;
        this.economy = economy;
        this.configPath = Path.of(getConfigPath(platform));

        if (CONFIG == null) {
            initConfig(platform);
        }

        TaterzensAPI.registerProfession(BlacksmithProfession.ID, BlacksmithProfession::new);
    }

    public static void initConfig(PlatformType platform) {
        CONFIG = SmithyConfig.loadConfigFile(new File(getConfigPath(platform)));
    }

    private static String getConfigPath(PlatformType platform) {
        return platform.getConfigPath() + "/Taterzens/blacksmiths.json";
    }

    public static Blacksmiths getInstance() {
        return INSTANCE;
    }

    public File getConfigFile() {
        return this.configPath.toFile();
    }

    public VanillaEconomy getEconomy() {
        return economy;
    }

    public void setEconomy(VanillaEconomy economy) {
        this.economy = economy;
    }
}
