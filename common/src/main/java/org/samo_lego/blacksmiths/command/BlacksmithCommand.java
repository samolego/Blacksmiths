package org.samo_lego.blacksmiths.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.samo_lego.blacksmiths.profession.BlacksmithProfession;
import org.samo_lego.taterzens.Taterzens;
import org.samo_lego.taterzens.api.professions.TaterzenProfession;
import org.samo_lego.taterzens.commands.NpcCommand;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.samo_lego.blacksmiths.Blacksmiths.CONFIG;
import static org.samo_lego.taterzens.commands.ProfessionCommand.PROFESSION_COMMAND_NODE;

public class BlacksmithCommand {

    private static final MutableComponent SUCCESS = Component.translatable("gui.done").append(".").withStyle(ChatFormatting.GREEN);

    public static void register() {

        LiteralCommandNode<CommandSourceStack> blacksmithNode = literal("blacksmith")
                .requires(src -> Taterzens.getInstance().getPlatform().checkPermission(src, "blacksmiths.command.blacksmith", CONFIG.permissions.blacksmithLevel))
                .then(literal("workInUnloadedChunks")
                        .requires(src -> Taterzens.getInstance().getPlatform().checkPermission(src, "blacksmiths.command.blacksmith.work_in_unloaded_chunks", 0))
                        .then(argument("value", BoolArgumentType.bool())
                            .executes(BlacksmithCommand::workInUnloadedChunks)
                        )
                )
                .then(literal("durabilityPerSecond")
                        .requires(src -> Taterzens.getInstance().getPlatform().checkPermission(src, "blacksmiths.command.blacksmith.durability_per_second", 0))
                        .then(argument("value", DoubleArgumentType.doubleArg(0.0D))
                                .executes(BlacksmithCommand::durabilityPerSecond)
                        )
                )
                .then(literal("costPerDurabilityPoint")
                        .requires(src -> Taterzens.getInstance().getPlatform().checkPermission(src, "blacksmiths.command.blacksmith.cost_per_durability_point", 0))
                        .then(argument("value", DoubleArgumentType.doubleArg(0.0D))
                                .executes(BlacksmithCommand::durabilityCostPerPoint)
                        )
                )
                .build();


        PROFESSION_COMMAND_NODE.addChild(blacksmithNode);
    }

    private static int durabilityCostPerPoint(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        double value = DoubleArgumentType.getDouble(context, "value");
        Entity entity = context.getSource().getEntityOrException();
        return NpcCommand.selectedTaterzenExecutor(entity, (taterzen) -> {
            TaterzenProfession profession = taterzen.getProfession(BlacksmithProfession.ID);
            if (profession instanceof BlacksmithProfession blacksmith) {
                blacksmith.setCostPerDamage(value);
                entity.sendSystemMessage(SUCCESS);
            }
        });
    }

    private static int durabilityPerSecond(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        double value = DoubleArgumentType.getDouble(context, "value");
        Entity entity = context.getSource().getEntityOrException();
        return NpcCommand.selectedTaterzenExecutor(entity, (taterzen) -> {
            TaterzenProfession profession = taterzen.getProfession(BlacksmithProfession.ID);
            if (profession instanceof BlacksmithProfession blacksmith) {
                blacksmith.setDurabilityPerSecond(value);
                entity.sendSystemMessage(SUCCESS);
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
                entity.sendSystemMessage(SUCCESS);
            }
        });
    }
}
