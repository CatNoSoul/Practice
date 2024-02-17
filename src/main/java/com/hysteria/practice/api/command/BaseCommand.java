package com.hysteria.practice.api.command;

public abstract class BaseCommand {

    protected BaseCommand() {
        CommandManager.getInstance().registerCommands(this, null);
    }

    public abstract void onCommand(CommandArgs command);
}
