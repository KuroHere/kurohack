package me.kurohere.kurohack.features.modules.movement;

import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.event.events.KeyEvent;
import me.kurohere.kurohack.event.events.PacketEvent;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.modules.movement.Flight;
import me.kurohere.kurohack.features.modules.movement.Phase;
import me.kurohere.kurohack.features.setting.Setting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreenOptionsSounds;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class NoSlowDown
extends Module {
    public Setting<Boolean> guiMove = this.register(new Setting<Boolean>("GuiMove", true));
    public Setting<Boolean> noSlow = this.register(new Setting<Boolean>("NoSlow", true));
    public Setting<Boolean> soulSand = this.register(new Setting<Boolean>("SoulSand", true));
    public Setting<Boolean> strict = this.register(new Setting<Boolean>("Strict", false));
    public Setting<Boolean> webs = this.register(new Setting<Boolean>("Webs", false));
    public final Setting<Double> webHorizontalFactor = this.register(new Setting<Double>("WebHSpeed", 2.0, 0.0, 100.0));
    public final Setting<Double> webVerticalFactor = this.register(new Setting<Double>("WebVSpeed", 2.0, 0.0, 100.0));
    private static NoSlowDown INSTANCE = new NoSlowDown();
    private static KeyBinding[] keys = new KeyBinding[]{NoSlowDown.mc.gameSettings.keyBindForward, NoSlowDown.mc.gameSettings.keyBindBack, NoSlowDown.mc.gameSettings.keyBindLeft, NoSlowDown.mc.gameSettings.keyBindRight, NoSlowDown.mc.gameSettings.keyBindJump, NoSlowDown.mc.gameSettings.keyBindSprint};

    public NoSlowDown() {
        super("NoSlowDown", "Prevents you from getting slowed down.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static NoSlowDown getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoSlowDown();
        }
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
        if (this.guiMove.getValue().booleanValue()) {
            if (NoSlowDown.mc.currentScreen instanceof GuiOptions || NoSlowDown.mc.currentScreen instanceof GuiVideoSettings || NoSlowDown.mc.currentScreen instanceof GuiScreenOptionsSounds || NoSlowDown.mc.currentScreen instanceof GuiContainer || NoSlowDown.mc.currentScreen instanceof GuiIngameMenu) {
                for (KeyBinding bind : keys) {
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)Keyboard.isKeyDown((int)bind.getKeyCode()));
                }
            } else if (NoSlowDown.mc.currentScreen == null) {
                for (KeyBinding bind : keys) {
                    if (Keyboard.isKeyDown((int)bind.getKeyCode())) continue;
                    KeyBinding.setKeyBindState((int)bind.getKeyCode(), (boolean)false);
                }
            }
        }
        if (this.webs.getValue().booleanValue() && kuro.moduleManager.getModuleByClass(Flight.class).isDisabled() && kuro.moduleManager.getModuleByClass(Phase.class).isDisabled() && NoSlowDown.mc.player.isInWeb) {
            NoSlowDown.mc.player.motionX *= this.webHorizontalFactor.getValue().doubleValue();
            NoSlowDown.mc.player.motionZ *= this.webHorizontalFactor.getValue().doubleValue();
            NoSlowDown.mc.player.motionY *= this.webVerticalFactor.getValue().doubleValue();
        }
    }

    @SubscribeEvent
    public void onInput(InputUpdateEvent event) {
        if (this.noSlow.getValue().booleanValue() && NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5.0f;
            event.getMovementInput().moveForward *= 5.0f;
        }
    }

    @SubscribeEvent
    public void onKeyEvent(KeyEvent event) {
        if (this.guiMove.getValue().booleanValue() && event.getStage() == 0 && !(NoSlowDown.mc.currentScreen instanceof GuiChat)) {
            event.info = event.pressed;
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && this.strict.getValue().booleanValue() && this.noSlow.getValue().booleanValue() && NoSlowDown.mc.player.isHandActive() && !NoSlowDown.mc.player.isRiding()) {
            NoSlowDown.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, new BlockPos(Math.floor(NoSlowDown.mc.player.posX), Math.floor(NoSlowDown.mc.player.posY), Math.floor(NoSlowDown.mc.player.posZ)), EnumFacing.DOWN));
        }
    }
}

