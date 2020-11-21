package com.hrznstudio.matteroverdrive.client.gui;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.gui.IHudElement;
import com.hrznstudio.matteroverdrive.api.android.stat.IAndroid;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.capabilities.android.AndroidData;
import com.hrznstudio.matteroverdrive.client.animation.AnimationConsole;
import com.hrznstudio.matteroverdrive.client.animation.segment.AnimationSegmentText;
import com.hrznstudio.matteroverdrive.client.gui.element.StatsHudElement;
import com.hrznstudio.matteroverdrive.reference.ReferenceClient;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

@OnlyIn(Dist.CLIENT)
public class AndroidHudScreen{

    public static final ResourceLocation glitch_tex = new ResourceLocation(ReferenceClient.PATH_GUI + "glitch.png");
    public static final ResourceLocation spinner_1_tex = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "spinner_1.png");
    public static final ResourceLocation spinner_2_tex = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "spinner_2.png");
    public static final ResourceLocation spinner_3_tex = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "spinner_3.png");
    public static final ResourceLocation CROSS_HAIR_RL = new ResourceLocation(ReferenceClient.PATH_ELEMENTS + "cross_hair.png");

    public Color baseGuiColor;
    public float opacity;
    public float opacityBackground;
    private float hudRotationYawSmooth;
    private float hudRotationPitchSmooth;
    private AnimationConsole animationConsole;
    private boolean shaderEnabled;
    private List<IHudElement> elementList;

    public AndroidHudScreen() {
        MinecraftForge.EVENT_BUS.addListener(this::renderGameOverlay);
        this.opacity = 0.5f;
        this.baseGuiColor = ReferenceClient.Colors.HOLO;
        this.opacityBackground = 0f;
        this.shaderEnabled = false;
        this.elementList = new ArrayList<>();
        this.elementList.add(new StatsHudElement());
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent.Pre event){
        if (Minecraft.getInstance().player.isSpectator()) return;

        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroidData -> {
            onOverlay(event, iAndroidData);
        });
    }

    private void onOverlay(RenderGameOverlayEvent event, IAndroid data){
        setupAnimationConsole();
        if (data.isTurning()){
            if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) event.setCanceled(true);
            if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) event.setCanceled(true);
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR ||
                    event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE ||
                    event.getType() == RenderGameOverlayEvent.ElementType.HEALTH ||
                    event.getType() == RenderGameOverlayEvent.ElementType.FOOD) event.setCanceled(true);
            if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
                onTransforming(event, data);
            }
            if (!shaderEnabled){
                shaderEnabled = true;
                Minecraft.getInstance().runImmediately(() -> Minecraft.getInstance().gameRenderer.loadShader(new ResourceLocation(MatterOverdrive.MOD_ID, "shaders/post/transform.json")));
            }
        }
        if (data.isAndroid()){
            if (shaderEnabled){
                shaderEnabled = false;
                Minecraft.getInstance().runImmediately(() -> Minecraft.getInstance().gameRenderer.stopUseShader());
            }
            onAndroid(event, data);
        }
    }

    private void onAndroid(RenderGameOverlayEvent event, IAndroid data){
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) event.setCanceled(true);
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) event.setCanceled(true);
        if (event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE ||
                event.getType() == RenderGameOverlayEvent.ElementType.HEALTH ||
                event.getType() == RenderGameOverlayEvent.ElementType.FOOD) event.setCanceled(true);
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT){
            MatrixStack stack = event.getMatrixStack();
            int centerX = event.getWindow().getScaledWidth() / 2;
            int centerY = event.getWindow().getScaledHeight() / 2;
            //CORSSHAIR
            Minecraft.getInstance().getTextureManager().bindTexture(CROSS_HAIR_RL);
            stack.push();
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.blendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR);
            RenderSystem.color3f(ReferenceClient.Colors.HOLO.getRed() / 255f, ReferenceClient.Colors.HOLO.getGreen() / 255f, ReferenceClient.Colors.HOLO.getBlue() / 255f);
            Screen.blit(stack, centerX - 9, centerY - 8, 0, 0, 16, 16, 16, 16);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableAlphaTest();
            stack.pop();
            //ELEMENTS
            stack.push();
            if (!Minecraft.getInstance().player.isSleeping()) {
                hudRotationYawSmooth = Minecraft.getInstance().player.prevRenderArmYaw + (Minecraft.getInstance().player.renderArmYaw - Minecraft.getInstance().player.prevRenderArmYaw) * event.getPartialTicks();
                hudRotationPitchSmooth = Minecraft.getInstance().player.prevRenderArmPitch + (Minecraft.getInstance().player.renderArmPitch - Minecraft.getInstance().player.prevRenderArmPitch) * event.getPartialTicks();
                stack.translate((hudRotationYawSmooth - Minecraft.getInstance().player.rotationYaw) * 0.2f, (hudRotationPitchSmooth - Minecraft.getInstance().player.rotationPitch) * 0.2f, 0);
            }
            for (IHudElement iHudElement : elementList) {
                if (iHudElement.isVisible(data)){
                    stack.push();
                    int elementWidth = (int) (iHudElement.getWidth(event.getWindow(), data) * iHudElement.getPosition().getX());
                    stack.translate(iHudElement.getPosition().getX() * event.getWindow().getScaledWidth() - elementWidth, iHudElement.getPosition().getY() * event.getWindow().getScaledHeight() - iHudElement.getHeight(event.getWindow(), data) * iHudElement.getPosition().getY(), 0);
                    iHudElement.setBaseColor(baseGuiColor);
                    iHudElement.setBackgroundAlpha(opacityBackground);
                    iHudElement.drawElement(stack, data, event.getWindow(), event.getPartialTicks());
                    stack.pop();
                }
            }
            stack.pop();
        }
    }

    private void onTransforming(RenderGameOverlayEvent event, IAndroid data){
        MatrixStack stack = event.getMatrixStack();
        int centerX = event.getWindow().getScaledWidth() / 2;
        int centerY = event.getWindow().getScaledHeight() / 2;
        int maxTime = AndroidData.TURNING_TIME;
        int time = maxTime - data.getTurningTime();
        animationConsole.setTime(time);

        String infos = animationConsole.getString();
        String[] split = infos.split("\n");
        stack.push();
        stack.translate(10, 40, 0);
        stack.scale(0.5f, 0.5f, 0.5f);
        for (int i = 0; i < split.length; i++) {
            String info = split[i];
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(stack, info, 0, i * 9, ReferenceClient.Colors.HOLO.getRGB());
        }
        stack.pop();
        int spinnerSize = 40;

        RenderSystem.blendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        if (data.getTurningTime() % 40 > 0 && data.getTurningTime() % 40 < 3) renderGlitch(event);
        RenderSystem.color3f(1, 1, 1);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_1_tex, 4, false);
        RenderSystem.color3f(0.8f, 0.8f, 0.8f);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_2_tex, 5, true);
        RenderSystem.color3f(0.7f, 0.7f, 0.7f);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_3_tex, 7, false);
    }

    public void renderSpinner(MatrixStack stack, int centerX, int centerY, int spinnerSize, ResourceLocation location, int mult, boolean invert) {
        Minecraft.getInstance().getTextureManager().bindTexture(location);
        stack.push();
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        stack.translate(centerX, centerY, 0);
        stack.rotate(Vector3f.ZP.rotation(Minecraft.getInstance().world.getGameTime()/ 90f * mult * (invert ? 1 : -1)));
        stack.translate(-spinnerSize / 2, -spinnerSize / 2, 0);
        Screen.blit(stack, 0, 0, 0, 0, spinnerSize, spinnerSize, spinnerSize, spinnerSize);
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
        stack.pop();
    }

    public void renderGlitch(RenderGameOverlayEvent event) {
        event.getMatrixStack().push();
        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.color3f(1, 1, 1);
        Minecraft.getInstance().getTextureManager().bindTexture(glitch_tex);
        Screen.blit(event.getMatrixStack(),0, 0,  0,0, 1280, 720, event.getWindow().getScaledWidth(), event.getWindow().getScaledHeight());
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
        event.getMatrixStack().pop();
    }

    private void setupAnimationConsole(){
        if (animationConsole == null){
            animationConsole = new AnimationConsole(false, AndroidData.TURNING_TIME);
            String info;
            for (int i = 0; i < 8; i++) {
                info = new TranslationTextComponent("gui.android_hud.transforming.line." + i).getString();
                animationConsole.addSegmentSequential(AnimationSegmentText.getSegmentText(info, 0, 1).setLengthPerCharacter(1));
            }
            info = new TranslationTextComponent("gui.android_hud.transforming.line.final").getString();
            animationConsole.setFinalSegment(new AnimationSegmentText(info, 0, 1).setLengthPerCharacter(1));
        }
    }

}
