package org.cyclops.cyclopscore.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
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
public abstract class RenderModel<T extends Entity, M extends ModelBase> extends Render<T> {
    
    private ResourceLocation texture;

    protected M model;
    
    /**
     * Make a new instance.
     * @param renderManager The render manager
     * @param config The config.
     */
    public RenderModel(RenderManager renderManager, ExtendedConfig<?> config) {
        super(renderManager);
        texture = createResourceLocation(config);
        model = constructModel();
    }

    protected ResourceLocation createResourceLocation(ExtendedConfig<?> config) {
        return new ResourceLocation(config.getMod().getModId(), config.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_MODELS) + config.getNamedId() + ".png");
    }
    
    protected abstract M constructModel();

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
    
}
