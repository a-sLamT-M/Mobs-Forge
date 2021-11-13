package org.lt.conquer.items.spawneggs;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.lt.conquer.registy.ModEntities;
import org.lt.conquer.registy.register.ItemsManager;

public class BlueHunterSpawnEgg
{
    private final static Item.Properties BLUE_HUNTER_SPAWN_EGG = new Item.Properties()
            .stacksTo(64)
            .tab(ItemsManager.CONQUER_ITEM_GROUP);

    public static ForgeSpawnEggItem get()
    {
        return new ForgeSpawnEggItem(ModEntities.BLUE_HUNTER, 0xFFFFFF, 0x2FDC25, BLUE_HUNTER_SPAWN_EGG);
    }
}
