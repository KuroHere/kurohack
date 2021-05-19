package me.kurohere.kurohack.features.command.commands;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.command.Command;

public class ReloadCommand
extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        kuro.reload();
    }
}

