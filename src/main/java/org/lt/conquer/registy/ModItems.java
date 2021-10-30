package org.lt.conquer.registy;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.items.hpPlus;
import org.lt.conquer.registy.register.ItemsManager;

import java.rmi.registry.Registry;

public class ModItems
{
    public static void registerForAll()
    {
        RegistryObject<Item> HPPLUS = ItemsManager.ITEMS.register(
                "hp_plus",hpPlus::new
        );
    }

}
