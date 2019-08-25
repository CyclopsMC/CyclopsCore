package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for enchantments.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class EnchantmentConfig extends ExtendedConfigForge<EnchantmentConfig, Enchantment> {
	
    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param elementConstructor The element constructor.
     */
    public EnchantmentConfig(ModBase mod, boolean enabledDefault, String namedId,
                             String comment, Function<EnchantmentConfig, ? extends Enchantment> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
    }

    @Override
	public String getTranslationKey() {
		return "enchantments." + getNamedId();
	}
    
    @Override
    public String getFullTranslationKey() {
        return "enchantment." + getMod().getModId() + "." + getNamedId();
    }

    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.ENCHANTMENT;
	}

    @Override
    public IForgeRegistry<Enchantment> getRegistry() {
        return ForgeRegistries.ENCHANTMENTS;
    }

}
