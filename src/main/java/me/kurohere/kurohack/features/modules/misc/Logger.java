package me.kurohere.kurohack.features.modules.misc;

import java.lang.reflect.Field;
import me.kurohere.kurohack.event.events.PacketEvent;
import me.kurohere.kurohack.features.command.Command;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.setting.Setting;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.network.Packet;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Logger
extends Module {
    public Setting<Packets> packets = this.register(new Setting<Packets>("Packets", Packets.OUTGOING));
    public Setting<Boolean> chat = this.register(new Setting<Boolean>("Chat", false));
    public Setting<Boolean> fullInfo = this.register(new Setting<Boolean>("FullInfo", false));
    public Setting<Boolean> noPing = this.register(new Setting<Boolean>("NoPing", false));

    public Logger() {
        super("Logger", "Logs stuff", Module.Category.MISC, true, false, false);
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onPacketSend(PacketEvent.Send event) {
        if (this.noPing.getValue().booleanValue() && Logger.mc.currentScreen instanceof GuiMultiplayer) {
            return;
        }
        if (this.packets.getValue() == Packets.OUTGOING || this.packets.getValue() == Packets.ALL) {
            if (this.chat.getValue().booleanValue()) {
                Command.sendMessage(event.getPacket().toString());
            } else {
                this.writePacketOnConsole((Packet<?>)event.getPacket(), false);
            }
        }
    }

    @SubscribeEvent(receiveCanceled=true)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.noPing.getValue().booleanValue() && Logger.mc.currentScreen instanceof GuiMultiplayer) {
            return;
        }
        if (this.packets.getValue() == Packets.INCOMING || this.packets.getValue() == Packets.ALL) {
            if (this.chat.getValue().booleanValue()) {
                Command.sendMessage(event.getPacket().toString());
            } else {
                this.writePacketOnConsole((Packet<?>)event.getPacket(), true);
            }
        }
    }

    private void writePacketOnConsole(Packet<?> packet, boolean in) {
        if (this.fullInfo.getValue().booleanValue()) {
            System.out.println((in ? "In: " : "Send: ") + packet.getClass().getSimpleName() + " {");
            try {
                for (Class<?> clazz = packet.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (Field field : clazz.getDeclaredFields()) {
                        if (field == null) continue;
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        System.out.println(StringUtils.stripControlCodes((String)("      " + field.getType().getSimpleName() + " " + field.getName() + " : " + field.get(packet))));
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("}");
        } else {
            System.out.println(packet.toString());
        }
    }

    public static enum Packets {
        NONE,
        INCOMING,
        OUTGOING,
        ALL;

    }
}

