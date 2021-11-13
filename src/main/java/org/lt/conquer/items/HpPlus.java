package org.lt.conquer.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import org.lt.conquer.registy.register.ItemsManager;

public class HpPlus extends Item
{

    private final static Properties p = new Properties()
            .stacksTo(64)
            .tab(ItemsManager.CONQUER_ITEM_GROUP)
            .food(new FoodProperties.Builder()
                    .nutrition(9)
                    .saturationMod(0.5f)
                    .alwaysEat()
                    .fast()
                    .effect(new MobEffectInstance(MobEffects.HEAL,3),1).build());

    public HpPlus()
    {
        super(p);
    }
}
