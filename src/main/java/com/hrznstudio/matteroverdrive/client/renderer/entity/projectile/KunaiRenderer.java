package com.hrznstudio.matteroverdrive.client.renderer.entity.projectile;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.hrznstudio.matteroverdrive.entity.projectile.KunaiEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class KunaiRenderer extends ArrowRenderer<KunaiEntity> {
   public static final ResourceLocation RES_ARROW = new ResourceLocation(MatterOverdrive.MOD_ID, "textures/entity/projectiles/kunai.png");

   public KunaiRenderer(EntityRendererManager manager) {
      super(manager);
   }

   /**
    * Returns the location of an entity's texture.
    */
   public ResourceLocation getEntityTexture(KunaiEntity entity) {
      return RES_ARROW;
   }
}
