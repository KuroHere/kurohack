package me.kurohere.kurohack.features.command.commands;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.command.Command;

public class UnloadCommand
extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        kuro.unload(true);
    }
}

