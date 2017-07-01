package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;

/**
 * The action used for {@link BiomeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BiomeAction extends ConfigurableTypeAction<BiomeConfig>{

    @Override
    public void preRun(BiomeConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.isEnabled());
        property.setRequiresWorldRestart(true);
        property.setRequiresMcRestart(true);
        property.setComment(eConfig.getComment());
        property.setLanguageKey(eConfig.getFullUnlocalizedName());

        if(startup) {
            // Update the ID, it could've changed
            eConfig.setEnabled(property.getBoolean());
        }
    }

    @Override
    public void postRun(BiomeConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register biome
        register(eConfig.getBiome(), eConfig, () -> {
            eConfig.registerBiomeDictionary();
            return null;
        });
    }
}
