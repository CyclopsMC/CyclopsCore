package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * The action used for {@link EntityConfig}.
 * @param <T> The entity type.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EntityAction<T extends Entity> extends ConfigurableTypeAction<EntityConfig<T>>{

    @Override
    public void preRun(EntityConfig<T> eConfig, Configuration config, boolean startup) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(EntityConfig<T> eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerModEntity(
                new ResourceLocation(eConfig.getMod().getModId(), eConfig.getSubUniqueName()),
                (Class<? extends Entity>) eConfig.getElement(),
                eConfig.getSubUniqueName(),
                Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY),
                eConfig.getMod(),
                eConfig.getTrackingRange(),
                eConfig.getUpdateFrequency(),
                eConfig.sendVelocityUpdates()
        );
    }

}
