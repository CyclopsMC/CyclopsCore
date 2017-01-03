package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.enchantment.Enchantment;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableEnchantment;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for enchantments.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EnchantmentConfig extends ExtendedConfig<EnchantmentConfig>{
	
    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param defaultId The default ID for the configurable.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public EnchantmentConfig(ModBase mod, int defaultId, String namedId,
            String comment, Class<? extends Enchantment> element) {
        super(mod, defaultId != 0, namedId, comment, element);
    }
    
    @Override
	public String getUnlocalizedName() {
		return "enchantments." + getNamedId();
	}
    
    @Override
    public String getFullUnlocalizedName() {
        return "enchantment." + getMod().getModId() + "." + getNamedId();
    }

    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.ENCHANTMENT;
	}

    /**
     * Get the enchantment configurable
     * @return The enchantment.
     */
    public ConfigurableEnchantment getEnchantment() {
        return (ConfigurableEnchantment) this.getSubInstance();
    }

}
