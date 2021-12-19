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
    @BrigadierDescription(defaultOption = "1.0")
    @SerializedName("durability_per_second")
    public double durabilityPerSecond = 1.0D;

    @SerializedName("// Whether items should update their durability in real-time or not.")
    public final String _comment_liveUpdate = "";
    @SerializedName("live_update")
    public boolean liveUpdate = true;

    @SerializedName("// Whether repairing should happen based on time, which makes it 'work' in unloaded chunks as well.")
    public final String _comment_workInUnloadedChunks0 = "";
    @SerializedName("// Doesn't cause any more lag when turned on.")
    public final String _comment_workInUnloadedChunks1 = "";
    @SerializedName("work_when_unloaded")
    public boolean workInUnloadedChunks = true;

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
