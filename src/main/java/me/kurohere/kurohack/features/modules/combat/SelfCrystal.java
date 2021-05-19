package me.kurohere.kurohack.features.modules.combat;

import me.kurohere.kurohack.features.modules.Module;

public class SelfCrystal
extends Module {
    public SelfCrystal() {
        super("SelfCrystal", "Best module", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onTick() {
        if (AutoCrystal.getInstance().isEnabled()) {
            AutoCrystal.target = SelfCrystal.mc.player;
        }
    }
}

