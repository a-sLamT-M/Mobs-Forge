package org.lt.conquer.registy;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.Conquer;
import org.lt.conquer.entities.HunterEntity;
import org.lt.conquer.registy.register.EntitiesManager;

public class ModEntities
{
    public static RegistryObject<EntityType<HunterEntity>> HUNTER;
    public static void EntitiesInit()
    {
        // hunter
         HUNTER = EntitiesManager.ENTITY_TYPES.register("hunter",
                 () -> EntityType.Builder
                         .of(HunterEntity::new, MobCategory.MONSTER)
                         .sized(1f,2f)
                         .build(new ResourceLocation(Conquer.MOD_ID, "hunter").toString()));
    }
}
