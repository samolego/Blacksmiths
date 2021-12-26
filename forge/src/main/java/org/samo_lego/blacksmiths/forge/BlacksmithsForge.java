package org.samo_lego.blacksmiths.forge;

import net.minecraft.commands.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.blacksmiths.PlatformType;
import org.samo_lego.blacksmiths.economy.VanillaEconomy;
import org.samo_lego.blacksmiths.forge.platform.ForgePlatform;

@Mod(Blacksmiths.MOD_ID)
public class BlacksmithsForge {

    public static final PlatformType forgePlatform = new ForgePlatform();

    public BlacksmithsForge() {
        MinecraftForge.EVENT_BUS.register(this);
        VanillaEconomy economy = new VanillaEconomy();
        new Blacksmiths(forgePlatform, economy);
    }

    @SubscribeEvent()
    public void registerCommands(RegisterCommandsEvent event) {
        forgePlatform.registerCommands(event.getDispatcher(), event.getEnvironment().equals(Commands.CommandSelection.DEDICATED));
    }
}
