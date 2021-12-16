package org.cyclops.cyclopscore.client.render.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * A renderer for a custom model.
 * It will automatically take care of the texture.
 * @author rubensworks
 *
 * @param <T> The entity type
 * @param <M> The model that will be rendered.
 */
public abstract class RenderModel<T extends Entity, M extends Model> extends EntityRenderer<T> {

    private ResourceLocation texture;

    protected M model;

    /**
     * Make a new instance.
     * @param renderContext The render context
     * @param config The config.
     */
    public RenderModel(EntityRendererProvider.Context renderContext, ExtendedConfig<?, ?> config) {
        super(renderContext);
        texture = createResourceLocation(config);
        model = constructModel();
    }

    protected ResourceLocation createResourceLocation(ExtendedConfig<?, ?> config) {
        return new ResourceLocation(config.getMod().getModId(), config.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_MODELS) + config.getNamedId() + ".png");
    }

    protected abstract M constructModel();

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return texture;
    }

}
