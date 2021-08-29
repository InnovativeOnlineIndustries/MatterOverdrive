package com.hrznstudio.matteroverdrive.client.screen;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.api.android.perk.IAndroidPerk;
import com.hrznstudio.matteroverdrive.client.screen.elements.PerkButton;
import com.hrznstudio.matteroverdrive.container.AndroidStationContainer;
import com.hrznstudio.matteroverdrive.util.MOColorUtil;
import com.hrznstudio.titanium.container.BasicContainer;
import com.hrznstudio.titanium.container.impl.BasicInventoryContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class AndroidStationScreen extends ContainerScreen<AndroidStationContainer> {

    private float size = 0.3f;
    private int xStart = 0;
    private int yStart = 0;
    private int cornerHeights = 42;
    private int leftSize = 56;
    private double dragX = 0;
    private double dragY = 0;
    private int pointerX = 0;
    private int pointerY = 0;

    public AndroidStationScreen(AndroidStationContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();
        xStart = (int) (this.width * (size/2f));
        yStart = (int) (this.height * (size/6f));
        IAndroidPerk.PERKS.values().forEach(perk -> {
            if (perk.getParent() == null){
                addButton(new PerkButton(perk, xStart + leftSize + 32*perk.getAndroidStationLocation().getX(), yStart + cornerHeights + 32*perk.getAndroidStationLocation().getY(), 18, 18, new StringTextComponent(""), this::getScissors));
                addChildPerks(perk,  xStart + leftSize + 32*perk.getAndroidStationLocation().getX(), yStart + cornerHeights + 32*perk.getAndroidStationLocation().getY());
            }
        });
    }




    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        renderBackground(matrixStack);
        renderDynamicBackGround(matrixStack);
    }

    public Runnable getScissors(){
        double d0 = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        int scissorStart = this.xStart + leftSize - 14;
        int scissorYStart = this.yStart + cornerHeights + 18*4;
        return  () -> RenderSystem.enableScissor((int) (scissorStart * d0), (int) (scissorYStart * d0),  (int)((this.width - this.xStart * 2  - leftSize - 2)* d0), (int) ((this.height - this.yStart -scissorYStart - cornerHeights + 10) * d0));
    }

    public void renderDynamicBackGround(MatrixStack matrixStack){
        size = 0.3f;
        xStart = (int) (this.width * (size/2f));
        yStart = (int) (this.height * (size/6f));

        cornerHeights = 42;
        leftSize = 56;
        int rightSize = 36;
        fill(matrixStack, xStart +1, yStart + cornerHeights - 16, this.width - xStart - 16, this.height - yStart - cornerHeights + 14, 0xff222825);
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/base_gui.png"));
        //Top Left Corner
        blit(matrixStack, xStart, yStart, 0,0, leftSize, cornerHeights);
        //Bottom left corner
        blit(matrixStack,  xStart,this.height - yStart - cornerHeights, 0,155, leftSize, cornerHeights);

        //TOP RIGHT
        blit(matrixStack,  this.width - xStart - rightSize, yStart, 188,0, rightSize, cornerHeights);
        //BOTTOM RIGHT
        blit(matrixStack,  this.width - xStart - rightSize, this.height - yStart - cornerHeights, 188,155, rightSize, cornerHeights);

        //LEFT SIDE
        for (int yTemp = yStart + cornerHeights; yTemp < this.height - yStart - cornerHeights; ++yTemp){
            blit(matrixStack,  xStart, yTemp, 0,42, 56, 1);
        }
        //RIGHT SIDE
        for (int yTemp = yStart + cornerHeights; yTemp < this.height - yStart - cornerHeights; ++yTemp){
            blit(matrixStack,  this.width - xStart - rightSize, yTemp, 188,42, 46, 1);
        }
        //TOP SIDE
        for (int xTemp = leftSize + xStart; xTemp < this.width - xStart - rightSize; ++xTemp){
            blit(matrixStack,  xTemp, yStart, 62,0, 1, 30);
        }
        //BOTTOM SIDE
        for (int xTemp = leftSize + xStart; xTemp < this.width - xStart - rightSize; ++xTemp){
            blit(matrixStack,  xTemp, this.height - yStart - cornerHeights - 9, 55,146, 1, 30);
        }


        this.guiLeft =  xStart + leftSize - 18;
        this.guiTop = yStart + cornerHeights;
        this.xSize = this.width - xStart * 2;
        this.ySize = this.height - yStart * 2;
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/slot_small.png"));

        for (int i = 0; i < 9; i++) {
            blit(matrixStack,  xStart + leftSize + 18 * i - 11, this.height - yStart - cornerHeights - 7, 0,0, 18, 18,18,18);
            Slot slot = this.container.getSlot( 3 * 9 + i);
            slot.yPos = this.height - yStart - cornerHeights - 7 - this.guiTop + 1;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int slotX = xStart + leftSize + 18 * j - 11;
                int slotY = this.height - yStart - cornerHeights - 12 + 18 * i  - 18* 3;
                blit(matrixStack, slotX , slotY, 0,0, 18, 18,18,18);
                Slot slot = this.container.getSlot(j + i * 9);
                slot.yPos = slotY - this.guiTop + 1;
            }
        }
        getScissors().run();
        Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation(MatterOverdrive.MOD_ID, "textures/gui/elements/grid_bg.png"));
        float alpha = 0.25f;
        RenderSystem.color4f(MOColorUtil.HOLO_COLOR.getRed() * alpha/ 255f, MOColorUtil.HOLO_COLOR.getGreen()* alpha/ 255f, MOColorUtil.HOLO_COLOR.getBlue()  * alpha/ 255f, 1f);
        int oldPointerX =  this.pointerX;
        int oldPointerY = this.pointerY;
        this.pointerX = (int) MathHelper.clamp(this.dragX + this.pointerX, -256, 256);
        this.pointerY = (int) MathHelper.clamp(this.dragY + this.pointerY, -256, 256);
        for (int x = 0; x < this.width; x += 32) {
            for (int y = 0; y < this.width;  y += 32) {
                blit(matrixStack,  x + this.pointerX / 4, y + this.pointerY / 4, 0,0, 32,32,32,32);
            }
        }
        for (Widget button : this.buttons) {
            if (button instanceof PerkButton){
                if (oldPointerX != pointerX) button.x += dragX;
                if (oldPointerY != pointerY) button.y += dragY;
            }
        }
        this.dragX = 0;
        this.dragY = 0;
        RenderSystem.disableScissor();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        for (Widget button : this.buttons) {
            if (button instanceof PerkButton && button.isHovered()){
                this.func_243308_b(matrixStack, ((PerkButton) button).getTooltipLines(), x - this.guiLeft, y - this.guiTop);
            }
        }
    }

    private void addChildPerks(IAndroidPerk perk, double x, double y){
        if (perk.getChild().size() > 0){
            if (perk.getChild().size() > 1){
                for (int i = 0; i < perk.getChild().size(); i++) {
                    double perkX = x + 84;
                    double perkY = y + 30 * i;
                    addButton(new PerkButton(perk.getChild().get(i), perkX, perkY, 18, 18, new StringTextComponent(""), this::getScissors));
                    addChildPerks(perk.getChild().get(i), perkX, perkY);
                }
            } else {
                double perkX = x + 53;
                double perkY = y;
                addButton(new PerkButton(perk.getChild().get(0), perkX, perkY, 18, 18, new StringTextComponent(""), this::getScissors));
                addChildPerks(perk.getChild().get(0), perkX, perkY);
            }
        }
    }

    @Override
    public boolean mouseDragged(double p_231045_1_, double p_231045_3_, int p_231045_5_, double p_231045_6_, double p_231045_8_) {
        dragX = (int) MathHelper.clamp(dragX + p_231045_6_, -256, 256);
        dragY = (int) MathHelper.clamp(dragY + p_231045_8_, -256, 256);

        boolean dragged = super.mouseDragged(p_231045_1_, p_231045_3_, p_231045_5_, p_231045_6_, p_231045_8_);
        return dragged;
    }
}
