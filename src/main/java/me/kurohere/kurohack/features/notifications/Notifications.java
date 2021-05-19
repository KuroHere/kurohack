package me.kurohere.kurohack.features.notifications;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.modules.client.HUD;
import me.kurohere.kurohack.util.RenderUtil;
import me.kurohere.kurohack.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notifications {
    private final String text;
    private final long disableTime;
    private final float width;
    private final Timer timer = new Timer();

    public Notifications(String text, long disableTime) {
        this.text = text;
        this.disableTime = disableTime;
        this.width = kuro.moduleManager.getModuleByClass(HUD.class).renderer.getStringWidth(text);
        this.timer.reset();
    }

    public void onDraw(int y) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        if (this.timer.passedMs(this.disableTime)) {
            kuro.notificationManager.getNotifications().remove(this);
        }
        RenderUtil.drawRect((float)(scaledResolution.getScaledWidth() - 4) - this.width, y, scaledResolution.getScaledWidth() - 2, y + kuro.moduleManager.getModuleByClass(HUD.class).renderer.getFontHeight() + 3, 0x75000000);
        kuro.moduleManager.getModuleByClass(HUD.class).renderer.drawString(this.text, (float)scaledResolution.getScaledWidth() - this.width - 3.0f, y + 2, -1, true);
    }
}

