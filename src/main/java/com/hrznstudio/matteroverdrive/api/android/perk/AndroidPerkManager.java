package com.hrznstudio.matteroverdrive.api.android.perk;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AndroidPerkManager implements INBTSerializable<CompoundNBT> {

    public static String NBT_ENABLED = "Enabled";
    public static String NBT_OWNED = "Owned";

    private Map<String, Integer> owned;
    private List<String> enabled;

    public AndroidPerkManager() {
        this.owned = new HashMap<>();
        this.enabled = new ArrayList<>();
    }

    public boolean hasPerk(IAndroidPerk perk){
        return owned.containsKey(perk.getName());
    }

    public boolean hasPerkEnabled(IAndroidPerk perk){
        return enabled.contains(perk.getName());
    }

    public Map<String, Integer> getOwned() {
        return owned;
    }

    public List<String> getEnabled() {
        return enabled;
    }

    public void buyPerk(IAndroidPerk perk){
        if (this.owned.containsKey(perk.getName())){
            this.owned.put(perk.getName(), this.owned.get(perk.getName()) + 1);
        } else {
            this.owned.put(perk.getName(), 1);
        }
    }

    public int getLevel(IAndroidPerk perk){
        return hasPerk(perk) ? this.owned.get(perk.getName()) : 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compoundNBT =  new CompoundNBT();
        CompoundNBT owned = new CompoundNBT();
        for (String s : this.owned.keySet()) {
            owned.putInt(s, this.owned.get(s));
        }
        CompoundNBT enabled = new CompoundNBT();
        for (int i = 0; i < this.enabled.size(); i++) {
            enabled.putString(i + "", this.enabled.get(i));
        }
        compoundNBT.put(NBT_ENABLED, enabled);
        compoundNBT.put(NBT_OWNED, owned);
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.owned.clear();
        this.enabled.clear();
        CompoundNBT owned = nbt.getCompound(NBT_OWNED);
        for (String s : owned.keySet()) {
            this.owned.put(s, owned.getInt(s));
        }
        CompoundNBT enabled = nbt.getCompound(NBT_ENABLED);
        for (String s : enabled.keySet()) {
            this.enabled.add(enabled.getString(s));
        }
    }
}
