package org.samo_lego.blacksmiths.config;

import com.google.gson.annotations.SerializedName;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.config2brigadier.IBrigadierConfigurator;
import org.samo_lego.config2brigadier.annotation.BrigadierDescription;
import org.samo_lego.config2brigadier.annotation.BrigadierExcluded;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.LogManager.getLogger;
import static org.samo_lego.blacksmiths.Blacksmiths.LOGGER;
import static org.samo_lego.blacksmiths.Blacksmiths.MOD_ID;
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
    public Costs costs = new Costs();
    public Messages messages = new Messages();

    public static class Messages {

        public String insufficentCredit = "You still need %s more to repair this item.";
        public String cost = "Fully repairing the item will cost you %s.";
    }

    public static class Costs {
        @SerializedName("// Which item should count as payment. Only used if ")
        public final String _comment_paymentItem = "";
        @BrigadierDescription(defaultOption = "minecraft:iron_nugget")
        @SerializedName("payment_item")
        public String paymentItem = "minecraft:iron_nugget";

        @SerializedName("// How much is the above item worth.")
        public String _comment_itemWorth = "";
        @SerializedName("item_worth")
        public double itemWorth = 2.5D;

        @SerializedName("// How much does each durability point cost.")
        public final String _comment_costPerDurabilityPoint = "";
        @SerializedName("cost_per_durability_point")
        public double costPerDurabilityPoint = 0.2D;

        @SerializedName("// Whether to force item-based transactions.")
        public final String _comment_ignoreEconomyMod = "";
        @BrigadierExcluded
        @SerializedName("ignore_economy_mod")
        public boolean ignoreEconomyMod = false;

        @SerializedName("// Whether to add lore with cost info to the item.")
        public final String _comment_addLore = "";
        @SerializedName("add_lore")
        public boolean addLore = true;
    }


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

        // We can support GrandEconomy -> VanillaEconomy during runtime, but not the other way around.
        if (newConfig.costs.ignoreEconomyMod && !this.costs.ignoreEconomyMod) {
            Blacksmiths.getInstance().setEconomy(new VanillaEconomy());
        } else if (!newConfig.costs.ignoreEconomyMod && this.costs.ignoreEconomyMod) {
            getLogger(MOD_ID).warn("Enabling GrandEconomy support during runtime is not supported. Disabling it.");
        }
        this.reload(newConfig);
        this.save();
    }

    @Override
    public void save() {
        this.saveConfigFile(Blacksmiths.getInstance().getConfigFile());
    }
}
