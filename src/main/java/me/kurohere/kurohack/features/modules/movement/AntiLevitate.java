package me.kurohere.kurohack.features.modules.movement;

import java.util.Objects;
import me.kurohere.kurohack.features.modules.Module;
import net.minecraft.potion.Potion;

public class AntiLevitate
extends Module {
    public AntiLevitate() {
        super("AntiLevitate", "Removes shulker levitation", Module.Category.MOVEMENT, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (AntiLevitate.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionFromResourceLocation((String)"levitation")))) {
            AntiLevitate.mc.player.removeActivePotionEffect(Potion.getPotionFromResourceLocation((String)"levitation"));
        }
    }
}

