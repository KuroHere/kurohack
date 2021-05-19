package me.kurohere.kurohack.features.modules.misc;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.kurohere.kurohack.event.events.PacketEvent;
import me.kurohere.kurohack.features.command.Command;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.util.PlayerUtil;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiVanish
extends Module {
    private final Queue<UUID> toLookUp = new ConcurrentLinkedQueue<UUID>();

    public AntiVanish() {
        super("AntiVanish", "Notifies you when players vanish", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        SPacketPlayerListItem sPacketPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (sPacketPlayerListItem = (SPacketPlayerListItem)event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            for (SPacketPlayerListItem.AddPlayerData addPlayerData : sPacketPlayerListItem.getEntries()) {
                try {
                    if (mc.getConnection().getPlayerInfo(addPlayerData.getProfile().getId()) != null) continue;
                    this.toLookUp.add(addPlayerData.getProfile().getId());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        UUID lookUp;
        if (PlayerUtil.timer.passedS(5.0) && (lookUp = this.toLookUp.poll()) != null) {
            try {
                String name = PlayerUtil.getNameFromUUID(lookUp);
                if (name != null) {
                    Command.sendMessage("\u00a7c" + name + " has gone into vanish.");
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            PlayerUtil.timer.reset();
        }
    }

    @Override
    public void onLogout() {
        this.toLookUp.clear();
    }
}

