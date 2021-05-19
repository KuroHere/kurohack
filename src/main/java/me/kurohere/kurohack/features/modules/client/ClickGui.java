package me.kurohere.kurohack.features.modules.client;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.event.events.ClientEvent;
import me.kurohere.kurohack.features.command.Command;
import me.kurohere.kurohack.features.gui.kurohackGui;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.modules.client.Colors;
import me.kurohere.kurohack.features.setting.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui
extends Module {
    public Setting<Boolean> colorSync = this.register(new Setting<Boolean>("Sync", false));
    public Setting<Boolean> rainbowRolling = this.register(new Setting<Object>("RollingRainbow", Boolean.valueOf(false), v -> this.colorSync.getValue() != false && Colors.INSTANCE.rainbow.getValue() != false));
    public Setting<String> prefix = this.register(new Setting<String>("Prefix", ".").setRenderName(true));
    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 0, 0, 255));
    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> customFov = this.register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = this.register(new Setting<Object>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f), v -> this.customFov.getValue()));
    public Setting<Boolean> openCloseChange = this.register(new Setting<Boolean>("Open/Close", false));
    public Setting<String> open = this.register(new Setting<Object>("Open:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<String> close = this.register(new Setting<Object>("Close:", "", v -> this.openCloseChange.getValue()).setRenderName(true));
    public Setting<String> moduleButton = this.register(new Setting<Object>("Buttons:", "", v -> this.openCloseChange.getValue() == false).setRenderName(true));
    public Setting<Boolean> devSettings = this.register(new Setting<Boolean>("DevSettings", false));
    public Setting<Integer> topRed = this.register(new Setting<Object>("TopRed", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.devSettings.getValue()));
    public Setting<Integer> topGreen = this.register(new Setting<Object>("TopGreen", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.devSettings.getValue()));
    public Setting<Integer> topBlue = this.register(new Setting<Object>("TopBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> this.devSettings.getValue()));
    public Setting<Integer> topAlpha = this.register(new Setting<Object>("TopAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.devSettings.getValue()));
    private static ClickGui INSTANCE = new ClickGui();

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                kuro.commandManager.setPrefix(this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to \u00a7a" + kuro.commandManager.getPrefix());
            }
            kuro.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen((GuiScreen)new kurohackGui());
    }

    @Override
    public void onLoad() {
        if (this.colorSync.getValue().booleanValue()) {
            kuro.colorManager.setColor(Colors.INSTANCE.getCurrentColor().getRed(), Colors.INSTANCE.getCurrentColor().getGreen(), Colors.INSTANCE.getCurrentColor().getBlue(), this.hoverAlpha.getValue());
        } else {
            kuro.colorManager.setColor(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.hoverAlpha.getValue());
        }
        kuro.commandManager.setPrefix(this.prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof kurohackGui)) {
            this.disable();
        }
    }

    @Override
    public void onDisable() {
        if (ClickGui.mc.currentScreen instanceof kurohackGui) {
            mc.displayGuiScreen(null);
        }
    }
}

