package org.lt.conquer.registy.register;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lt.conquer.Conquer;
import org.lt.conquer.registy.ModItems;

public class ItemsManager implements IRegistry
{
    public enum Singleton
    {
        INSTANCE;
        private final ItemsManager instance;
        Singleton()
        {
            instance = new ItemsManager();
        }

    }

    public static ItemsManager getInstance()
    {
        return Singleton.INSTANCE.instance;
    }
    private ItemsManager(){}

    // tab
    public static final CreativeModeTab CONQUER_ITEM_GROUP = new CreativeModeTab("counquerItemsGroup")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(Items.CROSSBOW);
        }
    };

    // items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Conquer.MOD_ID);

    @Override
    public void register(IEventBus eventBus)
    {
        ModItems.itemsInit();
        ITEMS.register(eventBus);
    }
}
