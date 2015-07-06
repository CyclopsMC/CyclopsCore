package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * The action used for {@link EntityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EntityAction extends ConfigurableTypeAction<EntityConfig>{

    @Override
    public void preRun(EntityConfig eConfig, Configuration config, boolean startup) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(EntityConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        EntityRegistry.registerModEntity(
                eConfig.getElement(),
                eConfig.getSubUniqueName(),
                Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY),
                eConfig.getMod(),
                eConfig.getTrackingRange(),
                eConfig.getUpdateFrequency(),
                eConfig.sendVelocityUpdates()
        );
    }

}
