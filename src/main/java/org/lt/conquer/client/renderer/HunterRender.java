package org.lt.conquer.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lt.conquer.Conquer;
import org.lt.conquer.client.model.HunterModel;
import org.lt.conquer.entities.HunterEntity;

public class HunterRender<Type extends HunterEntity> extends MobRenderer<Type, HunterModel<Type>>
{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(Conquer.MOD_ID, "textures/entities/hunter/hunter.png");

    public HunterRender(EntityRendererProvider.Context context)
    {
        super(context, new HunterModel<>(context.bakeLayer(HunterModel.LAYER_LOCATION),false), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(HunterEntity pEntity)
    {
        return TEXTURE;
    }
}
