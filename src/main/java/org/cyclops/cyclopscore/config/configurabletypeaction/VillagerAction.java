package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.cyclops.cyclopscore.config.configurable.ConfigurableVillager;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class VillagerAction extends ConfigurableTypeAction<VillagerConfig>{

    @Override
    public void preRun(VillagerConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(),
                eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.setComment(eConfig.getComment());

        if(startup) {
            // Update the ID, it could've changed
            eConfig.setEnabled(property.getBoolean(true));
        }
    }

    @Override
    public void postRun(VillagerConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();

        // Register
        //register((ConfigurableVillager) eConfig.getSubInstance(), eConfig); // Don't call this, because VillagerRegistry.VillagerProfession already sets registry name
        GameRegistry.register((ConfigurableVillager) eConfig.getSubInstance());
    }

}
