package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 * @param <M> The mod type
 */
public abstract class EntityClientConfig<M extends IModBase, T extends Entity> {

    private final EntityConfigCommon<M, T> entityConfig;

    public EntityClientConfig(EntityConfigCommon<M, T> entityConfig) {
        this.entityConfig = entityConfig;
    }

    public EntityConfigCommon<M, T> getEntityConfig() {
        return entityConfig;
    }

    /**
     * Get the render for this configurable.
     * @param renderContext The render context.
     * @param renderItem The render item instance.
     * @return Get the render.
     */
    public abstract EntityRenderer<? super T> getRender(EntityRendererProvider.Context renderContext, ItemRenderer renderItem);
}
