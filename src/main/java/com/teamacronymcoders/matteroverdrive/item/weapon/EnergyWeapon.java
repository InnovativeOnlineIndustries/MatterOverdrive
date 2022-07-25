package com.teamacronymcoders.matteroverdrive.item.weapon;

import com.teamacronymcoders.matteroverdrive.api.weapon.IMOWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

public abstract class EnergyWeapon extends Item implements IMOWeapon, ICapabilityProvider {

  public static final String WEAPON_DAMAGE_TAG = "WeaponDamage";
  public static final String WEAPON_ACCURACY_TAG = "WeaponAccuracy";
  public static final String WEAPON_RANGE_TAG = "WeaponRange";
  public static final String WEAPON_SPEED_TAG = "WeaponSpeed";
  public static final String WEAPON_DAMAGE_MULTIPLY_TAG = "WeaponDamageMultiply";
  public static final String WEAPON_ACCURACY_MULTIPLY_TAG = "WeaponAccuracyMultiply";
  public static final String WEAPON_RANGE_MULTIPLY_TAG = "WeaponRangeMultiply";
  public static final String WEAPON_SPEED_MULTIPLY_TAG = "WeaponSpeedMultiply";

  private IEnergyStorage storage;
  private final LazyOptional<IEnergyStorage> energyStorageCap = LazyOptional.of(() -> storage);
  private final int defaultRange;

  private final DecimalFormat damageFormater = new DecimalFormat("#.##");

  protected InteractionResultHolder<ItemStack> leftClickFire;

  public EnergyWeapon(Item.Properties properties, int capacity, int defaultRange) {
    this(properties, capacity, capacity, capacity, 0, defaultRange);
  }

  public EnergyWeapon(Item.Properties properties, int capacity, int maxTransfer, int defaultRange) {
    this(properties, capacity, maxTransfer, maxTransfer, 0, defaultRange);
  }

  public EnergyWeapon(Item.Properties properties, int capacity, int maxReceive, int maxExtract, int defaultRange) {
    this(properties, capacity, maxReceive, maxExtract, 0, defaultRange);
  }

  public EnergyWeapon(Item.Properties properties, int capacity, int maxReceive, int maxExtract, int energy, int defaultRange) {
    super(properties.stacksTo(1));
    this.storage = new EnergyStorage(capacity, maxReceive, maxExtract, energy);
    this.defaultRange = defaultRange;
  }

  @Nullable
  public static IEnergyStorage getStorage(ItemStack stack) {
    LazyOptional<IEnergyStorage> storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
    if (storage.isPresent()) {
      return storage.resolve().get();
    }
    else return null;
  }

  @Override
  public int getMaxDamage(ItemStack weapon) {
    IEnergyStorage storage = getStorage(weapon);
    return storage != null ? storage.getMaxEnergyStored() : super.getMaxDamage(weapon);
  }

  @Override
  public boolean isBarVisible(ItemStack weapon) {
    return true;
  }

  @Override
  public int getBarWidth(ItemStack weapon) {
    IEnergyStorage storage = getStorage(weapon);
    return storage != null ? (storage.getMaxEnergyStored() - storage.getEnergyStored()) / storage.getMaxEnergyStored() : super.getBarWidth(weapon);
  }

  @Override
  public int getBarColor(ItemStack weapon) {
    return 15866137;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltips, TooltipFlag flag) {
    IEnergyStorage storage = getStorage(stack);
    tooltips.add(Component.translatable("tooltip.matteroverdrive.charge", storage.getEnergyStored(), storage.getMaxEnergyStored()).withStyle(ChatFormatting.YELLOW));
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @org.jetbrains.annotations.Nullable Direction side) {
    if (cap == CapabilityEnergy.ENERGY) {
      energyStorageCap.cast();
    }
    return super.getDefaultInstance().getCapability(cap, side);
  }



}
