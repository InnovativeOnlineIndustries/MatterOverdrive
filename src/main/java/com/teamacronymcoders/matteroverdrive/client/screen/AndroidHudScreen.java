package com.teamacronymcoders.matteroverdrive.client.screen;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.android.perks.PerkTree;
import com.teamacronymcoders.matteroverdrive.api.android.IAndroid;
import com.teamacronymcoders.matteroverdrive.api.android.gui.IHudElement;
import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.capabilities.android.AndroidData;
import com.teamacronymcoders.matteroverdrive.client.animation.AnimationConsole;
import com.teamacronymcoders.matteroverdrive.client.animation.segment.AnimationSegmentText;
import com.teamacronymcoders.matteroverdrive.client.screen.elements.PerksHudElement;
import com.teamacronymcoders.matteroverdrive.client.screen.elements.StatsHudElement;
import com.teamacronymcoders.matteroverdrive.reference.ReferenceClient;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.teamacronymcoders.matteroverdrive.util.MORenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        this.opacityBackground = Minecraft.getInstance().options.textBackgroundOpacity().get().floatValue();
        this.shaderEnabled = false;
        this.elementList = new ArrayList<>();
        this.elementList.add(new StatsHudElement());
        this.elementList.add(new PerksHudElement());
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGuiOverlayEvent.Pre event){
        if (Minecraft.getInstance().player.isSpectator()) return;

        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroidData -> {
            onOverlay(event, iAndroidData);
        });
    }

    private void onOverlay(RenderGuiOverlayEvent.Pre event, IAndroid data){
        setupAnimationConsole();
        if (data.isTurning()){
            if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) event.setCanceled(true);
            if (event.getOverlay() == VanillaGuiOverlay.POTION_ICONS.type()) event.setCanceled(true);
            if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type() ||
                    event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type() ||
                    event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() ||
                    event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) event.setCanceled(true);
            if (event.getOverlay() == VanillaGuiOverlay.TITLE_TEXT.type()){
                onTransforming(event, data);
            }
            if (!shaderEnabled){
                shaderEnabled = true;
                Minecraft.getInstance().doRunTask(() -> Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation(MatterOverdrive.MOD_ID, "shaders/post/transform.json")));
            }
        }
        if (data.isAndroid()){
            if (shaderEnabled){
                shaderEnabled = false;
                Minecraft.getInstance().doRunTask(() -> Minecraft.getInstance().gameRenderer.shutdownEffect());
            }
            onAndroid(event, data);
        }
    }

    private void onAndroid(RenderGuiOverlayEvent.Pre event, IAndroid data) {
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) event.setCanceled(true);
        if (event.getOverlay() == VanillaGuiOverlay.POTION_ICONS.type()) event.setCanceled(true);
        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) event.setCanceled(true);
        if (event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type() && data.getPerkManager().hasPerk(PerkTree.ZERO_CALORIES)) {
            event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.AIR_LEVEL.type() && data.getPerkManager().hasPerk(PerkTree.RESPIROCYTES)) {
            event.setCanceled(true);
        }
        if (event.getOverlay() == VanillaGuiOverlay.TITLE_TEXT.type()) {
            PoseStack stack = event.getPoseStack();
            int centerX = event.getWindow().getGuiScaledWidth() / 2;
            int centerY = event.getWindow().getGuiScaledHeight() / 2;
            //CROSSHAIR
            RenderSystem.setShaderTexture(0, CROSS_HAIR_RL);
            stack.pushPose();
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL_ONE_MINUS_DST_COLOR, GL_ONE_MINUS_SRC_COLOR);
            RenderSystem.setShaderColor(ReferenceClient.Colors.HOLO.getRed() / 255f, ReferenceClient.Colors.HOLO.getGreen() / 255f, ReferenceClient.Colors.HOLO.getBlue() / 255f, ReferenceClient.Colors.HOLO.getAlpha());
            Screen.blit(stack, centerX - 9, centerY - 8, 0, 0, 16, 16, 16, 16);
            RenderSystem.disableBlend();
            RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            stack.popPose();
            //ELEMENTS
            stack.pushPose();
            if (!Minecraft.getInstance().player.isSleeping()) {
                hudRotationYawSmooth = Minecraft.getInstance().player.yBobO + (Minecraft.getInstance().player.yBob - Minecraft.getInstance().player.yBobO) * event.getPartialTick();
                hudRotationPitchSmooth = Minecraft.getInstance().player.xBobO + (Minecraft.getInstance().player.xBob - Minecraft.getInstance().player.xBobO) * event.getPartialTick();
                stack.translate((hudRotationYawSmooth - Minecraft.getInstance().player.getYRot()) * 0.2f, (hudRotationPitchSmooth - Minecraft.getInstance().player.getXRot()) * 0.2f, 0);
            }
            for (IHudElement iHudElement : elementList) {
                if (iHudElement.isVisible(data)){
                    stack.pushPose();
                    int elementWidth = (int) (iHudElement.getWidth(event.getWindow(), data) * iHudElement.getPosition().getX());
                    stack.translate(iHudElement.getPosition().getX() * event.getWindow().getWidth() - elementWidth, iHudElement.getPosition().getY() * event.getWindow().getHeight() - iHudElement.getHeight(event.getWindow(), data) * iHudElement.getPosition().getY(), 0);
                    iHudElement.setBaseColor(baseGuiColor);
                    iHudElement.setBackgroundAlpha(opacityBackground);
                    iHudElement.drawElement(stack, data, event.getWindow(), event.getPartialTick());
                    stack.popPose();
                }
            }
            stack.popPose();
        }
    }

    private void onTransforming(RenderGuiOverlayEvent.Pre event, IAndroid data){
        PoseStack stack = event.getPoseStack();
        int centerX = event.getWindow().getGuiScaledWidth() / 2;
        int centerY = event.getWindow().getGuiScaledHeight() / 2;
        int maxTime = AndroidData.TURNING_TIME;
        int time = maxTime - data.getTurningTime();
        animationConsole.setTime(time);

        String infos = animationConsole.getString();
        String[] split = infos.split("\n");
        stack.pushPose();
        stack.translate(10, 40, 0);
        for (int i = 0; i < split.length; i++) {
            String info = split[i];
            Minecraft.getInstance().font.drawShadow(stack, info, 0, i * 9, ReferenceClient.Colors.HOLO.getRGB());
        }
        stack.popPose();
        int spinnerSize = 40;

        RenderSystem.blendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        if (data.getTurningTime() % 40 > 0 && data.getTurningTime() % 40 < 3) renderGlitch(event);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_1_tex, 4, false);
        RenderSystem.setShaderColor(0.8f, 0.8f, 0.8f, 0.8f);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_2_tex, 5, true);
        RenderSystem.setShaderColor(0.7f, 0.7f, 0.7f, 0.7f);
        renderSpinner(stack, centerX, centerY, spinnerSize, spinner_3_tex, 7, false);
    }

    public void renderSpinner(PoseStack stack, int centerX, int centerY, int spinnerSize, ResourceLocation location, int mult, boolean invert) {
        RenderSystem.setShaderTexture(0, location);
        stack.pushPose();
        RenderSystem.enableBlend();
        stack.translate(centerX, centerY, 0);
        stack.mulPose(Vector3f.ZP.rotation(Minecraft.getInstance().level.getGameTime()/ 90f * mult * (invert ? 1 : -1)));
        stack.translate(-spinnerSize / 2, -spinnerSize / 2, 0);
        Screen.blit(stack, 0, 0, 0, 0, spinnerSize, spinnerSize, spinnerSize, spinnerSize);
        RenderSystem.disableBlend();
        stack.popPose();
    }

    public void renderGlitch(RenderGuiOverlayEvent event) {
        event.getPoseStack().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, glitch_tex);
        Screen.blit(event.getPoseStack(),0, 0,  0,0, 1280, 720, event.getWindow().getWidth(), event.getWindow().getHeight());
        RenderSystem.disableBlend();
        event.getPoseStack().popPose();
    }

    private void setupAnimationConsole(){
        if (animationConsole == null){
            animationConsole = new AnimationConsole(false, AndroidData.TURNING_TIME);
            String info;
            for (int i = 0; i < 8; i++) {
                info = Component.translatable("gui.android_hud.transforming.line." + i).getString();
                animationConsole.addSegmentSequential(AnimationSegmentText.getSegmentText(info, 0, 1).setLengthPerCharacter(1));
            }
            info = Component.translatable("gui.android_hud.transforming.line.final").getString();
            animationConsole.setFinalSegment(new AnimationSegmentText(info, 0, 1).setLengthPerCharacter(1));
        }
    }

}
