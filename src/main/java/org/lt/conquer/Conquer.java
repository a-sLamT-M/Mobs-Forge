package org.lt.conquer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lt.conquer.registy.register.EntitiesManager;
import org.lt.conquer.registy.register.ItemsManager;

@Mod(Conquer.MOD_ID)
public class Conquer
{
    public static final String MOD_ID = "lconquer";
    private static final Logger LOGGER = LogManager.getLogger();

    public Conquer()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        EntitiesManager.getInstance().register(eventBus);
        ItemsManager.getInstance().register(eventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("[HQ] Conquer has been loaded.");
    }
}
