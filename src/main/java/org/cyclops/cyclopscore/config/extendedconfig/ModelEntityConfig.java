package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.cyclops.cyclopscore.client.render.model.RenderModel;
import org.cyclops.cyclopscore.init.ModBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Config for Entities with a custom Model.
 * @param <T> The entity type
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ModelEntityConfig<T extends Entity> extends EntityConfig<T>{

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ModelEntityConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Entity> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
    public Render<? super T> getRender(RenderManager renderManager, RenderItem renderItem) {
        Constructor<? extends Render<T>> constructor;
        Render<T> render = null;
        try {
            constructor = getRenderClass().getConstructor(ExtendedConfig.class);
            render = constructor.newInstance(this);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalArgumentException |
                IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return render;
    }
    
    /**
     * Get the {@link RenderModel} class for the configurable.
     * @return The class for the model of the configurable.
     */
    public abstract Class<? extends RenderModel<T, ?>> getRenderClass();
}
