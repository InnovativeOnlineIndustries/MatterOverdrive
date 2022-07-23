package com.teamacronymcoders.matteroverdrive;

import com.teamacronymcoders.matteroverdrive.android.perks.PerkTree;
import com.teamacronymcoders.matteroverdrive.block.MOBlocks;
import com.teamacronymcoders.matteroverdrive.client.MOClientModEvents;
import com.teamacronymcoders.matteroverdrive.client.MOShaders;
import com.teamacronymcoders.matteroverdrive.client.screen.AndroidHudScreen;
import com.teamacronymcoders.matteroverdrive.client.screen.AndroidStationScreen;
import com.teamacronymcoders.matteroverdrive.entity.MOEntities;
import com.teamacronymcoders.matteroverdrive.item.MOItems;
import com.teamacronymcoders.matteroverdrive.network.PacketHandler;
import com.teamacronymcoders.matteroverdrive.sounds.MOSounds;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(MatterOverdrive.MOD_ID)
public class MatterOverdrive {

    public static final String MOD_ID = "matteroverdrive";
    public static final Logger LOGGER = LoggerFactory.getLogger("Matter Overdrive");

    public MatterOverdrive() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::setup);
        eventBus.addListener(MOClientModEvents::colorHandlerEvent);
        eventBus.addListener(MOClientModEvents::registerKeyBinds);
        eventBus.addListener(MOShaders::onRegisterShaders);

        MOItems.register(eventBus);
        MOBlocks.register(eventBus);
        MOEntities.register(eventBus);
        MOSounds.register(eventBus);

        DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> AndroidHudScreen::new);

        PerkTree.poke();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MenuScreens.register(MOBlocks.ANDROID_CONTAINER.get(), AndroidStationScreen::new);
        MOClientModEvents.onClient();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }


    public static void onServerStarting(RegisterCommandsEvent event) {
    }
}
