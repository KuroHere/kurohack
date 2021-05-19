package me.kurohere.kurohack.features.modules.player;

import me.kurohere.kurohack.event.events.PacketEvent;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.setting.Setting;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoHunger extends Module {
    public Setting<Boolean> cancelSprint = register(new Setting("CancelSprint", Boolean.valueOf(true)));

    public NoHunger() {
        super("NoHunger", "Prevents you from getting Hungry", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.onGround = (mc.player.fallDistance >= 0.0F || mc.playerController.isHittingBlock);
        }
        if (((Boolean)this.cancelSprint.getValue()).booleanValue() && event.getPacket() instanceof CPacketEntityAction) {
            CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
            if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING)
                event.setCanceled(true);
        }
    }
}
