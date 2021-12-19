package org.samo_lego.blacksmiths.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.api.professions.TaterzenProfession;
import org.samo_lego.taterzens.commands.NpcCommand;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;
import static org.samo_lego.blacksmiths.Blacksmiths.INSTANCE;
import static org.samo_lego.taterzens.commands.ProfessionCommand.PROFESSION_COMMAND_NODE;

public class BlacksmithCommand {
    public static void register() {

        LiteralCommandNode<CommandSourceStack> blacksmithNode = literal("blacksmith")
                .requires(src -> INSTANCE.getPlatform().hasPermission(src, "blacksmiths.command.blacksmith", CONFIG.permissions.blacksmithLevel))
                .then(literal("workInUnloadedChunks")
                        .requires(src -> INSTANCE.getPlatform().hasPermission(src, "blacksmiths.command.blacksmith.work_in_unloaded_chunks", 0))
                        .then(argument("value", BoolArgumentType.bool())
                            .executes(BlacksmithCommand::workInUnloadedChunks)
                        )
                )
                .then(literal("durabilityPerSecond")
                        .requires(src -> INSTANCE.getPlatform().hasPermission(src, "blacksmiths.command.blacksmith.durability_per_second", 0))
                        .then(argument("value", DoubleArgumentType.doubleArg(0.0D))
                                .executes(BlacksmithCommand::durabilityPerSecond)
                        )
                )
                .build();


        PROFESSION_COMMAND_NODE.addChild(blacksmithNode);
    }

    private static int durabilityPerSecond(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        double value = DoubleArgumentType.getDouble(context, "value");
        Entity entity = context.getSource().getEntityOrException();
        return NpcCommand.selectedTaterzenExecutor(entity, (taterzen) -> {
            TaterzenProfession profession = taterzen.getProfession(BlacksmithProfession.ID);
            if (profession instanceof BlacksmithProfession blacksmith) {
                blacksmith.setDurabilityPerSecond(value);
                entity.sendMessage(new TranslatableComponent("gui.done"), taterzen.getUUID());
            }
        });

    }

    private static int workInUnloadedChunks(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean value = BoolArgumentType.getBool(context, "value");
        Entity entity = context.getSource().getEntityOrException();
        return NpcCommand.selectedTaterzenExecutor(entity, (taterzen) -> {
            TaterzenProfession profession = taterzen.getProfession(BlacksmithProfession.ID);
            if (profession instanceof BlacksmithProfession blacksmith) {
                blacksmith.setWorkInUnloadedChunks(value);
                entity.sendMessage(new TranslatableComponent("gui.done"), taterzen.getUUID());
            }
        });
    }
}
