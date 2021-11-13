package org.lt.conquer.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lt.conquer.Conquer;
import org.lt.conquer.client.model.HunterModel;
import org.lt.conquer.client.renderer.BlueHunterRender;
import org.lt.conquer.client.renderer.RedHunterRender;
import org.lt.conquer.registy.ModEntities;

@Mod.EventBusSubscriber(modid = Conquer.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class ClientModEvents
{
    private ClientModEvents(){}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
       event.registerLayerDefinition(HunterModel.LAYER_LOCATION, HunterModel::createLayerDefinition);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
        event.registerEntityRenderer(ModEntities.BLUE_HUNTER.get(), BlueHunterRender::new);
        event.registerEntityRenderer(ModEntities.RED_HUNTER.get(), RedHunterRender::new);
    }
}
