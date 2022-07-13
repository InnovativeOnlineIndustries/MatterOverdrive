package com.hrznstudio.matteroverdrive;

import com.hrznstudio.matteroverdrive.android.perks.PerkTree;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.capabilities.MOCapabilityHandler;
import com.hrznstudio.matteroverdrive.client.MOClientModEvents;
import com.hrznstudio.matteroverdrive.client.gui.AndroidHudScreen;
import com.hrznstudio.matteroverdrive.client.renderer.entity.MORenderers;
import com.hrznstudio.matteroverdrive.client.screen.AndroidStationScreen;
import com.hrznstudio.matteroverdrive.datagen.MOBlockstateProvider;
import com.hrznstudio.matteroverdrive.datagen.MOItemModelProvider;
import com.hrznstudio.matteroverdrive.datagen.MOModelProvider;
import com.hrznstudio.matteroverdrive.entity.MOEntities;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.sounds.MOSounds;
import com.hrznstudio.titanium.event.handler.EventManager;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MatterOverdrive.MOD_ID)
public class MatterOverdrive {

    public static final String MOD_ID = "matteroverdrive";
    public static final Logger LOGGER = LogManager.getLogger("Matter Overdrive");

    public MatterOverdrive() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::setup);
        eventBus.addListener(MOClientModEvents::registerKeyBinds);

        MOItems.register(eventBus);
        MOBlocks.register(eventBus);
        MOEntities.register(eventBus);
        MOSounds.register(eventBus);

        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> AndroidHudScreen::new);

        PerkTree.poke();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MORenderers.register();
        ScreenManager.registerFactory(MOBlocks.ANDROID_CONTAINER.get(), AndroidStationScreen::new);
        MOClientModEvents.onClient();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
        MOCapabilityHandler.register();
    }


    public static void onServerStarting(RegisterCommandsEvent event) {
    }
}
