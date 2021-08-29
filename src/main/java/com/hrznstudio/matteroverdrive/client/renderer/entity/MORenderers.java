package com.hrznstudio.matteroverdrive.client.renderer.entity;

import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.client.renderer.entity.projectile.KunaiRenderer;
import com.hrznstudio.matteroverdrive.client.renderer.tile.RenderAndroidStation;
import com.hrznstudio.matteroverdrive.entity.MOEntities;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class MORenderers {

    public static void register() {
        RenderingRegistry.registerEntityRenderingHandler(MOEntities.KUNAI.get(), KunaiRenderer::new);

        RenderTypeLookup.setRenderLayer(MOBlocks.ANDROID_STATION.get(), RenderType.getCutout());

        ClientRegistry.bindTileEntityRenderer(MOBlocks.ANDROID_STATION_TILE.get(), RenderAndroidStation::new);
    }

}
