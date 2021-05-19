package me.kurohere.kurohack.features.modules.client;

import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.modules.client.ServerModule;
import me.kurohere.kurohack.features.setting.Setting;

public class Media
extends Module {
    public final Setting<Boolean> changeOwn = this.register(new Setting<Boolean>("MyName", true));
    public final Setting<String> ownName = this.register(new Setting<Object>("Name", "Name here...", v -> this.changeOwn.getValue()));
    private static Media instance;

    public Media() {
        super("Media", "Helps with creating Media", Module.Category.CLIENT, false, false, false);
        instance = this;
    }

    public static Media getInstance() {
        if (instance == null) {
            instance = new Media();
        }
        return instance;
    }

    public static String getPlayerName() {
        if (Media.fullNullCheck() || !ServerModule.getInstance().isConnected()) {
            return mc.getSession().getUsername();
        }
        String name = ServerModule.getInstance().getPlayerName();
        if (name == null || name.isEmpty()) {
            return mc.getSession().getUsername();
        }
        return name;
    }
}

