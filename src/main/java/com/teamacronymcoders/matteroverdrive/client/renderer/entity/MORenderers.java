package com.teamacronymcoders.matteroverdrive.client.renderer.entity;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.teamacronymcoders.matteroverdrive.block.MOBlocks;
import com.teamacronymcoders.matteroverdrive.client.renderer.entity.projectile.KunaiRenderer;
import com.teamacronymcoders.matteroverdrive.client.renderer.tile.RenderAndroidStation;
import com.teamacronymcoders.matteroverdrive.entity.MOEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value= Dist.CLIENT, modid= MatterOverdrive.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class MORenderers {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(MOEntities.KUNAI.get(), KunaiRenderer::new);
        event.registerBlockEntityRenderer(MOBlocks.ANDROID_STATION_TILE.get(), RenderAndroidStation::new);
    }


    /**
     * TODO Set your render type in your model's JSON or override {@link net.minecraft.client.resources.model.BakedModel#getRenderTypes(BlockState, net.minecraft.util.RandomSource, net.minecraftforge.client.model.data.ModelData)}
     */
    //RenderTypeLookup.setRenderLayer(MOBlocks.ANDROID_STATION.get(), RenderType.getCutout());

}
