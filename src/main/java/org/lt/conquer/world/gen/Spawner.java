package org.lt.conquer.world.gen;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lt.conquer.Conquer;
import org.lt.conquer.registy.ModEntities;

import java.util.Arrays;
import java.util.List;

public class Spawner
{
    public static void onEntitySpawn(final BiomeLoadingEvent event)
    {
        List<MobSpawnSettings.SpawnerData> monster = event.getSpawns().getSpawner(MobCategory.MONSTER);
        List<MobSpawnSettings.SpawnerData> creature = event.getSpawns().getSpawner(MobCategory.CREATURE);
        monster.add(new MobSpawnSettings.SpawnerData(ModEntities.HUNTER.get(), 8, 1,4));
        creature.add(new MobSpawnSettings.SpawnerData(ModEntities.HUNTER.get(), 80, 1,4));

    }

    /*public static void onEntitySpawn(final BiomeLoadingEvent event) {
        MobSpawnInfoBuilder.getSpawner(ModEntities.HUNTER.get())
    }


    private static void addEntityToAllBiomes(MobSpawnInfoBuilder spawns, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<MobSpawnInfo.Spawners> base = spawns.getSpawner(type.getClassification());
        base.add(new MobSpawnInfo.Spawners(type,weight, minCount, maxCount));
    }*/
}
