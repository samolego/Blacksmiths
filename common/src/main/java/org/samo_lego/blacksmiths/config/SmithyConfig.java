package org.samo_lego.blacksmiths.config;

import com.google.gson.annotations.SerializedName;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;
import org.samo_lego.config2brigadier.annotation.BrigadierDescription;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.samo_lego.blacksmiths.Blacksmiths.*;
import static org.samo_lego.taterzens.Taterzens.GSON;

public class SmithyConfig implements IBrigadierConfigurator {

    @SerializedName("// How much should the durability be increased each second.")
    public final String _comment_durabilityPerSecond = "";
    @BrigadierDescription(defaultOption = "0.5")
    @SerializedName("durability_per_second")
    public double durabilityPerSecond = 0.5D;

    @SerializedName("// Whether items should update their durability 100% accurately or not.")
    public final String _comment_liveUpdate0 = "";
    @SerializedName("// Causes a bit more lag if true. If false, durability display can vary for some points (but is still accurate when item is taken).")
    public final String _comment_liveUpdate1 = "";
    @BrigadierDescription(defaultOption = "true")
    @SerializedName("force_accurate_update")
    public boolean forceAccurate = false;

    @SerializedName("// Whether repairing should happen based on time, which makes it 'work' in unloaded chunks as well.")
    public final String _comment_workInUnloadedChunks0 = "";
    @SerializedName("// Doesn't cause any more lag when turned on.")
    public final String _comment_workInUnloadedChunks1 = "";
    @SerializedName("work_when_unloaded")
    public boolean workInUnloadedChunks = true;

    public Permissions permissions = new Permissions();
    public static class Permissions {

        @SerializedName("// Permission level required for blacksmiths command.")
        public final String _comment_blacksmithLevel = "";
        @BrigadierDescription(defaultOption = "4")
        @SerializedName("blacksmiths_cmd_level")
        public int blacksmithsLevel = 4;

        @BrigadierDescription(defaultOption = "2")
        @SerializedName("profession_blacksmith_cmd_level")
        public int blacksmithLevel = 2;
    }

    /**
     * Loads config file.
     *
     * @param file file to load the language file from.
     * @return TaterzenLanguage object
     */
    public static SmithyConfig loadConfigFile(File file) {
        SmithyConfig config = null;
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = GSON.fromJson(fileReader, SmithyConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(MOD_ID + " Problem occurred when trying to load config: ", e);
            }
        }
        if(config == null)
            config = new SmithyConfig();

        config.saveConfigFile(file);

        return config;
    }

    /**
     * Saves the config to the given file.
     *
     * @param file file to save config to
     */
    public void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Problem occurred when saving config: " + e.getMessage());
        }
    }

    /**
     * Changes values of current object with reflection,
     * in order to keep the same object.
     * (that still allows in-game editing)
     *
     * @param file file to read new config from
     */
    public void reload(File file) {
        SmithyConfig newConfig = loadConfigFile(file);
        this.reload(newConfig);
    }

    @Override
    public void save() {
        this.saveConfigFile(INSTANCE.getConfigFile());
    }
}
