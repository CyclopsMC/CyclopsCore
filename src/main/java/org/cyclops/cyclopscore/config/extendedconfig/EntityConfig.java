package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for entities.
 * @param <T> The entity type
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EntityConfig<T extends Entity> extends ExtendedConfigForge<EntityConfig<T>, EntityType<T>> {

    private final Class<? extends T> entityClass;

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     * @param entityClass The class of the entity.
     */
    public EntityConfig(ModBase mod, String namedId, Function<EntityConfig<T>, ? extends EntityType<T>> elementConstructor,
                        Class<? extends T> entityClass) {
        super(mod, namedId, elementConstructor);
        this.entityClass = entityClass;
    }
    
    @Override
	public String getTranslationKey() {
		return "entity." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.ENTITY;
	}

    public Class<? extends T> getEntityClass() {
        return entityClass;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onRegistered() {
        super.onRegistered();
        @SuppressWarnings("unchecked")
        Class<? extends T> clazz = this.getEntityClass();
        RenderingRegistry.registerEntityRenderingHandler(clazz,
                (IRenderFactory<T>) manager -> EntityConfig.this.getRender(manager, Minecraft.getInstance().getItemRenderer()));

    }
    
    /**
     * The range at which MC will send tracking updates.
     * @return The tracking range.
     */
    public int getTrackingRange() {
        return 160;
    }
    
    /**
     * The frequency of tracking updates.
     * @return The update frequency.
     */
    public int getUpdateFrequency() {
        return 10;
    }
    
    /**
     * Whether to send velocity information packets as well.
     * @return Send velocity updates?
     */
    public boolean sendVelocityUpdates() {
        return false;
    }

    /**
     * Get the render for this configurable.
     * @param renderManager The render manager.
     * @param renderItem The render item instance.
     * @return Get the render.
     */
    @OnlyIn(Dist.CLIENT)
    public abstract EntityRenderer<? super T> getRender(EntityRendererManager renderManager, ItemRenderer renderItem);

    @Override
    public IForgeRegistry<EntityType<?>> getRegistry() {
        return ForgeRegistries.ENTITIES;
    }
}
