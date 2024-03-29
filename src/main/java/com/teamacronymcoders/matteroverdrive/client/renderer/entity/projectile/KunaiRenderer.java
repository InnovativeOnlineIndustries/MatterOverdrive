package com.teamacronymcoders.matteroverdrive.client.renderer.entity.projectile;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.entity.projectile.KunaiEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KunaiRenderer extends ArrowRenderer<KunaiEntity> {
    public static final ResourceLocation RES_ARROW = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/entity/projectiles/kunai.png");

    public KunaiRenderer(EntityRendererProvider.Context  manager) {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(KunaiEntity entity) {
        return RES_ARROW;
    }
}
