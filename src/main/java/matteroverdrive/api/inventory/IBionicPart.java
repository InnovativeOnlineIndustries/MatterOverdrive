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

package matteroverdrive.api.inventory;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 5/26/2015.
 * This class represents parts that can be worn by Android players.
 * By equipping them in the Android Station.
 */
public interface IBionicPart {
    /**
     * The type of part. At witch part for the body can the Bionic part be worn.
     * <ol>
     * <li>Head</li>
     * <li>Arms</li>
     * <li>Legs</li>
     * <li>Chest</li>
     * <li>Other</li>
     * <li>Battery</li>
     * </ol>
     *
     * @param itemStack The bionic Items Stack.
     * @return The type of bionic part.
     */
    int getType(ItemStack itemStack);

    /**
     * @param player    The android player.
     * @param itemStack The bionic item stack.
     * @return Does the bionic part affect the android player.
     */
    boolean affectAndroid(AndroidPlayer player, ItemStack itemStack);

    /**
     * A Multimap of modifiers similar to vanilla armor modifiers.
     *
     * @param player    The android player.
     * @param itemStack The Bionic part item stack.
     * @return A multimap of modifiers.
     */
    Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack);

    /**
     * Returns the bionic part texture for the given item stack.
     *
     * @param androidPlayer the android player.
     * @param itemStack     the item stack.
     * @return the resource location of the texture.
     */
    @SideOnly(Side.CLIENT)
    ResourceLocation getTexture(AndroidPlayer androidPlayer, ItemStack itemStack);

    @SideOnly(Side.CLIENT)
    ModelBiped getModel(AndroidPlayer androidPlayer, ItemStack itemStack);
}
