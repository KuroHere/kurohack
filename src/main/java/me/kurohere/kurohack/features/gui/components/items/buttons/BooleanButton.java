package me.kurohere.kurohack.features.gui.components.items.buttons;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.features.gui.kurohackGui;
import me.kurohere.kurohack.features.gui.components.items.buttons.Button;
import me.kurohere.kurohack.features.modules.client.ClickGui;
import me.kurohere.kurohack.features.modules.client.HUD;
import me.kurohere.kurohack.features.setting.Setting;
import me.kurohere.kurohack.util.ColorUtil;
import me.kurohere.kurohack.util.MathUtil;
import me.kurohere.kurohack.util.RenderUtil;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;

public class BooleanButton
extends Button {
    private Setting setting;

    public BooleanButton(Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (ClickGui.getInstance().rainbowRolling.getValue().booleanValue()) {
            int color = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)), kuro.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            int color1 = ColorUtil.changeAlpha(HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)), kuro.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue());
            RenderUtil.drawGradientRect(this.x, this.y, (float)this.width + 7.4f, (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y, 0, this.renderer.scaledHeight)) : color) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515), this.getState() ? (!this.isHovering(mouseX, mouseY) ? HUD.getInstance().colorMap.get(MathUtil.clamp((int)this.y + this.height, 0, this.renderer.scaledHeight)) : color1) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        } else {
            RenderUtil.drawRect(this.x, this.y, this.x + (float)this.width + 7.4f, this.y + (float)this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? kuro.colorManager.getColorWithAlpha(kuro.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : kuro.colorManager.getColorWithAlpha(kuro.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
        }
        kuro.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float)kurohackGui.getClickGui().getTextOffset(), this.getState() ? -1 : -5592406);
    }

    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.getMasterRecord((SoundEvent)SoundEvents.UI_BUTTON_CLICK, (float)1.0f));
        }
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public void toggle() {
        this.setting.setValue((Boolean)this.setting.getValue() == false);
    }

    @Override
    public boolean getState() {
        return (Boolean)this.setting.getValue();
    }
}

