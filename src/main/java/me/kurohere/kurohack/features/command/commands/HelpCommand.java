package me.kurohere.kurohack.features.command.commands;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.command.Command;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("commands");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("You can use following commands: ");
        for (Command command : kuro.commandManager.getCommands()) {
            HelpCommand.sendMessage(kuro.commandManager.getPrefix() + command.getName());
        }
    }
}

