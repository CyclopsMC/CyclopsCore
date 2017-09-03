package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurablePotion;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for potions.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class PotionConfig extends ExtendedConfig<PotionConfig, Potion> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param defaultId The default ID for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public PotionConfig(ModBase mod, int defaultId, String namedId,
                             String comment, Class<? extends Potion> element) {
        super(mod, defaultId != 0, namedId, comment, element);
    }

    @Override
    public String getUnlocalizedName() {
        return "potions." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getHolderType() {
        return ConfigurableType.POTION;
    }

    /**
     * Get the potion configurable
     * @return The potion.
     */
    public ConfigurablePotion getPotion() {
        return (ConfigurablePotion) this.getSubInstance();
    }

    @Override
    public IForgeRegistry<?> getRegistry() {
        return ForgeRegistries.POTIONS;
    }

}
