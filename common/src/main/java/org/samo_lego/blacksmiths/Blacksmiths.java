package org.samo_lego.blacksmiths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.samo_lego.blacksmiths.config.SmithyConfig;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.api.TaterzensAPI;

import java.io.File;
import java.nio.file.Path;

public class Blacksmiths {

    public static final String MOD_ID = "blacksmiths";
    public static SmithyConfig CONFIG = null;
    private PlatformType platform;

    public static final Blacksmiths INSTANCE = new Blacksmiths();
    private Path configPath;
    public static final Logger LOGGER = LogManager.getLogger("Blacksmiths");

    public static void init(PlatformType platform) {
        INSTANCE.platform = platform;
        INSTANCE.configPath = Path.of(platform.getConfigPath() + "/Taterzens/blacksmiths.json");
        CONFIG = SmithyConfig.loadConfigFile(INSTANCE.getConfigFile());

        TaterzensAPI.registerProfession(BlacksmithProfession.ID, new BlacksmithProfession());
    }

    public PlatformType getPlatform() {
        return platform;
    }

    public File getConfigFile() {
        return this.configPath.toFile();
    }
}
