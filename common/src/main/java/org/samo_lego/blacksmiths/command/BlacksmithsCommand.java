package org.samo_lego.blacksmiths.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;

import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;
import static org.samo_lego.blacksmiths.Blacksmiths.INSTANCE;

public class BlacksmithsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        LiteralCommandNode<CommandSourceStack> blacksmiths = dispatcher.register(
                literal("blacksmiths")
                    .requires(source -> INSTANCE.getPlatform().hasPermission(source, "blacksmiths.command.blacksmiths", CONFIG.permissions.blacksmithsLevel))
        );

        // Node for config editing
        LiteralCommandNode<CommandSourceStack> config = literal("config")
                .requires(source -> INSTANCE.getPlatform().hasPermission(source, "blacksmiths.command.blacksmiths.config", 0))
                .then(literal("reload")
                        .requires(source -> INSTANCE.getPlatform().hasPermission(source, "blacksmiths.command.blacksmiths.config.reload", 0))
                        .executes(BlacksmithsCommand::reloadConfig)
                )
                .build();
        LiteralCommandNode<CommandSourceStack> edit = literal("edit")
                .requires(source -> INSTANCE.getPlatform().hasPermission(source, "blacksmiths.command.blacksmiths.config.edit", 0))
                .build();

        CONFIG.generateCommand(edit);

        config.addChild(edit);
        blacksmiths.addChild(config);
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        CONFIG.reload(INSTANCE.getConfigFile());
        context.getSource().sendSuccess(new TranslatableComponent("gui.done").withStyle(ChatFormatting.GREEN), false);
        return 1;
    }
}
