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

package matteroverdrive.items;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemUpgrade extends MOBaseItem implements IUpgrade, IAdvancedModelProvider {
    public static final String[] subItemNames = {"base", "speed", "power", "failsafe", "range", "power_storage", "hyper_speed", "matter_storage"};

    public ItemUpgrade(String name) {
        super(name);
        this.setMaxStackSize(16);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_MODULES);
    }

    @Override
    public String[] getSubNames() {
        return subItemNames;
    }

    @Override
    public boolean hasDetails(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        return damage != 0;
    }

    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addDetails(ItemStack itemstack, EntityPlayer player, @Nullable World worldIn, List<String> infos) {
        Map<UpgradeTypes, Double> stats = getUpgrades(itemstack);
        for (final Map.Entry<UpgradeTypes, Double> entry : stats.entrySet()) {
            infos.add(MOStringHelper.toInfo(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs))
            for (int i = 0; i < subItemNames.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
    }

    /*@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        int j = MathHelper.clamp(damage, 0, (icons.length-1));
        return this.icons[j];
    }*/

    @Override
    public String getTranslationKey(ItemStack stack) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, (subItemNames.length - 1));
        return super.getTranslationKey() + "." + subItemNames[i];
    }

    /*@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
    {
        this.icons = new IIcon[subItemNames.length];

        for (int i = 0; i < subItemNames.length; ++i)
        {
            this.icons[i] = iconRegister.registerIcon(Reference.MOD_ID + ":" + getUnlocalizedName().substring(5) + "_" + subItemNames[i]);
        }

        this.itemIcon = this.icons[0];
    }*/
    @Override
    public Map<UpgradeTypes, Double> getUpgrades(ItemStack itemStack) {
        HashMap<UpgradeTypes, Double> upgrades = new HashMap<>();
        int damage = itemStack.getItemDamage();
        switch (damage) {
            case 1:
                //the speed upgrade
                upgrades.put(UpgradeTypes.Speed, 0.75);
                upgrades.put(UpgradeTypes.PowerUsage, 1.25);
                upgrades.put(UpgradeTypes.Fail, 1.25);
                upgrades.put(UpgradeTypes.MatterUsage, 1.25);
                break;
            case 2:
                //less power upgrade
                upgrades.put(UpgradeTypes.Speed, 1.5);
                upgrades.put(UpgradeTypes.PowerUsage, 0.75);
                upgrades.put(UpgradeTypes.Fail, 1.25);
                break;
            case 3:
                //less chance to fail upgrade
                upgrades.put(UpgradeTypes.Fail, 0.5);
                upgrades.put(UpgradeTypes.Speed, 1.25);
                upgrades.put(UpgradeTypes.PowerUsage, 1.25);
                upgrades.put(UpgradeTypes.MatterUsage, 1.25);
                break;
            case 4:
                //range upgrade
                upgrades.put(UpgradeTypes.Range, 4d);
                upgrades.put(UpgradeTypes.PowerUsage, 1.5);
                upgrades.put(UpgradeTypes.MatterUsage, 1.5);
                break;
            case 5:
                upgrades.put(UpgradeTypes.PowerStorage, 2d);
                break;
            //hyper speed
            case 6:
                upgrades.put(UpgradeTypes.Speed, 0.15);
                upgrades.put(UpgradeTypes.PowerUsage, 2d);
                upgrades.put(UpgradeTypes.MatterUsage, 2d);
                upgrades.put(UpgradeTypes.Fail, 1.25);
                break;
            case 7:
                upgrades.put(UpgradeTypes.MatterStorage, 2d);
        }
        return upgrades;
    }

    @Override
    public UpgradeTypes getMainUpgrade(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        switch (damage) {
            case 1:
                //the speed upgrade
                return UpgradeTypes.Speed;
            case 2:
                //less power upgrade
                return UpgradeTypes.PowerUsage;
            case 3:
                //less chance to fail upgrade
                return UpgradeTypes.Fail;
            case 4:
                //range upgrade
                return UpgradeTypes.Range;
            case 5:
                return UpgradeTypes.PowerStorage;
            //hyper speed
            case 6:
                return UpgradeTypes.Speed;
            case 7:
                return UpgradeTypes.MatterStorage;
        }
        return null;
    }
}