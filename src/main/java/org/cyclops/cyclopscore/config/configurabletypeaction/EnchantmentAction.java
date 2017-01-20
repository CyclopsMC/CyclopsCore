package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;

/**
 * The action used for {@link EnchantmentConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EnchantmentAction extends ConfigurableTypeAction<EnchantmentConfig>{

    @Override
    public void preRun(EnchantmentConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.setComment(eConfig.getComment());
        property.setLanguageKey(eConfig.getFullUnlocalizedName());

        if(startup) {
	        // Update the ID, it could've changed
	        eConfig.setEnabled(property.getBoolean());
        }
    }

    @Override
    public void postRun(EnchantmentConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();

        // Register the enchantment
        register(eConfig.getEnchantment(), eConfig);
    }

}
