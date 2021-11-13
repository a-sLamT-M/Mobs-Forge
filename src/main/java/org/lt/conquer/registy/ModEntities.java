package org.lt.conquer.registy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.Conquer;
import org.lt.conquer.entities.BlueHunterEntity;
import org.lt.conquer.entities.RedHunterEntity;
import org.lt.conquer.registy.register.EntitiesManager;

public class ModEntities
{
    public static RegistryObject<EntityType<RedHunterEntity>> RED_HUNTER;
    public static RegistryObject<EntityType<BlueHunterEntity>> BLUE_HUNTER;

    public static void EntitiesInit()
    {

        BLUE_HUNTER = EntitiesManager.ENTITY_TYPES.register("blue_hunter",
                () -> EntityType.Builder
                        .of(BlueHunterEntity::new, MobCategory.MONSTER)
                        .sized(1f,2f)
                        .build(new ResourceLocation(Conquer.MOD_ID, "blue_hunter").toString()));

        RED_HUNTER = EntitiesManager.ENTITY_TYPES.register("red_hunter",
                 () -> EntityType.Builder
                         .of(RedHunterEntity::new, MobCategory.MONSTER)
                         .sized(1f,2f)
                         .build(new ResourceLocation(Conquer.MOD_ID, "red_hunter").toString()));
    }
}
