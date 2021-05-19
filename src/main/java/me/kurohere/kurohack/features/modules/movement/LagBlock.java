package me.kurohere.kurohack.features.modules.movement;

import me.kurohere.kurohack.event.events.UpdateWalkingPlayerEvent;
import me.kurohere.kurohack.features.modules.Module;
import me.kurohere.kurohack.features.setting.Setting;
import me.kurohere.kurohack.util.BlockUtil;
import me.kurohere.kurohack.util.InventoryUtil;
import me.kurohere.kurohack.util.RotationUtil;
import me.kurohere.kurohack.util.Timer;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LagBlock
extends Module {
    private Setting<Boolean> packet = this.register(new Setting<Boolean>("Packet", true));
    private Setting<Boolean> invalidPacket = this.register(new Setting<Boolean>("InvalidPacket", false));
    private Setting<Integer> rotations = this.register(new Setting<Integer>("Rotations", 5, 1, 10));
    private Setting<Integer> timeOut = this.register(new Setting<Integer>("TimeOut", 194, 0, 1000));
    private BlockPos startPos;
    private final Timer timer = new Timer();
    private int lastHotbarSlot = -1;
    private int blockSlot = -1;
    private static LagBlock INSTANCE;

    public LagBlock() {
        super("BlockLag", "Lags You back", Module.Category.MOVEMENT, true, false, false);
        INSTANCE = this;
    }

    public static LagBlock getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LagBlock();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        this.lastHotbarSlot = -1;
        this.blockSlot = -1;
        if (LagBlock.fullNullCheck()) {
            this.disable();
            return;
        }
        this.blockSlot = this.findBlockSlot();
        this.startPos = new BlockPos(LagBlock.mc.player.getPositionVector());
        if (!BlockUtil.isElseHole(this.startPos) || this.blockSlot == -1) {
            this.disable();
            return;
        }
        LagBlock.mc.player.jump();
        this.timer.reset();
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0 || !this.timer.passedMs(this.timeOut.getValue().intValue())) {
            return;
        }
        this.lastHotbarSlot = LagBlock.mc.player.inventory.currentItem;
        InventoryUtil.switchToHotbarSlot(this.blockSlot, false);
        for (int i = 0; i < this.rotations.getValue(); ++i) {
            RotationUtil.faceVector(new Vec3d((Vec3i)this.startPos), true);
        }
        BlockUtil.placeBlock(this.startPos, this.blockSlot == -2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, false, this.packet.getValue(), LagBlock.mc.player.isSneaking());
        InventoryUtil.switchToHotbarSlot(this.lastHotbarSlot, false);
        if (this.invalidPacket.getValue().booleanValue()) {
            LagBlock.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(LagBlock.mc.player.posX, 1337.0, LagBlock.mc.player.posZ, true));
        }
        this.disable();
    }

    private int findBlockSlot() {
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        if (obbySlot == -1) {
            if (InventoryUtil.isBlock(LagBlock.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class)) {
                return -2;
            }
            int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (echestSlot == -1 && InventoryUtil.isBlock(LagBlock.mc.player.getHeldItemOffhand().getItem(), BlockEnderChest.class)) {
                return -2;
            }
            return -1;
        }
        return obbySlot;
    }
}

