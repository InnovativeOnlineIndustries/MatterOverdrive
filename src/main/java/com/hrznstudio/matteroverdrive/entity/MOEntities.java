package com.hrznstudio.matteroverdrive.entity;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.entity.projectile.KunaiEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MOEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MatterOverdrive.MOD_ID);

    public static final RegistryObject<EntityType<KunaiEntity>> KUNAI = register("kunai",
        EntityType.Builder.<KunaiEntity>create(KunaiEntity::new, EntityClassification.MISC).size(0.5F, 0.5F).trackingRange(8));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String key, EntityType.Builder<T> builder) {
        return ENTITIES.register(key, () -> builder.build(key));
    }

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

}
