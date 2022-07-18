package com.hrznstudio.matteroverdrive.client.screen;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.client.screen.elements.PerkButton;
import com.hrznstudio.matteroverdrive.menu.AndroidStationMenu;
import com.hrznstudio.matteroverdrive.util.MOColorUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

/**
 * See {@link net.minecraft.client.gui.screens.inventory.CraftingScreen} for references on how to render container + button
 */
public class AndroidStationScreen extends AbstractContainerScreen<AndroidStationMenu> implements MenuAccess<AndroidStationMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation("textures/gui/container/generic_54.png");

    private float size = 0.3f;
    private int xStart = 0;
    private int yStart = 0;
    private int cornerHeights = 42;
    private int leftSize = 56;
    private double dragX = 0;
    private double dragY = 0;
    private int pointerX = 0;
    private int pointerY = 0;

    private final int containerRows;

    public AndroidStationScreen(AndroidStationMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
        this.containerRows = 0;
        //this.containerRows = screenMenu.getRowCount();
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 15;
        titleLabelY = -35;
        xStart = (int) (this.width * (size/2f));
        yStart = (int) (this.height * (size/6f));
        IAndroidPerk.PERKS.values().forEach(perk -> {
            if (perk.getParent() == null){
                addWidget(new PerkButton(perk, xStart + leftSize + 32*perk.getAndroidStationLocation().getX(), yStart + cornerHeights + 32*perk.getAndroidStationLocation().getY(), 18, 18, Component.empty(), this::getScissors));
                addChildPerks(perk,  xStart + leftSize + 32*perk.getAndroidStationLocation().getX(), yStart + cornerHeights + 32*perk.getAndroidStationLocation().getY());
            }
        });
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        renderBackground(poseStack);
        renderDynamicBackground(poseStack);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, ChatFormatting.WHITE.getColor());
    }

    public Runnable getScissors(){
        double d0 = Minecraft.getInstance().getWindow().getGuiScale();
        int scissorStart = this.xStart + leftSize - 14;
        int scissorYStart = this.yStart + cornerHeights + 18*4;
        return  () -> RenderSystem.enableScissor((int) (scissorStart * d0), (int) (scissorYStart * d0),  (int)((this.width - this.xStart * 2  - leftSize - 2)* d0), (int) ((this.height - this.yStart -scissorYStart - cornerHeights + 10) * d0));
    }

    public void renderDynamicBackground(PoseStack poseStack){
        size = 0.3f;
        xStart = (int) (this.width * (size/2f));
        yStart = (int) (this.height * (size/6f));

        cornerHeights = 42;
        leftSize = 56;
        int rightSize = 36;
        fill(poseStack, xStart +1, yStart + cornerHeights - 16, this.width - xStart - 16, this.height - yStart - cornerHeights + 14, 0xff222825);
        RenderSystem.setShaderTexture(0, new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/base_gui.png"));
        //Top Left Corner
        blit(poseStack, xStart, yStart, 0,0, leftSize, cornerHeights);
        //Bottom left corner
        blit(poseStack,  xStart,this.height - yStart - cornerHeights, 0,155, leftSize, cornerHeights);

        //TOP RIGHT
        blit(poseStack,  this.width - xStart - rightSize, yStart, 188,0, rightSize, cornerHeights);
        //BOTTOM RIGHT
        blit(poseStack,  this.width - xStart - rightSize, this.height - yStart - cornerHeights, 188,155, rightSize, cornerHeights);

        //LEFT SIDE
        for (int yTemp = yStart + cornerHeights; yTemp < this.height - yStart - cornerHeights; ++yTemp){
            blit(poseStack,  xStart, yTemp, 0,42, 56, 1);
        }
        //RIGHT SIDE
        for (int yTemp = yStart + cornerHeights; yTemp < this.height - yStart - cornerHeights; ++yTemp){
            blit(poseStack,  this.width - xStart - rightSize, yTemp, 188,42, 46, 1);
        }
        //TOP SIDE
        for (int xTemp = leftSize + xStart; xTemp < this.width - xStart - rightSize; ++xTemp){
            blit(poseStack,  xTemp, yStart, 62,0, 1, 30);
        }
        //BOTTOM SIDE
        for (int xTemp = leftSize + xStart; xTemp < this.width - xStart - rightSize; ++xTemp){
            blit(poseStack,  xTemp, this.height - yStart - cornerHeights - 9, 55,146, 1, 30);
        }


        this.leftPos =  xStart + leftSize - 18;
        this.topPos = yStart + cornerHeights;
        this.imageWidth = this.width - xStart * 2;
        this.imageHeight = this.height - yStart * 2;
        RenderSystem.setShaderTexture(0, new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/slot_small.png"));

        for (int i = 0; i < 9; i++) {
            blit(poseStack,  xStart + leftSize + 18 * i - 11, this.height - yStart - cornerHeights - 7, 0,0, 18, 18,18,18);
            Slot slot = menu.getSlot( 3 * 9 + i);
            slot.y = this.height - yStart - cornerHeights - 7 - this.getGuiTop() + 1;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int slotX = xStart + leftSize + 18 * j - 11;
                int slotY = this.height - yStart - cornerHeights - 12 + 18 * i  - 18* 3;
                blit(poseStack, slotX , slotY, 0,0, 18, 18,18,18);
                Slot slot = menu.getSlot(j + i * 9);
                slot.y = slotY - this.getGuiTop() + 1;
            }
        }
        getScissors().run();
        RenderSystem.setShaderTexture(0, new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/grid_bg.png"));
        float alpha = 0.25f;
        RenderSystem.setShaderColor(MOColorUtil.HOLO_COLOR.getRed() * alpha/ 255f, MOColorUtil.HOLO_COLOR.getGreen()* alpha/ 255f, MOColorUtil.HOLO_COLOR.getBlue()  * alpha/ 255f, 1f);
        int oldPointerX =  this.pointerX;
        int oldPointerY = this.pointerY;
        this.pointerX = (int) Mth.clamp(this.dragX + this.pointerX, -256, 256);
        this.pointerY = (int) Mth.clamp(this.dragY + this.pointerY, -256, 256);
        for (int x = 0; x < this.width; x += 32) {
            for (int y = 0; y < this.width;  y += 32) {
                blit(poseStack,  x + this.pointerX / 4, y + this.pointerY / 4, 0,0, 32,32,32,32);
            }
        }
        for (Widget button : this.renderables) {
            if (button instanceof PerkButton perkButton){
                if (oldPointerX != pointerX) perkButton.x += dragX;
                if (oldPointerY != pointerY) perkButton.y += dragY;
            }
        }
        this.dragX = 0;
        this.dragY = 0;
        RenderSystem.disableScissor();
    }

//    @Override
//    protected void drawGuiContainerForegroundLayer(PoseStack poseStack, int x, int y) {
//        for (Widget button : this.buttons) {
//            if (button instanceof PerkButton && ((PerkButton) button).isHoveredOrFocused()){
//                this.renderComponentTooltip(poseStack, ((PerkButton) button).getTooltipLines(), x - this.guiLeft, y - this.guiTop);
//            }
//        }
//    }

    private void addChildPerks(IAndroidPerk perk, double x, double y){
        if (perk.getChild().size() > 0){
            if (perk.getChild().size() > 1){
                for (int i = 0; i < perk.getChild().size(); i++) {
                    double perkX = x + 84;
                    double perkY = y + 30 * i;
                    addWidget(new PerkButton(perk.getChild().get(i), perkX, perkY, 18, 18, Component.literal(""), this::getScissors));
                    addChildPerks(perk.getChild().get(i), perkX, perkY);
                }
            } else {
                double perkX = x + 53;
                double perkY = y;
                addWidget(new PerkButton(perk.getChild().get(0), perkX, perkY, 18, 18, Component.literal(""), this::getScissors));
                addChildPerks(perk.getChild().get(0), perkX, perkY);
            }
        }
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        dragX = (int) Mth.clamp(dragX + p_231045_6_, -256, 256);
        dragY = (int) Mth.clamp(dragY + p_231045_8_, -256, 256);
        return super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
    }
}
