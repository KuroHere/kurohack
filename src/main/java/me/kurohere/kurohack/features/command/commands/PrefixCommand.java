package me.kurohere.kurohack.features.command.commands;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.command.Command;
import me.kurohere.kurohack.features.modules.client.ClickGui;

public class PrefixCommand
extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("\u00a7cSpecify a new prefix.");
            return;
        }
        kuro.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to \u00a7a" + kuro.commandManager.getPrefix());
    }
}

