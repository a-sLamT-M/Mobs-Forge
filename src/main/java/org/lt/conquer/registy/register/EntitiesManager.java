package org.lt.conquer.registy.register;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lt.conquer.Conquer;
import org.lt.conquer.registy.ModEntities;

public class EntitiesManager implements IRegistry
{
    public enum Singleton
    {
        INSTANCE;
        private final EntitiesManager instance;
        Singleton()
        {
            instance = new EntitiesManager();
        }

    }
    public static EntitiesManager getInstance()
    {
        return EntitiesManager.Singleton.INSTANCE.instance;
    }
    private EntitiesManager(){}

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, Conquer.MOD_ID);

    @Override
    public void register(IEventBus eventBus)
    {
        ModEntities.EntitiesInit();
        ENTITY_TYPES.register(eventBus);
    }
}
