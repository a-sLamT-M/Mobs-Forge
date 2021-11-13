package org.lt.conquer.world.gen;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.lt.conquer.registy.ModEntities;

import java.util.List;

public class Spawner
{
    public static void onEntitySpawn(final BiomeLoadingEvent event)
    {
        List<MobSpawnSettings.SpawnerData> monster = event.getSpawns().getSpawner(MobCategory.MONSTER);
        List<MobSpawnSettings.SpawnerData> creature = event.getSpawns().getSpawner(MobCategory.CREATURE);

        monster.add(new MobSpawnSettings.SpawnerData(ModEntities.RED_HUNTER.get(), 12, 1,2));
        creature.add(new MobSpawnSettings.SpawnerData(ModEntities.RED_HUNTER.get(), 44, 1,4));

        monster.add(new MobSpawnSettings.SpawnerData(ModEntities.BLUE_HUNTER.get(), 12, 1,2));
        creature.add(new MobSpawnSettings.SpawnerData(ModEntities.BLUE_HUNTER.get(), 44, 1,4));
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
