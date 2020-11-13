package com.hrznstudio.matteroverdrive.client.renderer.entity;

import com.hrznstudio.matteroverdrive.client.renderer.entity.projectile.KunaiRenderer;
import com.hrznstudio.matteroverdrive.entity.MOEntities;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class MORenderers {

    public static void register() {
        RenderingRegistry.registerEntityRenderingHandler(MOEntities.KUNAI.get(), KunaiRenderer::new);
    }

}
