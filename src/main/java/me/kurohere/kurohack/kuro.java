package me.kurohere.kurohack;

import me.kurohere.kurohack.DiscordPresence;
import me.kurohere.kurohack.features.gui.custom.GuiCustomMainScreen;
import me.kurohere.kurohack.features.modules.misc.RPC;
import me.kurohere.kurohack.manager.ColorManager;
import me.kurohere.kurohack.manager.CommandManager;
import me.kurohere.kurohack.manager.ConfigManager;
import me.kurohere.kurohack.manager.CosmeticsManager;
import me.kurohere.kurohack.manager.EventManager;
import me.kurohere.kurohack.manager.FileManager;
import me.kurohere.kurohack.manager.FriendManager;
import me.kurohere.kurohack.manager.HoleManager;
import me.kurohere.kurohack.manager.InventoryManager;
import me.kurohere.kurohack.manager.ModuleManager;
import me.kurohere.kurohack.manager.NoStopManager;
import me.kurohere.kurohack.manager.NotificationManager;
import me.kurohere.kurohack.manager.PacketManager;
import me.kurohere.kurohack.manager.PositionManager;
import me.kurohere.kurohack.manager.PotionManager;
import me.kurohere.kurohack.manager.ReloadManager;
import me.kurohere.kurohack.manager.RotationManager;
import me.kurohere.kurohack.manager.SafetyManager;
import me.kurohere.kurohack.manager.ServerManager;
import me.kurohere.kurohack.manager.SpeedManager;
import me.kurohere.kurohack.manager.TextManager;
import me.kurohere.kurohack.manager.TimerManager;
import me.kurohere.kurohack.manager.TotemPopManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid="kurohack", name="kurohack", version="beta0.1")
public class kuro {

    @Mod.Instance
    public static kuro INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("ohare is cute!!!");
        LOGGER.info("faggot above - 3vt");
        LOGGER.info("megyn wins again");
        LOGGER.info("gtfo my logs - 3arth");
        LOGGER.info("hqrion is chad");
        LOGGER.info("ciruu is epic :o");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        public static final String MODID = "kurohack";
        public static final String MODNAME = "kurohack";
        public static final String MODVER = "Beta0.1";
        public static final String KURO_UNICODE = "3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
        public static final String KURO_UNICODE = "\u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
        public static final String CHAT_SUFFIX = " \u23d0 3\u1d00\u0280\u1d1b\u029c\u029c4\u1d04\u1d0b";
        public static final String KURO_SUFFIX = " \u23d0 \u1d18\u029c\u1d0f\u0299\u1d0f\ua731";
        public static final Logger LOGGER = LogManager.getLogger((String)"kurohack");
        public static ModuleManager moduleManager;
        public static SpeedManager speedManager;
        public static PositionManager positionManager;
        public static RotationManager rotationManager;
        public static CommandManager commandManager;
        public static EventManager eventManager;
        public static ConfigManager configManager;
        public static FileManager fileManager;
        public static FriendManager friendManager;
        public static TextManager textManager;
        public static ColorManager colorManager;
        public static ServerManager serverManager;
        public static PotionManager potionManager;
        public static InventoryManager inventoryManager;
        public static TimerManager timerManager;
        public static PacketManager packetManager;
        public static ReloadManager reloadManager;
        public static TotemPopManager totemPopManager;
        public static HoleManager holeManager;
        public static NotificationManager notificationManager;
        public static SafetyManager safetyManager;
        public static GuiCustomMainScreen customMainScreen;
        public static CosmeticsManager cosmeticsManager;
        public static NoStopManager baritoneManager;
        private static boolean unloaded; customMainScreen = new GuiCustomMainScreen();
        Display.setTitle("kurohack - Beta0.1");
        kuro.load();
    }

    public static void load() {
        LOGGER.info("\n\nLoading kurohack Beta0.1");
        unloaded = false;
        if (reloadManager != null) {
            reloadManager.unload();
            reloadManager = null;
        }
        baritoneManager = new NoStopManager();
        totemPopManager = new TotemPopManager();
        timerManager = new TimerManager();
        packetManager = new PacketManager();
        serverManager = new ServerManager();
        colorManager = new ColorManager();
        textManager = new TextManager();
        moduleManager = new ModuleManager();
        speedManager = new SpeedManager();
        rotationManager = new RotationManager();
        positionManager = new PositionManager();
        commandManager = new CommandManager();
        eventManager = new EventManager();
        configManager = new ConfigManager();
        fileManager = new FileManager();
        friendManager = new FriendManager();
        potionManager = new PotionManager();
        inventoryManager = new InventoryManager();
        holeManager = new HoleManager();
        notificationManager = new NotificationManager();
        safetyManager = new SafetyManager();
        LOGGER.info("Initialized Managers");
        moduleManager.init();
        LOGGER.info("Modules loaded.");
        configManager.init();
        eventManager.init();
        LOGGER.info("EventManager loaded.");
        textManager.init(true);
        moduleManager.onLoad();
        totemPopManager.init();
        timerManager.init();
        if (moduleManager.getModuleByClass(RPC.class).isEnabled()) {
            DiscordPresence.start();
        }
        cosmeticsManager = new CosmeticsManager();
        LOGGER.info("kurohack initialized!\n");
    }

    public static void unload(boolean unload) {
        LOGGER.info("\n\nUnloading kurohack beta0.1");
        if (unload) {
            reloadManager = new ReloadManager();
            reloadManager.init(commandManager != null ? commandManager.getPrefix() : ".");
        }
        if (baritoneManager != null) {
            baritoneManager.stop();
        }
        kuro.onUnload();
        eventManager = null;
        holeManager = null;
        timerManager = null;
        moduleManager = null;
        totemPopManager = null;
        serverManager = null;
        colorManager = null;
        textManager = null;
        speedManager = null;
        rotationManager = null;
        positionManager = null;
        commandManager = null;
        configManager = null;
        fileManager = null;
        friendManager = null;
        potionManager = null;
        inventoryManager = null;
        notificationManager = null;
        safetyManager = null;
        LOGGER.info("kurohack unloaded!\n");
    }

    public static void reload() {
        kuro.unload(false);
        kuro.load();
    }

    public static void onUnload() {
        if (!unloaded) {
            eventManager.onUnload();
            moduleManager.onUnload();
            configManager.saveConfig(kuro.configManager.config.replaceFirst("kuro/", ""));
            moduleManager.onUnloadPost();
            timerManager.unload();
            unloaded = true;
        }
    }

    static {
        unloaded = false;
    }
}

