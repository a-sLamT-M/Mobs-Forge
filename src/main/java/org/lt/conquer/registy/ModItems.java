package org.lt.conquer.registy;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.items.HpPlus;
import org.lt.conquer.items.HunterSpawnEgg;
import org.lt.conquer.registy.register.ItemsManager;

public class ModItems
{
    public static RegistryObject<Item> HPPLUS;
    public static RegistryObject<ForgeSpawnEggItem> HUNTER_SPAWN_EGG;
    private final static Item.Properties p_HUNTER_SPAWN_EGG = new Item.Properties()
            .stacksTo(64)
            .tab(ItemsManager.CONQUER_ITEM_GROUP);

    public static void itemsInit()
    {
        //hp_plus
        HPPLUS = ItemsManager.ITEMS.register("hp_plus", HpPlus::new);
        //hunter_spawn_egg
        HUNTER_SPAWN_EGG = ItemsManager.ITEMS.register("hunter_spawn_egg", HunterSpawnEgg::get);
    }
}
