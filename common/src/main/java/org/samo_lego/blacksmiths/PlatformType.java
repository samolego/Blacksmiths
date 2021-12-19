package org.samo_lego.blacksmiths;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import org.samo_lego.blacksmiths.command.BlacksmithCommand;
import org.samo_lego.blacksmiths.command.BlacksmithsCommand;

import java.nio.file.Path;

public abstract class PlatformType {
    public abstract Path getConfigPath();

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        BlacksmithCommand.register();
        BlacksmithsCommand.register(dispatcher, dedicated);
    }

    public abstract boolean hasPermission(CommandSourceStack src, String permission, int fallback);
}
