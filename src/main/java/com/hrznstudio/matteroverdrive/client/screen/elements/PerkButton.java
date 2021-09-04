package com.hrznstudio.matteroverdrive.client.screen.elements;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.network.client.AndroidPerkAttemptBuyPacket;
import com.hrznstudio.matteroverdrive.util.MOColorUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class PerkButton extends Widget {

    public static ResourceLocation BG_BUTTON = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/slot_holo.png");
    public static ResourceLocation LINE_BUTTON_DOWN = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/connection_down.png");
    public static ResourceLocation LINE_BUTTON_RIGHT = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/connection_right.png");
    public static ResourceLocation CIRCLE = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/black_circle.png");

    private IAndroidPerk perk;
    private Supplier<Runnable> runnable;

    public PerkButton(IAndroidPerk perk, double x, double y, int width, int height, ITextComponent title, Supplier<Runnable> supplier) {
        super((int) x, (int) y, width, height, title);
        this.perk = perk;
        this.visible = true;
        this.runnable = supplier;
    }

    @Override
    public void renderButton(MatrixStack stack, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        runnable.get().run();
        AtomicReference<Color> color = new AtomicReference<>(MOColorUtil.HOLO_COLOR.darker());
        if (perk.getParent() != null){
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                if (!iAndroid.getPerkManager().hasPerk(perk.getParent())){
                    color.set(MOColorUtil.INVALID_HOLO_COLOR);
                }
            });
        }
        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
            if (iAndroid.getPerkManager().hasPerk(perk)){
                color.set(MOColorUtil.HOLO_COLOR);
            }
        });
        RenderSystem.color4f(color.get().getRed() / 255f, color.get().getGreen()/ 255f, color.get().getBlue()  / 255f, 1.0f);
        Minecraft.getInstance().getTextureManager().bindTexture(BG_BUTTON);
        blit(stack, x,y, 0,0, 18,18, 18, 18 );
        Minecraft.getInstance().getTextureManager().bindTexture(perk.getIcon());
        blit(stack, x,y, 0,0, 18,18, 18, 18 );
        if (perk.getChild().size() > 0){
            Minecraft.getInstance().getTextureManager().bindTexture(LINE_BUTTON_RIGHT);
            blit(stack, x + 22,y + 6, 0,0, 30, 7, 30, 7);
            if (perk.getChild().size() > 1){
                for (int i = 0; i < perk.getChild().size(); i++) {
                    if (i != 0){
                        Minecraft.getInstance().getTextureManager().bindTexture(LINE_BUTTON_DOWN);
                        blit(stack, x + 22 + 29,y + 8 + 30 * (i -1), 0,0, 7, 30, 7, 30);
                    }
                    Minecraft.getInstance().getTextureManager().bindTexture(LINE_BUTTON_RIGHT);
                    blit(stack, x + 22 + 31,y + 6 + 30 * i, 0,0, 30, 7, 30, 7);
                }
            }
        }
        if (perk.getMaxLevel() > 0){
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                if (iAndroid.getPerkManager().hasPerk(perk)){
                    Minecraft.getInstance().getTextureManager().bindTexture(CIRCLE);
                    float scale = 0.5f;
                    stack.scale(scale, scale, scale);
                    blit(stack, (int)((x + 12)/scale),(int)((y +12)/scale), 0,0, 18,18, 18, 18 );
                    stack.scale(1/scale,1/scale,1/scale);
                    Minecraft.getInstance().fontRenderer.drawString(stack, iAndroid.getPerkManager().getLevel(perk) +"", x + 14,y +13, color.get().getRGB());
                }
            });
        }
        RenderSystem.disableScissor();
    }

    public List<ITextComponent> getTooltipLines(){
        List<ITextComponent> list = new ArrayList<>();
        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
            list.add(new StringTextComponent(perk.getDisplayName(iAndroid, 0).getString()).mergeStyle(perk.getParent() == null || iAndroid.getPerkManager().hasPerk(perk.getParent()) ? TextFormatting.AQUA : TextFormatting.RED));
            list.add(new TranslationTextComponent("matteroverdrive.perk." + perk.getName() + ".desc" + (perk.getMaxLevel() > 1 ? "." + iAndroid.getPerkManager().getLevel(perk) : "")).mergeStyle(TextFormatting.GRAY));
            if (perk.getMaxLevel() > 1){
                list.add(new TranslationTextComponent("matteroverdrive.perk.level").mergeStyle(TextFormatting.DARK_AQUA).append(new StringTextComponent(iAndroid.getPerkManager().getLevel(perk) + "§6/§r" + perk.getMaxLevel()).mergeStyle(TextFormatting.WHITE)));
            }
            if (perk.getParent() != null) list.add(new TranslationTextComponent("gui.android_station.parent").mergeStyle(TextFormatting.GOLD).append(new StringTextComponent(perk.getParent().getDisplayName(iAndroid, 0).getString()).mergeStyle(TextFormatting.WHITE)));
            if ((!iAndroid.getPerkManager().hasPerk(perk) || iAndroid.getPerkManager().getLevel(perk) < perk.getMaxLevel()) && perk.getRequiredXP(iAndroid, 0) > 0){
                list.add(new StringTextComponent("XP: " + perk.getRequiredXP(iAndroid, 0)).mergeStyle(Minecraft.getInstance().player.experienceLevel >= perk.getRequiredXP(iAndroid, iAndroid.getPerkManager().getLevel(perk) + 1) ? TextFormatting.GREEN : TextFormatting.RED));
            }
        });

        return list;
    }

    @Override
    public void onClick(double p_230982_1_, double p_230982_3_) {
        PacketHandler.NETWORK.get().sendToServer(new AndroidPerkAttemptBuyPacket(perk.getName()));
    }
}
