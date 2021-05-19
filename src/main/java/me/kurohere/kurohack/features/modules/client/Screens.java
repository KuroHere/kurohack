package me.kurohere.kurohack.features.modules.client;

import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.setting.Setting;

public class Screens
extends Module {
    public Setting<Boolean> mainScreen = this.register(new Setting<Boolean>("MainScreen", true));
    public static Screens INSTANCE;

    public Screens() {
        super("Screens", "Controls custom screens used by the client", Module.Category.CLIENT, true, false, false);
        INSTANCE = this;
    }

    @Override
    public void onTick() {
    }
}

