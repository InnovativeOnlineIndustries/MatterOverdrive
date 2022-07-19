package com.hrznstudio.matteroverdrive.datagen.client;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public class MOShaders {

  private static ShaderInstance androidShader;
  private static ShaderInstance renderStationShader;

  public static void onRegisterShaders(RegisterShadersEvent event) {
    try {
      event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(MatterOverdrive.MOD_ID, "android_station_shader"), DefaultVertexFormat.NEW_ENTITY), e -> {
        androidShader = e;
      });
      event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation(MatterOverdrive.MOD_ID, "render_station_shader"), DefaultVertexFormat.NEW_ENTITY), e -> {
        renderStationShader = e;
      });
    } catch (IOException err) {
      MatterOverdrive.LOGGER.warn(err.getMessage());
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
