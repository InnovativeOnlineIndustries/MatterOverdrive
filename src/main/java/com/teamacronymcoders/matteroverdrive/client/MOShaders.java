package com.teamacronymcoders.matteroverdrive.client;

import com.teamacronymcoders.matteroverdrive.MatterOverdrive;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MOShaders {

  private static ShaderInstance androidShader;
  private static ShaderInstance renderStationShader;

  public static final Logger LOGGER = LoggerFactory.getLogger("Matter Overdrive: Shaders");

  public static void onRegisterShaders(RegisterShadersEvent event) {
    try {
      event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(MatterOverdrive.MOD_ID, "android_station_shader"), DefaultVertexFormat.NEW_ENTITY), e -> {
        androidShader = e;
      });
      event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(MatterOverdrive.MOD_ID, "render_station_shader"), DefaultVertexFormat.POSITION_TEX_COLOR), e -> {
        renderStationShader = e;
      });
    } catch (IOException err) {
      LOGGER.warn(err.getMessage());
    }
  }

  public static ShaderInstance getAndroidShader() {
    if (androidShader == null) throw new IllegalArgumentException("Tried getting Android Shader before it was compiled");
    return androidShader;
  }

  public static ShaderInstance getRenderStationShader() {
    if (renderStationShader == null) throw new IllegalArgumentException("Tried getting RenderStationShader before it was compiled");
    return renderStationShader;
  }
}
