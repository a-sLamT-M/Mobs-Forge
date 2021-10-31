package org.lt.conquer.registy;

import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;
import org.lt.conquer.items.HpPlus;
import org.lt.conquer.registy.register.ItemsManager;

public class ModItems
{
    public static RegistryObject<Item> HPPLUS;
    public static void itemsInit()
    {
        //hp_plus
        HPPLUS = ItemsManager.ITEMS.register("hp_plus", HpPlus::new);
    }


}
