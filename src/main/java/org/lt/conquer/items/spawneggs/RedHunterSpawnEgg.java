package org.lt.conquer.items.spawneggs;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.registy.register.ItemsManager;

public class RedHunterSpawnEgg
{
    private final static Item.Properties RED_HUNTER_SPAWN_EGG = new Item.Properties()
            .stacksTo(64)
            .tab(ItemsManager.CONQUER_ITEM_GROUP);

    public static ForgeSpawnEggItem get()
    {
        return new ForgeSpawnEggItem(ModEntities.RED_HUNTER, 0xFFFFFF, 0x2FDC22, RED_HUNTER_SPAWN_EGG);
    }
}
