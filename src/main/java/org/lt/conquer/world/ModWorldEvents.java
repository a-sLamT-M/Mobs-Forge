package org.lt.conquer.world;

import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lt.conquer.Conquer;
import org.lt.conquer.world.gen.Spawner;

@Mod.EventBusSubscriber(modid = Conquer.MOD_ID)
public class ModWorldEvents
{
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event)
    {
        Spawner.onEntitySpawn(event);
    }
}
