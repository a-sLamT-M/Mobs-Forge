package org.lt.conquer.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lt.conquer.Conquer;
import org.lt.conquer.client.model.HunterModel;
import org.lt.conquer.entities.AbstractHunter;
import org.lt.conquer.entities.RedHunterEntity;

public class RedHunterRender<Type extends AbstractHunter> extends MobRenderer<Type, HunterModel<Type>>
{
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(Conquer.MOD_ID, "textures/entities/hunter/steve_red_png.png");

    public RedHunterRender(EntityRendererProvider.Context context)
    {
        super(context, new HunterModel<>(context.bakeLayer(HunterModel.LAYER_LOCATION),false), 0.6F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractHunter pEntity)
    {
        return TEXTURE;
    }
}
