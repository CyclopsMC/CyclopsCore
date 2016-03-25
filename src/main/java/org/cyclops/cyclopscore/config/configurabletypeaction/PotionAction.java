package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.cyclops.cyclopscore.config.extendedconfig.PotionConfig;

/**
 * The action used for {@link PotionConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class PotionAction extends ConfigurableTypeAction<PotionConfig> {

    @Override
    public void preRun(PotionConfig eConfig, Configuration config, boolean startup) {
        // Get property in config file and set comment
        Property property = config.get(eConfig.getHolderType().getCategory(), eConfig.getNamedId(), eConfig.isEnabled());
        property.setRequiresMcRestart(true);
        property.comment = eConfig.getComment();

        if(startup) {
            // Update the ID, it could've changed
            eConfig.setEnabled(property.getBoolean());
        }
    }

    @Override
    public void postRun(PotionConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();

        // Register the potion
        Potion.potionRegistry.register(-1, new ResourceLocation(eConfig.getMod().getModId(), eConfig.getNamedId()), eConfig.getPotion());
    }

}
