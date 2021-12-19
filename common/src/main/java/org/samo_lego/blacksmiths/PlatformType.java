package org.samo_lego.blacksmiths;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.nio.file.Path;

public abstract class PlatformType {
    public abstract Path getConfigPath();

    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {

    }
}
