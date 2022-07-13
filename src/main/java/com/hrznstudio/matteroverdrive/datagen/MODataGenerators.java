package com.hrznstudio.matteroverdrive.datagen;

import com.hrznstudio.matteroverdrive.MatterOverdrive;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MODataGenerators {

    @SubscribeEvent
    public void onDataGather(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new MOBlockstateProvider(event.getGenerator(), MatterOverdrive.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new MOItemModelProvider(event.getGenerator(), MatterOverdrive.MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new MOModelProvider(event.getGenerator(), MatterOverdrive.MOD_ID, event.getExistingFileHelper()));
    }
}
