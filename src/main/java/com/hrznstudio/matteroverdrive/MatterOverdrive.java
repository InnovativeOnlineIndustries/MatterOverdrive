package com.hrznstudio.matteroverdrive;

import com.hrznstudio.matteroverdrive.network.PacketHandler;
import com.hrznstudio.matteroverdrive.block.MOBlocks;
import com.hrznstudio.matteroverdrive.item.MOItems;
import com.hrznstudio.titanium.module.ModuleController;
import com.hrznstudio.titanium.network.NetworkHandler;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    public static NetworkHandler HANDLER;

    public MatterOverdrive() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::setup);
        MOItems.register(eventBus);
        MOBlocks.register(eventBus);
        HANDLER = new NetworkHandler(MOD_ID);
    }

    private void clientSetup(final FMLClientSetupEvent event) {

    }

    private void setup(final FMLCommonSetupEvent event) {
        PacketHandler.init();
    }

    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {}

    @SubscribeEvent
    public static void onDataGather(GatherDataEvent event) {
        // For Client-Side Generated Files
        if (event.includeClient()) {
            //TODO: Put stuff here
        }
    }

}
