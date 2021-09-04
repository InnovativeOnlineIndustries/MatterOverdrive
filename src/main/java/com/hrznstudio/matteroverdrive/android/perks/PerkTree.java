package com.hrznstudio.matteroverdrive.android.perks;

import com.google.common.collect.ImmutableMultimap;
import com.hrznstudio.matteroverdrive.capabilities.AndroidEnergyCapability;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilities;
import com.hrznstudio.matteroverdrive.item.food.AndroidPillItem;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;

import java.awt.*;
import java.util.Collections;
import java.util.UUID;

public class PerkTree {

    //NANOBOTS - Implemented
    //ATTACK BOOST - Implemented
    //FLASH COOLING - Waiting for guns
    //SONIC SHOCKWAVE -
    //NANO ARMOR - Implemented
    //PLASMA SHIELD -
    //EMERGENCY SHIELD -
    //CLOAK -
    //ZERO CALORIES - Implemented
    //RESPIROCYTES - Implemented
    //AIR BAGS -
    //NIGHT VISION - Implemented

    public static BasePerkBuilder NANONOTS = new BasePerkBuilder("nanobots")
            .point(new Point(0, 0))
            .xpNeeded(26)
            .maxLevel(4)
            .onAndroidTick((iAndroid, integer) -> {
                if (iAndroid.getHolder().getEntityWorld().getGameTime() % 20 == 0)
                    iAndroid.getHolder().heal(1); //TODO Check for energy
            })
            .attributeModifierMultimap((iAndroid, integer) -> ImmutableMultimap.of(Attributes.MAX_HEALTH, new AttributeModifier(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a0"), "Nanobots", 5 * integer, AttributeModifier.Operation.ADDITION)))
            .child(new BasePerkBuilder("attack_boost")
                    .maxLevel(4)
                    .xpNeeded(30)
                    .attributeModifierMultimap((iAndroid, integer) -> ImmutableMultimap.of(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a1"), "Attack Boost", 1 + 0.05 + 0.05 * integer, AttributeModifier.Operation.MULTIPLY_TOTAL)))
                    .child(new BasePerkBuilder("flash_cooling")
                            .xpNeeded(28)
                            .child(new BasePerkBuilder("sonic_shockwave")
                                    .xpNeeded(32)
                            )
                    )
            )
            .child(new BasePerkBuilder("nano_armor")
                    .maxLevel(4)
                    .xpNeeded(30)
                    .attributeModifierMultimap((iAndroid, integer) -> ImmutableMultimap.of(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(UUID.fromString("d28b7061-fb92-4064-90fb-7e02b95a72a2"), "Nano Armor", 1 + 0.12 * integer, AttributeModifier.Operation.MULTIPLY_TOTAL)))
                    .child(new BasePerkBuilder("plasma_shield")
                            .xpNeeded(36)
                            .requiredItems(Collections.singletonList(new ItemStack(Items.SHIELD)))
                            .child(new BasePerkBuilder("emergency_shield")
                                    .xpNeeded(26)
                            )
                            .child(new BasePerkBuilder("cloak")
                                    .xpNeeded(36)
                                    .onAndroidTick((iAndroid, integer) -> iAndroid.getHolder().getCapability(CapabilityEnergy.ENERGY).ifPresent(iEnergyStorage -> iEnergyStorage.extractEnergy(128, false)))
                            )
                    )
            );

    public static BasePerkBuilder RESPIROCYTES;

    public static BasePerkBuilder ZERO_CALORIES = new BasePerkBuilder("zero_calories")
            .point(new Point(0, 3))
            .xpNeeded(18)
            .onAndroidTick((iAndroid, integer) -> {
                if (iAndroid.getHolder() instanceof ServerPlayerEntity) {
                    if (((ServerPlayerEntity) iAndroid.getHolder()).getFoodStats().getFoodLevel() < 8) {
                        ((ServerPlayerEntity) iAndroid.getHolder()).getFoodStats().setFoodLevel(10);
                    }
                    if (((ServerPlayerEntity) iAndroid.getHolder()).getFoodStats().getFoodLevel() > 12) {
                        ((ServerPlayerEntity) iAndroid.getHolder()).getFoodStats().setFoodLevel(10);
                    }
                }
            })
            .child(RESPIROCYTES = new BasePerkBuilder("respirocytes")
                    .xpNeeded(12)
                    .onAndroidTick((iAndroid, integer) -> iAndroid.getHolder().setAir(iAndroid.getHolder().getMaxAir()))
                    .child(new BasePerkBuilder("air_bags")
                            .xpNeeded(14)
                    )
            );

    public static BasePerkBuilder NIGHT_VISION;

    public static void poke() {
        NIGHT_VISION = new BasePerkBuilder("night_vision")
                .point(new Point(5, 3))
                .xpNeeded(28)
                .canToggle()
                .onAndroidTick((iAndroid, integer) -> {
                    if (iAndroid.getPerkManager().hasPerkEnabled(NIGHT_VISION)) {
                        iAndroid.getHolder().getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                            if (energyStorage.getEnergyStored() >= 16) {
                                energyStorage.extractEnergy(16, false);
                                iAndroid.getHolder().addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 15 * 20, 0, true, false));
                                if (iAndroid.getHolder() instanceof ServerPlayerEntity)
                                    AndroidEnergyCapability.syncEnergy((ServerPlayerEntity) iAndroid.getHolder());
                            }
                        });
                    } else {
                        iAndroid.getHolder().removePotionEffect(Effects.NIGHT_VISION);
                    }
                });


        //Cancel healing when android
        EventManager.forge(LivingHealEvent.class).process(livingHealEvent -> {
            //livingHealEvent.getEntityLiving().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> livingHealEvent.setCanceled(true));
        }).subscribe();
        EventManager.forge(LivingEntityUseItemEvent.Start.class).process(livingEntityUseItemEvent -> {
            livingEntityUseItemEvent.getEntity().getCapability(MOCapabilities.ANDROID_DATA).ifPresent(iAndroid -> {
                if (iAndroid.getPerkManager().hasPerk(ZERO_CALORIES) && !(livingEntityUseItemEvent.getItem().getItem() instanceof AndroidPillItem)) {
                    livingEntityUseItemEvent.setCanceled(true);
                    livingEntityUseItemEvent.setDuration(0);
                }
            });
        }).subscribe();
        //TODO Make androids drown
    }

}
