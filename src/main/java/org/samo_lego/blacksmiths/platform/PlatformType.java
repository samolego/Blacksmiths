package org.samo_lego.blacksmiths.platform;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.samo_lego.blacksmiths.command.BlacksmithCommand;
import org.samo_lego.blacksmiths.command.BlacksmithsCommand;

import java.nio.file.Path;

public abstract class PlatformType {
    public abstract Path getConfigPath();

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        BlacksmithCommand.register();
        BlacksmithsCommand.register(dispatcher);
    }
}
