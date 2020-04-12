/*
 * This file is part of MatterOverdrive: Legacy Edition
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * MatterOverdrive: Legacy Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MatterOverdrive: Legacy Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.guide;

import matteroverdrive.Reference;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MOGuideEntry {
    public String name;
    private MOGuideEntry parent;
    private ItemStack[] stackIcons;
    @SideOnly(Side.CLIENT)
    private int guiPosX;
    @SideOnly(Side.CLIENT)
    private int guiPosY;
    private List<MOGuideEntry> children;
    private int id;
    private String group;
    @SideOnly(Side.CLIENT)
    private ResourceLocation styleLocation;

    public MOGuideEntry(String name, ItemStack... stackIcons) {
        this.stackIcons = stackIcons;
        this.name = name;
        init();
    }

    public MOGuideEntry(int id, String name, ItemStack... stackIcons) {
        this.stackIcons = stackIcons;
        this.name = name;
        this.id = id;
        init();
    }

    private void init() {
        children = new ArrayList<>();
    }

    public MOGuideEntry setStackIcons(ItemStack... stackIcons) {
        this.stackIcons = stackIcons;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public MOGuideEntry setStackIcons(Item item) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        item.getSubItems(CreativeTabs.SEARCH, stacks);
        if (stacks.size() > 0) {
            this.stackIcons = new ItemStack[stacks.size()];
            this.stackIcons = stacks.toArray(this.stackIcons);
        } else {
            this.stackIcons = new ItemStack[]{new ItemStack(item)};
        }
        return this;
    }

    public String getDisplayName() {
        return MOStringHelper.translateToLocal(String.format("guide.entry.%s.name", name));
    }

    public MOGuideEntry setGuiPos(int x, int y) {
        this.guiPosX = x;
        this.guiPosY = y;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public int getGuiPosX() {
        return guiPosX;
    }

    @SideOnly(Side.CLIENT)
    public int getGuiPosY() {
        return guiPosY;
    }

    public MOGuideEntry getParent() {
        return parent;
    }

    public MOGuideEntry setParent(MOGuideEntry parent) {
        this.parent = parent;
        return this;
    }

    public void addChild(MOGuideEntry child) {
        child.setParent(this);
        children.add(child);
    }

    public ItemStack[] getStackIcons() {
        return stackIcons;
    }

    @SideOnly(Side.CLIENT)
    public MOGuideEntry setStackIcons(Block block) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        block.getSubBlocks(CreativeTabs.SEARCH, stacks);
        if (stacks.size() > 0) {
            this.stackIcons = new ItemStack[stacks.size()];
            this.stackIcons = stacks.toArray(this.stackIcons);
        } else {
            this.stackIcons = new ItemStack[]{new ItemStack(block)};
        }
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SideOnly(Side.CLIENT)
    public String getDescription() {
        String info;
        StringWriter writer = new StringWriter();
        InputStream stream;

        try {

            stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(getDescriptionPath(Minecraft.getMinecraft().gameSettings.language))).getInputStream();
            info = IOUtils.toString(stream, "UTF-8");
            writer.close();
            stream.close();
        } catch (IOException e) {
            MOLog.warn("Language text %s for entry %s not found.", Minecraft.getMinecraft().gameSettings.language, name);
            try {
                stream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(getDescriptionPath("en_US"))).getInputStream();
                info = IOUtils.toString(stream, "UTF-8");
                writer.close();
                stream.close();
            } catch (IOException e1) {
                info = "There is no default text entry for " + name;
            }
        }

        return info;
    }

    @SideOnly(Side.CLIENT)
    public InputStream getDescriptionStream() {
        InputStream stream = null;
        try {
            return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(getDescriptionPath(Minecraft.getMinecraft().gameSettings.language))).getInputStream();
        } catch (IOException e) {
            MOLog.warn("Language text %s for entry %s not found.", Minecraft.getMinecraft().gameSettings.language, name);
            try {
                return Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(getDescriptionPath("en_US"))).getInputStream();
            } catch (IOException e1) {
                MOLog.warn("Default language entry for %s not found", name);
            }
        }
        return stream;
    }

    public String getDescriptionPath(String language) {
        return String.format("%s%s/%s.xml", Reference.PATH_INFO, language, name);
    }

    public String getGroup() {
        return group;
    }

    public MOGuideEntry setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getName() {
        return name;
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getStyleLocation() {
        return styleLocation;
    }

    @SideOnly(Side.CLIENT)
    public void setStyleLocation(ResourceLocation location) {
        this.styleLocation = location;
    }
}
