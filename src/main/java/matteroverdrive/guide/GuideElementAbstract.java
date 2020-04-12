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
import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class GuideElementAbstract implements IGuideElement {
    protected MOGuiBase gui;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int marginTop;
    protected int marginBottom;
    protected int marginLeft;
    protected int marginRight;
    protected int textAlign;
    protected int floating;
    protected Color color;

    @Override
    public void loadElement(MOGuideEntry entry, Element element, Map<String, String> styleSheetMap, int width, int height) {
        Map<String, String> styleMap = buildStyleMap(styleSheetMap, element);
        loadStyles(entry, element, styleMap, width, height);
        loadContent(entry, element, width, height);
        calculateDimentions(entry, element, styleMap, width, height);
    }

    protected void loadStyles(MOGuideEntry entry, Element element, Map<String, String> styleMap, int width, int height) {
        marginTop = getIntFromStyle("margin-top", styleMap, height);
        marginBottom = getIntFromStyle("margin-bottom", styleMap, height);
        marginLeft = getIntFromStyle("margin-left", styleMap, width);
        marginRight = getIntFromStyle("margin-right", styleMap, width);
        textAlign = calculateTextAlignFromStyle(styleMap);
        floating = getFloatingFromStyle(styleMap);
        color = getColorFromStyle(styleMap);
    }

    protected abstract void loadContent(MOGuideEntry entry, Element element, int width, int height);

    protected void calculateDimentions(MOGuideEntry entry, Element element, Map<String, String> styleMap, int parentWidth, int parentHeight) {
        this.width = calculateWidth(styleMap, parentWidth);
        if (styleMap.containsKey("height")) {
            this.height = getIntFromStyle("height", styleMap, parentHeight);
        }
        if (styleMap.containsKey("min-height")) {
            this.height = Math.min(this.height, getIntFromStyle("min-height", styleMap, parentHeight));
        }
        if (styleMap.containsKey("max-height")) {
            this.height = Math.max(this.height, getIntFromStyle("max-height", styleMap, parentHeight));
        }

    }

    protected int calculateWidth(Map<String, String> styleMap, int parentWidth) {
        int width = this.width;
        if (getFloating() == 0) {
            width = parentWidth;
        }
        if (styleMap.containsKey("width")) {
            width = getIntFromStyle("width", styleMap, parentWidth);
        }
        if (styleMap.containsKey("min-width")) {
            width = Math.min(this.width, getIntFromStyle("min-width", styleMap, parentWidth));
        }
        if (styleMap.containsKey("max-width")) {
            width = Math.max(this.width, getIntFromStyle("max-width", styleMap, parentWidth));
        }
        return width;
    }

    protected Map<String, String> decodeShortcode(String rawShortcode) {
        Map<String, String> shortCodeMap = new HashMap<>();
        String[] shortCodeParams = rawShortcode.substring(1, rawShortcode.length() - 1).split(" ");
        if (shortCodeParams.length > 0) {
            shortCodeMap.put("type", shortCodeParams[0]);
        }

        for (int i = 1; i < shortCodeParams.length; i++) {
            String[] keyValue = shortCodeParams[i].split("=", 2);
            if (keyValue.length >= 2) {
                shortCodeMap.put(keyValue[0], keyValue[1]);
            }
        }
        return shortCodeMap;
    }

    protected ItemStack shortCodeToStack(Map<String, String> shortCodeMap) {
        String mod = Reference.MOD_ID;
        int count = 1;
        int damage = 0;
        if (shortCodeMap.containsKey("mod")) {
            mod = shortCodeMap.get("mod");
        }
        if (shortCodeMap.containsKey("meta")) {
            damage = Integer.parseInt(shortCodeMap.get("meta"));
        }
        if (shortCodeMap.containsKey("count")) {
            count = Integer.parseInt(shortCodeMap.get("count"));
        }

        if (shortCodeMap.get("type").equalsIgnoreCase("block") && shortCodeMap.containsKey("name")) {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(mod, shortCodeMap.get("name")));
            if (block != null) {
                return new ItemStack(block, count, damage);
            }
        } else if (shortCodeMap.containsKey("name")) {
            Item item = Item.REGISTRY.getObject(new ResourceLocation(mod, shortCodeMap.get("name")));
            if (item != null) {
                return new ItemStack(item, count, damage);
            }
        }
        return ItemStack.EMPTY;
    }

    protected void bindTexture(ResourceLocation location) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    }

    protected FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRenderer;
    }

    public Map<String, String> decodeStyle(String styleRaw) {
        Map<String, String> styleMap = new HashMap<>();
        String[] lines = styleRaw.split(";");
        for (String line : lines) {
            String[] vars = line.split(":");
            if (vars.length == 2) {
                styleMap.put(vars[0].trim(), vars[1].trim());
            }
        }
        return styleMap;
    }

    public int getIntFromStyle(String key, Map<String, String> styleMap, int max) {
        if (styleMap.containsKey(key)) {
            return parseStyleNumber(styleMap.get(key), max);
        }
        return 0;
    }

    public int calculateTextAlignFromStyle(Map<String, String> syleMap) {
        if (syleMap.containsKey("text-align")) {
            if (syleMap.get("text-align").equalsIgnoreCase("center")) {
                return 1;
            }
            if (syleMap.get("text-align").equalsIgnoreCase("right")) {
                return 2;
            }
        }
        return 0;
    }

    public int getFloatingFromStyle(Map<String, String> stringMap) {
        if (stringMap.containsKey("float")) {
            if (stringMap.get("float").equalsIgnoreCase("left")) {
                return 1;
            } else if (stringMap.get("float").equalsIgnoreCase("right")) {
                return 2;
            }
        }
        return 0;
    }

    public Color getColorFromStyle(Map<String, String> stringMap) {
        if (stringMap.containsKey("color")) {
            if (stringMap.get("color").startsWith("#")) {
                return new Color(Integer.parseInt(stringMap.get("color").substring(1), 16));
            }
            if (stringMap.get("color").startsWith("rgb(")) {
                String[] rgb = stringMap.get("color").substring(4, stringMap.get("color").length() - 1).split(",");
                if (rgb.length == 3) {
                    return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                }
                if (rgb.length == 4) {
                    return new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]), Integer.parseInt(rgb[3]));
                }
            }
        }
        return new Color(255, 255, 255);
    }

    private int parseStyleNumber(String number, int value) {
        if (number.endsWith("px")) {
            try {
                return Integer.parseInt(number.substring(0, number.length() - 2));
            } catch (NumberFormatException e) {
                MOLog.log(Level.ERROR, e, "Could not parse Int %s in %s", number.substring(0, number.length() - 2), this);
            }

        } else if (number.endsWith("%")) {
            try {
                return (int) ((Integer.parseInt(number.substring(0, number.length() - 1)) / 100f) * value);
            } catch (NumberFormatException e) {
                MOLog.log(Level.ERROR, e, "Could not parse Int %s in %s", number.substring(0, number.length() - 1), this);
            }
        }
        return 0;
    }

    public Map<String, String> buildStyleMap(Map<String, String> styleSheetMap, Element element) {
        Map<String, String> styleMap = new HashMap<>();
        if (element.hasAttribute("class")) {
            String[] classes = element.getAttribute("class").split(" ");
            for (String aClass : classes) {
                if (styleSheetMap.containsKey("." + aClass)) {
                    styleMap.putAll(decodeStyle(styleSheetMap.get("." + aClass)));
                }
            }
        }
        if (element.hasAttribute("style")) {
            styleMap.putAll(decodeStyle(element.getAttribute("style")));
        }
        return styleMap;
    }

    public int getFloating() {
        return floating;
    }

    public int getWidth() {
        return width + marginLeft + marginRight;
    }

    public int getHeight() {
        return height + marginTop + marginBottom;
    }

    public void setGUI(MOGuiBase gui) {
        this.gui = gui;
    }
}