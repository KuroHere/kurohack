package me.kurohere.kurohack.util;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Objects;
import me.kurohere.kurohack.kuro;
import me.kurohere.kurohack.util.ColorUtil;
import me.kurohere.kurohack.util.EntityUtil;
import me.kurohere.kurohack.util.Util;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

public class RenderUtil
implements Util {
    public static RenderItem itemRender = mc.getRenderItem();
    public static ICamera camera = new Frustum();
    private static final Frustum frustrum = new Frustum();
    private static boolean depth = GL11.glIsEnabled((int)2896);
    private static boolean texture = GL11.glIsEnabled((int)3042);
    private static boolean clean = GL11.glIsEnabled((int)3553);
    private static boolean bind = GL11.glIsEnabled((int)2929);
    private static boolean override = GL11.glIsEnabled((int)2848);
    private static final FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int)3);
    private static final IntBuffer viewport = BufferUtils.createIntBuffer((int)16);
    private static final FloatBuffer modelView = BufferUtils.createFloatBuffer((int)16);
    private static final FloatBuffer projection = BufferUtils.createFloatBuffer((int)16);

    public static void drawRectangleCorrectly(int x, int y, int w, int h, int color) {
        GL11.glLineWidth((float)1.0f);
        Gui.drawRect((int)x, (int)y, (int)(x + w), (int)(y + h), (int)color);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtil.mc.getRenderManager().viewerPosX, bb.minY - RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void drawTexturedRect(int x, int y, int textureX, int textureY, int width, int height, int zLevel) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder BufferBuilder2 = tessellator.getBuffer();
        BufferBuilder2.begin(7, DefaultVertexFormats.POSITION_TEX);
        BufferBuilder2.pos((double)(x + 0), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).endVertex();
        BufferBuilder2.pos((double)(x + width), (double)(y + height), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + height) * 0.00390625f)).endVertex();
        BufferBuilder2.pos((double)(x + width), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + width) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).endVertex();
        BufferBuilder2.pos((double)(x + 0), (double)(y + 0), (double)zLevel).tex((double)((float)(textureX + 0) * 0.00390625f), (double)((float)(textureY + 0) * 0.00390625f)).endVertex();
        tessellator.draw();
    }

    public static void drawGradientRect(int x, int y, int w, int h, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)x + (double)w, (double)y, 0.0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)x, (double)y, 0.0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)x, (double)y + (double)h, 0.0).color(f5, f6, f7, f4).endVertex();
        vertexbuffer.pos((double)x + (double)w, (double)y + (double)h, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGradientBlockOutline(BlockPos pos, Color startColor, Color endColor, float linewidth) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawGradientBlockOutline(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), startColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(BlockPos pos, Color startColor, Color midColor, Color endColor, float linewidth) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawProperGradientBlockOutline(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), startColor, midColor, endColor, linewidth);
    }

    public static void drawProperGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color midColor, Color endColor, float linewidth) {
        float red = (float)endColor.getRed() / 255.0f;
        float green = (float)endColor.getGreen() / 255.0f;
        float blue = (float)endColor.getBlue() / 255.0f;
        float alpha = (float)endColor.getAlpha() / 255.0f;
        float red1 = (float)midColor.getRed() / 255.0f;
        float green1 = (float)midColor.getGreen() / 255.0f;
        float blue1 = (float)midColor.getBlue() / 255.0f;
        float alpha1 = (float)midColor.getAlpha() / 255.0f;
        float red2 = (float)startColor.getRed() / 255.0f;
        float green2 = (float)startColor.getGreen() / 255.0f;
        float blue2 = (float)startColor.getBlue() / 255.0f;
        float alpha2 = (float)startColor.getAlpha() / 255.0f;
        double dif = (bb.maxY - bb.minY) / 2.0;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        GL11.glBegin((int)1);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.minZ);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.minX, (double)(bb.minY + dif), (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)(bb.minY + dif), (double)bb.minZ);
        GL11.glColor4f((float)red2, (float)green2, (float)blue2, (float)alpha2);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.minX, (double)bb.minY, (double)bb.maxZ);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.minX, (double)(bb.minY + dif), (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)(bb.minY + dif), (double)bb.maxZ);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.maxZ);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.maxX, (double)(bb.minY + dif), (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)(bb.minY + dif), (double)bb.maxZ);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glColor4d((double)red, (double)green, (double)blue, (double)alpha);
        GL11.glVertex3d((double)bb.maxX, (double)bb.minY, (double)bb.minZ);
        GL11.glColor4d((double)red1, (double)green1, (double)blue1, (double)alpha1);
        GL11.glVertex3d((double)bb.maxX, (double)(bb.minY + dif), (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)(bb.minY + dif), (double)bb.minZ);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glColor4d((double)red2, (double)green2, (double)blue2, (double)alpha2);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.maxX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.maxZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glVertex3d((double)bb.minX, (double)bb.maxY, (double)bb.minZ);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color endColor, float linewidth) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientFilledBox(BlockPos pos, Color startColor, Color endColor) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawGradientFilledBox(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), startColor, endColor);
    }

    public static void drawGradientFilledBox(AxisAlignedBB bb, Color startColor, Color endColor) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        float alpha = (float)endColor.getAlpha() / 255.0f;
        float red = (float)endColor.getRed() / 255.0f;
        float green = (float)endColor.getGreen() / 255.0f;
        float blue = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)startColor.getRed() / 255.0f;
        float green1 = (float)startColor.getGreen() / 255.0f;
        float blue1 = (float)startColor.getBlue() / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawGradientRect(float x, float y, float w, float h, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel((int)7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos((double)x + (double)w, (double)y, 0.0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)x, (double)y, 0.0).color(f1, f2, f3, f).endVertex();
        vertexbuffer.pos((double)x, (double)y + (double)h, 0.0).color(f5, f6, f7, f4).endVertex();
        vertexbuffer.pos((double)x + (double)w, (double)y + (double)h, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void blockESP(BlockPos b, Color c, double length, double length2) {
        RenderUtil.blockEsp(b, c, length, length2);
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha));
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air);
        }
    }

    public static void drawBoxESP(BlockPos pos, Color color, boolean secondC, Color secondColor, float lineWidth, boolean outline, boolean box, int boxAlpha, boolean air, double height) {
        if (box) {
            RenderUtil.drawBox(pos, new Color(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha), height);
        }
        if (outline) {
            RenderUtil.drawBlockOutline(pos, secondC ? secondColor : color, lineWidth, air, height);
        }
    }

    public static void glScissor(float x, float y, float x1, float y1, ScaledResolution sr) {
        GL11.glScissor((int)((int)(x * (float)sr.getScaleFactor())), (int)((int)((float)RenderUtil.mc.displayHeight - y1 * (float)sr.getScaleFactor())), (int)((int)((x1 - x) * (float)sr.getScaleFactor())), (int)((int)((y1 - y) * (float)sr.getScaleFactor())));
    }

    public static void drawLine(float x, float y, float x1, float y1, float thickness, int hex) {
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GlStateManager.shadeModel((int)7425);
        GL11.glLineWidth((float)thickness);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel((int)7424);
        GL11.glDisable((int)2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos pos, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        double offset = (bb.maxY - bb.minY) / 2.0;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    }

    public static void drawBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)midColor.getRed() / 255.0f;
        float green2 = (float)midColor.getGreen() / 255.0f;
        float blue2 = (float)midColor.getBlue() / 255.0f;
        float alpha2 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        double offset = (bb.maxY - bb.minY) / 2.0;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.minZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.minY + offset, bb.maxZ).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawEvenBetterGradientBox(BlockPos pos, Color startColor, Color midColor, Color endColor) {
        float red = (float)startColor.getRed() / 255.0f;
        float green = (float)startColor.getGreen() / 255.0f;
        float blue = (float)startColor.getBlue() / 255.0f;
        float alpha = (float)startColor.getAlpha() / 255.0f;
        float red1 = (float)endColor.getRed() / 255.0f;
        float green1 = (float)endColor.getGreen() / 255.0f;
        float blue1 = (float)endColor.getBlue() / 255.0f;
        float alpha1 = (float)endColor.getAlpha() / 255.0f;
        float red2 = (float)midColor.getRed() / 255.0f;
        float green2 = (float)midColor.getGreen() / 255.0f;
        float blue2 = (float)midColor.getBlue() / 255.0f;
        float alpha2 = (float)midColor.getAlpha() / 255.0f;
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        double offset = (bb.maxY - bb.minY) / 2.0;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.minZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.maxX, bb.minY, bb.maxZ).color(red1, green1, blue1, alpha1).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        builder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBox(BlockPos pos, Color color, double height) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
            RenderUtil.drawBlockOutline(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), color, linewidth);
        }
    }

    public static void drawBlockOutline(BlockPos pos, Color color, float linewidth, boolean air, double height) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        if ((air || iblockstate.getMaterial() != Material.AIR) && RenderUtil.mc.world.getWorldBorder().contains(pos)) {
            AxisAlignedBB blockAxis = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY + height, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
            RenderUtil.drawBlockOutline(blockAxis.grow((double)0.002f), color, linewidth);
        }
    }

    public static void drawBlockOutline(AxisAlignedBB bb, Color color, float linewidth) {
        float red = (float)color.getRed() / 255.0f;
        float green = (float)color.getGreen() / 255.0f;
        float blue = (float)color.getBlue() / 255.0f;
        float alpha = (float)color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)linewidth);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoxESP(BlockPos pos, Color color, float lineWidth, boolean outline, boolean box, int boxAlpha) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(RenderUtil.mc.getRenderViewEntity()).posX, RenderUtil.mc.getRenderViewEntity().posY, RenderUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + RenderUtil.mc.getRenderManager().viewerPosX, bb.minY + RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ + RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX + RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY + RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ + RenderUtil.mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)lineWidth);
            double dist = RenderUtil.mc.player.getDistance((double)((float)pos.getX() + 0.5f), (double)((float)pos.getY() + 0.5f), (double)((float)pos.getZ() + 0.5f)) * 0.75;
            if (box) {
                RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)boxAlpha / 255.0f));
            }
            if (outline) {
                RenderGlobal.drawBoundingBox((double)bb.minX, (double)bb.minY, (double)bb.minZ, (double)bb.maxX, (double)bb.maxY, (double)bb.maxZ, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            }
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawText(BlockPos pos, String text) {
        if (pos == null || text == null) {
            return;
        }
        GlStateManager.pushMatrix();
        RenderUtil.glBillboardDistanceScaled((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, (EntityPlayer)RenderUtil.mc.player, 1.0f);
        GlStateManager.disableDepth();
        GlStateManager.translate((double)(-((double)kuro.textManager.getStringWidth(text) / 2.0)), (double)0.0, (double)0.0);
        kuro.textManager.drawStringWithShadow(text, 0.0f, 0.0f, -5592406);
        GlStateManager.popMatrix();
    }

    public static void drawOutlinedBlockESP(BlockPos pos, Color color, float linewidth) {
        IBlockState iblockstate = RenderUtil.mc.world.getBlockState(pos);
        Vec3d interp = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, mc.getRenderPartialTicks());
        RenderUtil.drawBoundingBox(iblockstate.getSelectedBoundingBox((World)RenderUtil.mc.world, pos).grow((double)0.002f).offset(-interp.x, -interp.y, -interp.z), linewidth, ColorUtil.toRGBA(color));
    }

    public static void blockEsp(BlockPos blockPos, Color c, double length, double length2) {
        double x = (double)blockPos.getX() - RenderUtil.mc.renderManager.renderPosX;
        double y = (double)blockPos.getY() - RenderUtil.mc.renderManager.renderPosY;
        double z = (double)blockPos.getZ() - RenderUtil.mc.renderManager.renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glColor4d((double)((float)c.getRed() / 255.0f), (double)((float)c.getGreen() / 255.0f), (double)((float)c.getBlue() / 255.0f), (double)0.25);
        RenderUtil.drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glColor4d((double)0.0, (double)0.0, (double)0.0, (double)0.5);
        RenderUtil.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)w, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void glrendermethod() {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)2.0f);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2884);
        GL11.glDisable((int)2929);
        double viewerPosX = RenderUtil.mc.getRenderManager().viewerPosX;
        double viewerPosY = RenderUtil.mc.getRenderManager().viewerPosY;
        double viewerPosZ = RenderUtil.mc.getRenderManager().viewerPosZ;
        GL11.glPushMatrix();
        GL11.glTranslated((double)(-viewerPosX), (double)(-viewerPosY), (double)(-viewerPosZ));
    }

    public static void glStart(float n, float n2, float n3, float n4) {
        RenderUtil.glrendermethod();
        GL11.glColor4f((float)n, (float)n2, (float)n3, (float)n4);
    }

    public static void glEnd() {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glPopMatrix();
        GL11.glEnable((int)2929);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static AxisAlignedBB getBoundingBox(BlockPos blockPos) {
        return RenderUtil.mc.world.getBlockState(blockPos).getBoundingBox((IBlockAccess)RenderUtil.mc.world, blockPos).offset(blockPos);
    }

    public static void drawOutlinedBox(AxisAlignedBB axisAlignedBB) {
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.minY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.maxX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.maxZ);
        GL11.glVertex3d((double)axisAlignedBB.minX, (double)axisAlignedBB.maxY, (double)axisAlignedBB.minZ);
        GL11.glEnd();
    }

    public static void drawFilledBoxESPN(BlockPos pos, Color color) {
        AxisAlignedBB bb = new AxisAlignedBB((double)pos.getX() - RenderUtil.mc.getRenderManager().viewerPosX, (double)pos.getY() - RenderUtil.mc.getRenderManager().viewerPosY, (double)pos.getZ() - RenderUtil.mc.getRenderManager().viewerPosZ, (double)(pos.getX() + 1) - RenderUtil.mc.getRenderManager().viewerPosX, (double)(pos.getY() + 1) - RenderUtil.mc.getRenderManager().viewerPosY, (double)(pos.getZ() + 1) - RenderUtil.mc.getRenderManager().viewerPosZ);
        int rgba = ColorUtil.toRGBA(color);
        RenderUtil.drawFilledBox(bb, rgba);
    }

    public static void drawFilledBox(AxisAlignedBB bb, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoundingBox(AxisAlignedBB bb, float width, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.translate((double)((double)x - RenderUtil.mc.getRenderManager().renderPosX), (double)((double)y - RenderUtil.mc.getRenderManager().renderPosY), (double)((double)z - RenderUtil.mc.getRenderManager().renderPosZ));
        GlStateManager.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-RenderUtil.mc.player.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)RenderUtil.mc.player.rotationPitch, (float)(RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)(-scale), (float)(-scale), (float)scale);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int)player.getDistance((double)x, (double)y, (double)z);
        float scaleDistance = (float)distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale((float)scaleDistance, (float)scaleDistance, (float)scaleDistance);
    }

    public static void drawColoredBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glLineWidth((float)width);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, 0.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, 0.0f).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, 0.0f).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, 0.0f).endVertex();
        tessellator.draw();
        GL11.glDisable((int)2848);
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
        Sphere s = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glLineWidth((float)1.2f);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        s.setDrawStyle(100013);
        GL11.glTranslated((double)(x - RenderUtil.mc.renderManager.renderPosX), (double)(y - RenderUtil.mc.renderManager.renderPosY), (double)(z - RenderUtil.mc.renderManager.renderPosZ));
        s.draw(size, slices, stacks);
        GL11.glLineWidth((float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    public static void GLPre(float lineWidth) {
        depth = GL11.glIsEnabled((int)2896);
        texture = GL11.glIsEnabled((int)3042);
        clean = GL11.glIsEnabled((int)3553);
        bind = GL11.glIsEnabled((int)2929);
        override = GL11.glIsEnabled((int)2848);
        RenderUtil.GLPre(depth, texture, clean, bind, override, lineWidth);
    }

    public static void GlPost() {
        RenderUtil.GLPost(depth, texture, clean, bind, override);
    }

    private static void GLPre(boolean depth, boolean texture, boolean clean, boolean bind, boolean override, float lineWidth) {
        if (depth) {
            GL11.glDisable((int)2896);
        }
        if (!texture) {
            GL11.glEnable((int)3042);
        }
        GL11.glLineWidth((float)lineWidth);
        if (clean) {
            GL11.glDisable((int)3553);
        }
        if (bind) {
            GL11.glDisable((int)2929);
        }
        if (!override) {
            GL11.glEnable((int)2848);
        }
        GlStateManager.blendFunc((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glHint((int)3154, (int)4354);
        GlStateManager.depthMask((boolean)false);
    }

    public static float[][] getBipedRotations(ModelBiped biped) {
        float[][] rotations = new float[5][];
        float[] headRotation = new float[]{biped.bipedHead.rotateAngleX, biped.bipedHead.rotateAngleY, biped.bipedHead.rotateAngleZ};
        rotations[0] = headRotation;
        float[] rightArmRotation = new float[]{biped.bipedRightArm.rotateAngleX, biped.bipedRightArm.rotateAngleY, biped.bipedRightArm.rotateAngleZ};
        rotations[1] = rightArmRotation;
        float[] leftArmRotation = new float[]{biped.bipedLeftArm.rotateAngleX, biped.bipedLeftArm.rotateAngleY, biped.bipedLeftArm.rotateAngleZ};
        rotations[2] = leftArmRotation;
        float[] rightLegRotation = new float[]{biped.bipedRightLeg.rotateAngleX, biped.bipedRightLeg.rotateAngleY, biped.bipedRightLeg.rotateAngleZ};
        rotations[3] = rightLegRotation;
        float[] leftLegRotation = new float[]{biped.bipedLeftLeg.rotateAngleX, biped.bipedLeftLeg.rotateAngleY, biped.bipedLeftLeg.rotateAngleZ};
        rotations[4] = leftLegRotation;
        return rotations;
    }

    private static void GLPost(boolean depth, boolean texture, boolean clean, boolean bind, boolean override) {
        GlStateManager.depthMask((boolean)true);
        if (!override) {
            GL11.glDisable((int)2848);
        }
        if (bind) {
            GL11.glEnable((int)2929);
        }
        if (clean) {
            GL11.glEnable((int)3553);
        }
        if (!texture) {
            GL11.glDisable((int)3042);
        }
        if (depth) {
            GL11.glEnable((int)2896);
        }
    }

    public static void drawArc(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin((int)4);
        int i = (int)((float)num_segments / (360.0f / start_angle)) + 1;
        while ((float)i <= (float)num_segments / (360.0f / end_angle)) {
            double previousangle = Math.PI * 2 * (double)(i - 1) / (double)num_segments;
            double angle = Math.PI * 2 * (double)i / (double)num_segments;
            GL11.glVertex2d((double)cx, (double)cy);
            GL11.glVertex2d((double)((double)cx + Math.cos(angle) * (double)r), (double)((double)cy + Math.sin(angle) * (double)r));
            GL11.glVertex2d((double)((double)cx + Math.cos(previousangle) * (double)r), (double)((double)cy + Math.sin(previousangle) * (double)r));
            ++i;
        }
        RenderUtil.glEnd();
    }

    public static void drawArcOutline(float cx, float cy, float r, float start_angle, float end_angle, int num_segments) {
        GL11.glBegin((int)2);
        int i = (int)((float)num_segments / (360.0f / start_angle)) + 1;
        while ((float)i <= (float)num_segments / (360.0f / end_angle)) {
            double angle = Math.PI * 2 * (double)i / (double)num_segments;
            GL11.glVertex2d((double)((double)cx + Math.cos(angle) * (double)r), (double)((double)cy + Math.sin(angle) * (double)r));
            ++i;
        }
        RenderUtil.glEnd();
    }

    public static void drawCircleOutline(float x, float y, float radius) {
        RenderUtil.drawCircleOutline(x, y, radius, 0, 360, 40);
    }

    public static void drawCircleOutline(float x, float y, float radius, int start, int end, int segments) {
        RenderUtil.drawArcOutline(x, y, radius, start, end, segments);
    }

    public static void drawCircle(float x, float y, float radius) {
        RenderUtil.drawCircle(x, y, radius, 0, 360, 64);
    }

    public static void drawCircle(float x, float y, float radius, int start, int end, int segments) {
        RenderUtil.drawArc(x, y, radius, start, end, segments);
    }

    public static void drawOutlinedRoundedRectangle(int x, int y, int width, int height, float radius, float dR, float dG, float dB, float dA, float outlineWidth) {
        RenderUtil.drawRoundedRectangle(x, y, width, height, radius);
        GL11.glColor4f((float)dR, (float)dG, (float)dB, (float)dA);
        RenderUtil.drawRoundedRectangle((float)x + outlineWidth, (float)y + outlineWidth, (float)width - outlineWidth * 2.0f, (float)height - outlineWidth * 2.0f, radius);
    }

    public static void drawRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glVertex2d((double)width, (double)height);
        RenderUtil.glEnd();
    }

    public static void drawRectangleXY(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)2);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static void drawFilledRectangle(float x, float y, float width, float height) {
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)(x + width), (double)y);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)x, (double)(y + height));
        GL11.glVertex2d((double)(x + width), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static Vec3d to2D(double x, double y, double z) {
        GL11.glGetFloat((int)2982, (FloatBuffer)modelView);
        GL11.glGetFloat((int)2983, (FloatBuffer)projection);
        GL11.glGetInteger((int)2978, (IntBuffer)viewport);
        boolean result = GLU.gluProject((float)((float)x), (float)((float)y), (float)((float)z), (FloatBuffer)modelView, (FloatBuffer)projection, (IntBuffer)viewport, (FloatBuffer)screenCoords);
        if (result) {
            return new Vec3d((double)screenCoords.get(0), (double)((float)Display.getHeight() - screenCoords.get(1)), (double)screenCoords.get(2));
        }
        return null;
    }

    public static void drawTracerPointer(float x, float y, float size, float widthDiv, float heightDiv, boolean outline, float outlineWidth, int color) {
        boolean blend = GL11.glIsEnabled((int)3042);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        RenderUtil.hexColor(color);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x, (double)y);
        GL11.glVertex2d((double)(x - size / widthDiv), (double)(y + size));
        GL11.glVertex2d((double)x, (double)(y + size / heightDiv));
        GL11.glVertex2d((double)(x + size / widthDiv), (double)(y + size));
        GL11.glVertex2d((double)x, (double)y);
        GL11.glEnd();
        if (outline) {
            GL11.glLineWidth((float)outlineWidth);
            GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)alpha);
            GL11.glBegin((int)2);
            GL11.glVertex2d((double)x, (double)y);
            GL11.glVertex2d((double)(x - size / widthDiv), (double)(y + size));
            GL11.glVertex2d((double)x, (double)(y + size / heightDiv));
            GL11.glVertex2d((double)(x + size / widthDiv), (double)(y + size));
            GL11.glVertex2d((double)x, (double)y);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2848);
    }

    public static int getRainbow(int speed, int offset, float s, float b) {
        float hue = (System.currentTimeMillis() + (long)offset) % (long)speed;
        return Color.getHSBColor(hue /= (float)speed, s, b).getRGB();
    }

    public static void hexColor(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static boolean isInViewFrustrum(Entity entity) {
        return RenderUtil.isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }

    public static void drawRoundedRectangle(float x, float y, float width, float height, float radius) {
        GL11.glEnable((int)3042);
        RenderUtil.drawArc(x + width - radius, y + height - radius, radius, 0.0f, 90.0f, 16);
        RenderUtil.drawArc(x + radius, y + height - radius, radius, 90.0f, 180.0f, 16);
        RenderUtil.drawArc(x + radius, y + radius, radius, 180.0f, 270.0f, 16);
        RenderUtil.drawArc(x + width - radius, y + radius, radius, 270.0f, 360.0f, 16);
        GL11.glBegin((int)4);
        GL11.glVertex2d((double)(x + width - radius), (double)y);
        GL11.glVertex2d((double)(x + radius), (double)y);
        GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + radius), (double)y);
        GL11.glVertex2d((double)(x + radius), (double)(y + radius));
        GL11.glVertex2d((double)(x + width), (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width), (double)(y + radius));
        GL11.glVertex2d((double)x, (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
        GL11.glVertex2d((double)(x + width - radius), (double)(y + height));
        GL11.glVertex2d((double)(x + radius), (double)(y + height - radius));
        GL11.glVertex2d((double)(x + radius), (double)(y + height));
        RenderUtil.glEnd();
    }

    public static void renderOne(float lineWidth) {
        RenderUtil.checkSetupFBO();
        GL11.glPushAttrib((int)1048575);
        GL11.glDisable((int)3008);
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glLineWidth((float)lineWidth);
        GL11.glEnable((int)2848);
        GL11.glEnable((int)2960);
        GL11.glClear((int)1024);
        GL11.glClearStencil((int)15);
        GL11.glStencilFunc((int)512, (int)1, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public static void renderTwo() {
        GL11.glStencilFunc((int)512, (int)0, (int)15);
        GL11.glStencilOp((int)7681, (int)7681, (int)7681);
        GL11.glPolygonMode((int)1032, (int)6914);
    }

    public static void renderThree() {
        GL11.glStencilFunc((int)514, (int)1, (int)15);
        GL11.glStencilOp((int)7680, (int)7680, (int)7680);
        GL11.glPolygonMode((int)1032, (int)6913);
    }

    public static void renderFour(Color color) {
        RenderUtil.setColor(color);
        GL11.glDepthMask((boolean)false);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)10754);
        GL11.glPolygonOffset((float)1.0f, (float)-2000000.0f);
        OpenGlHelper.setLightmapTextureCoords((int)OpenGlHelper.lightmapTexUnit, (float)240.0f, (float)240.0f);
    }

    public static void renderFive() {
        GL11.glPolygonOffset((float)1.0f, (float)2000000.0f);
        GL11.glDisable((int)10754);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2960);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)3008);
        GL11.glPopAttrib();
    }

    public static void setColor(Color color) {
        GL11.glColor4d((double)((double)color.getRed() / 255.0), (double)((double)color.getGreen() / 255.0), (double)((double)color.getBlue() / 255.0), (double)((double)color.getAlpha() / 255.0));
    }

    public static void checkSetupFBO() {
        Framebuffer fbo = RenderUtil.mc.framebuffer;
        if (fbo != null && fbo.depthBuffer > -1) {
            RenderUtil.setupFBO(fbo);
            fbo.depthBuffer = -1;
        }
    }

    private static void setupFBO(Framebuffer fbo) {
        EXTFramebufferObject.glDeleteRenderbuffersEXT((int)fbo.depthBuffer);
        int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        EXTFramebufferObject.glBindRenderbufferEXT((int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glRenderbufferStorageEXT((int)36161, (int)34041, (int)RenderUtil.mc.displayWidth, (int)RenderUtil.mc.displayHeight);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36128, (int)36161, (int)stencilDepthBufferID);
        EXTFramebufferObject.glFramebufferRenderbufferEXT((int)36160, (int)36096, (int)36161, (int)stencilDepthBufferID);
    }

    public static class RenderTesselator
    extends Tessellator {
        public static RenderTesselator INSTANCE = new RenderTesselator();

        public RenderTesselator() {
            super(0x200000);
        }

        public static void prepare(int mode) {
            RenderTesselator.prepareGL();
            RenderTesselator.begin(mode);
        }

        public static void prepareGL() {
            GL11.glBlendFunc((int)770, (int)771);
            GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
            GlStateManager.glLineWidth((float)1.5f);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
        }

        public static void begin(int mode) {
            INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
        }

        public static void release() {
            RenderTesselator.render();
            RenderTesselator.releaseGL();
        }

        public static void render() {
            INSTANCE.draw();
        }

        public static void releaseGL() {
            GlStateManager.enableCull();
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
        }

        public static void drawBox(BlockPos blockPos, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawBox(blockPos, r, g, b, a, sides);
        }

        public static void drawBox(float x, float y, float z, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawBox(INSTANCE.getBuffer(), x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
        }

        public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
            RenderTesselator.drawBox(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
        }

        public static BufferBuilder getBufferBuilder() {
            return INSTANCE.getBuffer();
        }

        public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
            if ((sides & 1) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 2) != 0) {
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 4) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 8) != 0) {
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x10) != 0) {
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x20) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
        }

        public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
            if ((sides & 0x11) != 0) {
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x12) != 0) {
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x21) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x22) != 0) {
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 5) != 0) {
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 6) != 0) {
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 9) != 0) {
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0xA) != 0) {
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x14) != 0) {
                buffer.pos((double)x, (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x24) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)z).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)z).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x18) != 0) {
                buffer.pos((double)x, (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)x, (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
            if ((sides & 0x28) != 0) {
                buffer.pos((double)(x + w), (double)y, (double)(z + d)).color(r, g, b, a).endVertex();
                buffer.pos((double)(x + w), (double)(y + h), (double)(z + d)).color(r, g, b, a).endVertex();
            }
        }

        public static void drawBoundingBox(AxisAlignedBB bb, float width, float red, float green, float blue, float alpha) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)0, (int)1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask((boolean)false);
            GL11.glEnable((int)2848);
            GL11.glHint((int)3154, (int)4354);
            GL11.glLineWidth((float)width);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
            tessellator.draw();
            GL11.glDisable((int)2848);
            GlStateManager.depthMask((boolean)true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int argb, int alpha2) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawFullBox(bb, blockPos, width, r, g, b, a, alpha2);
        }

        public static void drawFullBox(AxisAlignedBB bb, BlockPos blockPos, float width, int red, int green, int blue, int alpha, int alpha2) {
            RenderTesselator.prepare(7);
            RenderTesselator.drawBox(blockPos, red, green, blue, alpha, 63);
            RenderTesselator.release();
            RenderTesselator.drawBoundingBox(bb, width, red, green, blue, alpha2);
        }

        public static void drawHalfBox(BlockPos blockPos, int argb, int sides) {
            int a = argb >>> 24 & 0xFF;
            int r = argb >>> 16 & 0xFF;
            int g = argb >>> 8 & 0xFF;
            int b = argb & 0xFF;
            RenderTesselator.drawHalfBox(blockPos, r, g, b, a, sides);
        }

        public static void drawHalfBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
            RenderTesselator.drawBox(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0f, 0.5f, 1.0f, r, g, b, a, sides);
        }
    }

    public static final class GeometryMasks {
        public static final HashMap<EnumFacing, Integer> FACEMAP = new HashMap();

        static {
            FACEMAP.put(EnumFacing.DOWN, 1);
            FACEMAP.put(EnumFacing.WEST, 16);
            FACEMAP.put(EnumFacing.NORTH, 4);
            FACEMAP.put(EnumFacing.SOUTH, 8);
            FACEMAP.put(EnumFacing.EAST, 32);
            FACEMAP.put(EnumFacing.UP, 2);
        }

        public static final class Line {
            public static final int DOWN_WEST = 17;
            public static final int UP_WEST = 18;
            public static final int DOWN_EAST = 33;
            public static final int UP_EAST = 34;
            public static final int DOWN_NORTH = 5;
            public static final int UP_NORTH = 6;
            public static final int DOWN_SOUTH = 9;
            public static final int UP_SOUTH = 10;
            public static final int NORTH_WEST = 20;
            public static final int NORTH_EAST = 36;
            public static final int SOUTH_WEST = 24;
            public static final int SOUTH_EAST = 40;
            public static final int ALL = 63;
        }

        public static final class Quad {
            public static final int DOWN = 1;
            public static final int UP = 2;
            public static final int NORTH = 4;
            public static final int SOUTH = 8;
            public static final int WEST = 16;
            public static final int EAST = 32;
            public static final int ALL = 63;
        }
    }
}

