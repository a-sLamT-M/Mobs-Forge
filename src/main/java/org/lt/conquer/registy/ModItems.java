package org.lt.conquer.registy;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.items.HpPlus;
import org.lt.conquer.items.spawneggs.BlueHunterSpawnEgg;
import org.lt.conquer.items.spawneggs.RedHunterSpawnEgg;
import org.lt.conquer.registy.register.ItemsManager;

public class ModItems
{
    public static RegistryObject<Item> HPPLUS;
    public static RegistryObject<ForgeSpawnEggItem> RED_HUNTER_SPAWN_EGG;
    public static RegistryObject<ForgeSpawnEggItem> BLUE_HUNTER_SPAWN_EGG;


    public static void itemsInit()
    {
        //hp_plus
        HPPLUS = ItemsManager.ITEMS.register("hp_plus", HpPlus::new);
        //hunter_spawn_egg
        RED_HUNTER_SPAWN_EGG = ItemsManager.ITEMS.register("red_hunter_spawn_egg", RedHunterSpawnEgg::get);
        BLUE_HUNTER_SPAWN_EGG = ItemsManager.ITEMS.register("blue_hunter_spawn_egg", BlueHunterSpawnEgg::get);
    }
}
