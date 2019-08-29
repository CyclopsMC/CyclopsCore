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
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public EnchantmentConfig(ModBase mod, String namedId, Function<EnchantmentConfig, ? extends Enchantment> elementConstructor) {
        super(mod, namedId, elementConstructor);
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
