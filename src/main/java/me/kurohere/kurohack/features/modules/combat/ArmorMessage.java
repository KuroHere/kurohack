package me.kurohere.kurohack.features.modules.combat;

import java.util.HashMap;
import java.util.Map;
import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.event.events.UpdateWalkingPlayerEvent;
import me.kurohere.kurohack.features.command.Command;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.setting.Setting;
import me.kurohere.kurohack.util.DamageUtil;
import me.kurohere.kurohack.util.Timer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorMessage
extends Module {
    private final Setting<Integer> armorThreshhold = this.register(new Setting<Integer>("Armor%", 20, 1, 100));
    private final Setting<Boolean> notifySelf = this.register(new Setting<Boolean>("NotifySelf", true));
    private final Setting<Boolean> notification = this.register(new Setting<Boolean>("Notification", true));
    private final Map<EntityPlayer, Integer> entityArmorArraylist = new HashMap<EntityPlayer, Integer>();
    private final Timer timer = new Timer();

    public ArmorMessage() {
        super("ArmorMessage", "Message friends when their armor is low", Module.Category.COMBAT, true, false, false);
    }

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {
        for (EntityPlayer player : ArmorMessage.mc.world.playerEntities) {
            if (player.isDead || !kuro.friendManager.isFriend(player.getName())) continue;
            for (ItemStack stack : player.inventory.armorInventory) {
                if (stack == ItemStack.EMPTY) continue;
                int percent = DamageUtil.getRoundedDamage(stack);
                if (percent <= this.armorThreshhold.getValue() && !this.entityArmorArraylist.containsKey((Object)player)) {
                    if (player == ArmorMessage.mc.player && this.notifySelf.getValue().booleanValue()) {
                        Command.sendMessage(player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!", this.notification.getValue());
                    } else {
                        ArmorMessage.mc.player.sendChatMessage("/msg " + player.getName() + " " + player.getName() + " watchout your " + this.getArmorPieceName(stack) + " low dura!");
                    }
                    this.entityArmorArraylist.put(player, player.inventory.armorInventory.indexOf((Object)stack));
                }
                if (!this.entityArmorArraylist.containsKey((Object)player) || this.entityArmorArraylist.get((Object)player).intValue() != player.inventory.armorInventory.indexOf((Object)stack) || percent <= this.armorThreshhold.getValue()) continue;
                this.entityArmorArraylist.remove((Object)player);
            }
            if (!this.entityArmorArraylist.containsKey((Object)player) || player.inventory.armorInventory.get(this.entityArmorArraylist.get((Object)player).intValue()) != ItemStack.EMPTY) continue;
            this.entityArmorArraylist.remove((Object)player);
        }
    }

    private String getArmorPieceName(ItemStack stack) {
        if (stack.getItem() == Items.DIAMOND_HELMET || stack.getItem() == Items.GOLDEN_HELMET || stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET || stack.getItem() == Items.LEATHER_HELMET) {
            return "helmet is";
        }
        if (stack.getItem() == Items.DIAMOND_CHESTPLATE || stack.getItem() == Items.GOLDEN_CHESTPLATE || stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE || stack.getItem() == Items.LEATHER_CHESTPLATE) {
            return "chestplate is";
        }
        if (stack.getItem() == Items.DIAMOND_LEGGINGS || stack.getItem() == Items.GOLDEN_LEGGINGS || stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS || stack.getItem() == Items.LEATHER_LEGGINGS) {
            return "leggings are";
        }
        return "boots are";
    }
}

