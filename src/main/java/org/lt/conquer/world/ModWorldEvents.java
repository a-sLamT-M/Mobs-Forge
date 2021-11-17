package org.lt.conquer.world;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lt.conquer.Conquer;
import org.lt.conquer.entities.BlueHunterEntity;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.world.gen.ModSpawn;
import org.lt.conquer.world.gen.Spawner;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Conquer.MOD_ID)
public class ModWorldEvents
{
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event)
    {
        Spawner.onEntitySpawn(event);
    }

    @SubscribeEvent
    public static void playerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event)
    {
        ModSpawn.onLogging(event);
    }
}
