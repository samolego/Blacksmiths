package org.samo_lego.blacksmiths.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.samo_lego.blacksmiths.Blacksmiths;
import org.samo_lego.taterzens.Taterzens;

import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;

public class BlacksmithsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
        LiteralCommandNode<CommandSourceStack> blacksmiths = dispatcher.register(
                literal("blacksmiths")
                    .requires(source -> Taterzens.getInstance().getPlatform().checkPermission(source, "blacksmiths.command.blacksmiths", CONFIG.permissions.blacksmithsLevel))
        );

        // Node for config editing
        LiteralCommandNode<CommandSourceStack> config = literal("config")
                .requires(source -> Taterzens.getInstance().getPlatform().checkPermission(source, "blacksmiths.command.blacksmiths.config", 0))
                .then(literal("reload")
                        .requires(source -> Taterzens.getInstance().getPlatform().checkPermission(source, "blacksmiths.command.blacksmiths.config.reload", 0))
                        .executes(BlacksmithsCommand::reloadConfig)
                )
                .build();
        LiteralCommandNode<CommandSourceStack> edit = literal("edit")
                .requires(source -> Taterzens.getInstance().getPlatform().checkPermission(source, "blacksmiths.command.blacksmiths.config.edit", 0))
                .build();

        CONFIG.generateCommand(edit);

        config.addChild(edit);
        blacksmiths.addChild(config);
    }

    private static int reloadConfig(CommandContext<CommandSourceStack> context) {
        CONFIG.reload(Blacksmiths.getInstance().getConfigFile());
        context.getSource().sendSuccess(Component.translatable("gui.done").withStyle(ChatFormatting.GREEN), false);
        return 1;
    }
}
