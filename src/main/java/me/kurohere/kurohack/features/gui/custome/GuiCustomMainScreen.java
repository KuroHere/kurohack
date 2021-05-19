package me.kurohere.kurohack.features.gui.custome;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiCustomMainScreen
extends GuiScreen {
    private ResourceLocation resourceLocation = new ResourceLocation("textures/background.png");
    private final String backgroundURL = "https://i.imgur.com/C9WdEYM.png";
    private int y;
    private int x;
    private int singleplayerWidth;
    private int multiplayerWidth;
    private int settingsWidth;
    private int OutgameWidth;
    private int textHeight;
    private float xOffset;
    private float yOffset;

    public void initGui() {
        this.buttonList.clear();
        this.x = this.width / 3;
        this.y = this.height / 4 + 48;
        this.buttonList.add(new TextButton(0, this.x, this.y + 20, "Singleplayer"));
        this.buttonList.add(new TextButton(1, this.x, this.y + 44, "Multiplayer"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 66, "Settings"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 88, "Outgame"));
    }

    protected void actionPerformed(GuiButton button) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiCustomMainScreen.isHovered(this.x - kuro.textManager.getStringWidth("Singleplayer") / 2, this.y + 20, kuro.textManager.getStringWidth("Singleplayer"), kuro.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiWorldSelection((GuiScreen)this));
        } else if (GuiCustomMainScreen.isHovered(this.x - kuro.textManager.getStringWidth("Multiplayer") / 2, this.y + 44, kuro.textManager.getStringWidth("Multiplayer"), kuro.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
        } else if (GuiCustomMainScreen.isHovered(this.x - kuro.textManager.getStringWidth("Settings") / 2, this.y + 66, kuro.textManager.getStringWidth("Settings"), kuro.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, this.mc.gameSettings));
        } else if (GuiCustomMainScreen.isHovered(this.x - kuro.textManager.getStringWidth("Outgame") / 2, this.y + 88, kuro.textManager.getStringWidth("Outgame"), kuro.textManager.getFontHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.xOffset = -1.0f * (((float)mouseX - (float)this.width / 2.0f) / ((float)this.width / 32.0f));
        this.yOffset = -1.0f * (((float)mouseY - (float)this.height / 2.0f) / ((float)this.height / 18.0f));
        this.x = this.width / 3;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        GuiCustomMainScreen.drawCompleteImage(-16.0f + this.xOffset, -9.0f + this.yOffset, this.width + 32, this.height + 18);
        this.mc.getTextureManager().deleteTexture(this.resourceLocation);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)posX, (float)posY, (float)0.0f);
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex3f((float)0.0f, (float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex3f((float)0.0f, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex3f((float)width, (float)height, (float)0.0f);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex3f((float)width, (float)0.0f, (float)0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public BufferedImage parseBackground(BufferedImage background) {
        int height;
        int width = 1920;
        int srcWidth = background.getWidth();
        int srcHeight = background.getHeight();
        for (height = 1080; width < srcWidth || height < srcHeight; width *= 2, height *= 2) {
        }
        BufferedImage imgNew = new BufferedImage(width, height, 2);
        Graphics g = imgNew.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        return imgNew;
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }

    private static class TextButton
    extends GuiButton {
        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, kuro.textManager.getStringWidth(buttonText), kuro.textManager.getFontHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float)mouseX >= (float)this.x - (float)kuro.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                kuro.textManager.drawStringWithShadow(this.displayString, (float)this.x - (float)kuro.textManager.getStringWidth(this.displayString) / 2.0f, this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine((float)(this.x - 1) - (float)kuro.textManager.getStringWidth(this.displayString) / 2.0f, this.y + 2 + kuro.textManager.getFontHeight(), (float)this.x + (float)kuro.textManager.getStringWidth(this.displayString) / 2.0f + 1.0f, this.y + 2 + kuro.textManager.getFontHeight(), 1.0f, Color.WHITE.getRGB());
                }
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float)mouseX >= (float)this.x - (float)kuro.textManager.getStringWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }
}

