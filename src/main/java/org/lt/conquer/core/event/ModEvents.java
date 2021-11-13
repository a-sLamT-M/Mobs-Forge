package org.lt.conquer.core.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lt.conquer.Conquer;
import org.lt.conquer.entities.BlueHunterEntity;
import org.lt.conquer.entities.RedHunterEntity;
import org.lt.conquer.registy.ModEntities;

@Mod.EventBusSubscriber(modid = Conquer.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents
{
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event)
    {
        event.put(ModEntities.BLUE_HUNTER.get(), BlueHunterEntity.createAttributes().build());
        event.put(ModEntities.RED_HUNTER.get(), RedHunterEntity.createAttributes().build());
    }
}
