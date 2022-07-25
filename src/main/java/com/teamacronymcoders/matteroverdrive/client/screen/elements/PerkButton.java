package com.teamacronymcoders.matteroverdrive.client.screen.elements;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.api.android.perk.IAndroidPerk;
import com.teamacronymcoders.matteroverdrive.capabilities.MOCapabilities;
import com.teamacronymcoders.matteroverdrive.network.PacketHandler;
import com.teamacronymcoders.matteroverdrive.network.c2s.AndroidPerkAttemptBuyPacket;
import com.teamacronymcoders.matteroverdrive.util.MOColorUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class PerkButton extends AbstractWidget {

    public static ResourceLocation BG_BUTTON = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/slot_holo.png");
    public static ResourceLocation LINE_BUTTON_DOWN = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/connection_down.png");
    public static ResourceLocation LINE_BUTTON_RIGHT = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/connection_right.png");
    public static ResourceLocation CIRCLE = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/black_circle.png");

    private IAndroidPerk perk;
    private Supplier<Runnable> runnable;

    public PerkButton(IAndroidPerk perk, double x, double y, int width, int height, MutableComponent title, Supplier<Runnable> supplier) {
        super((int) x, (int) y, width, height, title);
        this.perk = perk;
        this.visible = true;
        this.runnable = supplier;
    }

    private boolean blink = false;
    private int count = 0;

    @Override
    public void renderButton(PoseStack stack, int mouseX, int mouseY, float partial) {
        runnable.get().run();
        AtomicReference<Color> color = new AtomicReference<>(MOColorUtil.HOLO_COLOR.darker().darker());

        // Grabs the color for the button
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

        // Blinks the locked traits
        // TODO: Discuss with Sekwah on how to make this fade the colors between darker and brighter to indicate it being unlockable.
//        Color unaltered = color.get();
//        if (color.get().equals(MOColorUtil.HOLO_COLOR.darker().darker())) {
//            count += 1;
//            if (count == 20) {
//                if (!blink) {
//                    color.set(color.get().darker());
//                    blink = true;
//                } else {
//                    blink = false;
//                }
//            }
//            if (count == 100) {
//                count = 0;
//            }
//        }

        // Sets the Color and Texture of the Button
        RenderSystem.setShaderColor(color.get().getRed() / 255f, color.get().getGreen()/ 255f, color.get().getBlue()  / 255f, 1.0f);
        RenderSystem.setShaderTexture(0, BG_BUTTON);
        // Draws the Button Background
        blit(stack, x,y, 0,0, 18,18, 18, 18 );
        // Sets and Draws the Button Icon
        RenderSystem.setShaderTexture(0, perk.getIcon());
        blit(stack, x,y, 0,0, 18,18, 18, 18 );

        // Draws connections
        //RenderSystem.setShaderColor(unaltered.getRed() / 255f, unaltered.getGreen()/ 255f, unaltered.getBlue()  / 255f, 1.0f);
        if (perk.getChild().size() > 0){
            RenderSystem.setShaderTexture(0, LINE_BUTTON_RIGHT);
            blit(stack, x + 22,y + 6, 0,0, 30, 7, 30, 7);
            if (perk.getChild().size() > 1){
                for (int i = 0; i < perk.getChild().size(); i++) {
                    if (i != 0){
                        RenderSystem.setShaderTexture(0, LINE_BUTTON_DOWN);
                        blit(stack, x + 22 + 29,y + 8 + 30 * (i -1), 0,0, 7, 30, 7, 30);
                    }
                    RenderSystem.setShaderTexture(0, LINE_BUTTON_RIGHT);
                    blit(stack, x + 22 + 31,y + 6 + 30 * i, 0,0, 30, 7, 30, 7);
                }
            }
        }
        // If the perk is an upgradable perk, then render a dark circle with the number in the bottom-right corner
        if (perk.getMaxLevel() > 1){
            Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                if (iAndroid.getPerkManager().hasPerk(perk)){
                    RenderSystem.setShaderTexture(0, CIRCLE);
                    float scale = 0.5f;
                    stack.scale(scale, scale, scale);
                    blit(stack, (int)((x + 12)/scale),(int)((y +12)/scale), 0,0, 18,18, 18, 18 );
                    stack.scale(1/scale,1/scale,1/scale);
                    Minecraft.getInstance().font.draw(stack, iAndroid.getPerkManager().getLevel(perk) +"", x + 14,y +13, color.get().getRGB());
                }
            });
        }
        RenderSystem.disableScissor();
    }

    public List<Component> getTooltipLines(){
        List<Component> list = new ArrayList<>();
        Minecraft.getInstance().player.getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
            list.add(Component.literal(perk.getDisplayName(iAndroid, 0).getString()).withStyle(perk.getParent() == null || iAndroid.getPerkManager().hasPerk(perk.getParent()) ? ChatFormatting.AQUA : ChatFormatting.RED));
            list.add(Component.translatable("matteroverdrive.perk." + perk.getName() + ".desc" + (perk.getMaxLevel() > 1 ? "." + iAndroid.getPerkManager().getLevel(perk) : "")).withStyle(ChatFormatting.GRAY));
            if (perk.getMaxLevel() > 1){
                list.add(
                        Component.translatable("matteroverdrive.perk.level")
                                .withStyle(ChatFormatting.DARK_AQUA)
                                .append(Component.literal(String.valueOf(iAndroid.getPerkManager().getLevel(perk)))
                                        .append(Component.literal("/").withStyle(ChatFormatting.GOLD))
                                        .append(Component.literal(String.valueOf(perk.getMaxLevel())).withStyle(ChatFormatting.RESET))
                                )
                );
            }
            if (perk.getParent() != null) list.add(Component.translatable("gui.android_station.parent").withStyle(ChatFormatting.GOLD).append(Component.literal(perk.getParent().getDisplayName(iAndroid, 0).getString()).withStyle(ChatFormatting.WHITE)));
            if ((!iAndroid.getPerkManager().hasPerk(perk) || iAndroid.getPerkManager().getLevel(perk) < perk.getMaxLevel()) && perk.getRequiredXP(iAndroid, 0) > 0){
                list.add(Component.literal("XP: " + perk.getRequiredXP(iAndroid, 0)).withStyle(Minecraft.getInstance().player.experienceLevel >= perk.getRequiredXP(iAndroid, iAndroid.getPerkManager().getLevel(perk) + 1) ? ChatFormatting.GREEN : ChatFormatting.RED));
            }
        });

        return list;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        PacketHandler.MO_CHANNEL.sendToServer(new AndroidPerkAttemptBuyPacket(perk.getName()));
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {}
}
