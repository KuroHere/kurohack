package kuro.here.hack;

import kuro.here.hack.api.EventHandler;
import kuro.here.hack.api.command.CommandManager;
import kuro.here.hack.api.gui.clickgui.ClickGUI;
import kuro.here.hack.api.manager.FriendManager;
import kuro.here.hack.api.module.ModuleManager;
import kuro.here.hack.api.setting.SettingManager;
import kuro.here.hack.api.manager.PositionManager;
import kuro.here.hack.api.util.font.CustomFontRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import dev.kohimanayagato.serenity.api.manager.ConfigManager;

import java.awt.*;
import java.io.IOException;

@Mod(modid = "kurohack", name = "kurohack", version = "beta0.1")
public class Client
{
	public static ModuleManager moduleManager;
	public static SettingManager settingManager;
        public static PositionManager positionManager;
	public static CustomFontRenderer customFontRenderer;
	public static ClickGUI clickGUI;
	public static CommandManager commandManager;
	public static FriendManager friendManager;

	@Mod.EventHandler
	public void initialize(FMLInitializationEvent event) throws IOException {
		commandManager = new CommandManager();
		settingManager = new SettingManager();
                positionManager = new PositionManager();
		moduleManager = new ModuleManager();
		friendManager = new FriendManager();
		customFontRenderer = new CustomFontRenderer(new Font("Verdana", Font.PLAIN, 19), true, false);
		clickGUI = new ClickGUI();

		ConfigManager.loadConfig();

		Runtime.getRuntime().addShutdownHook(new ConfigManager());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	public static ModuleManager getModuleManager()
	{
		return moduleManager;
	}
	public static FriendManager getFriendManager()
	{
		return friendManager;
	}
}
