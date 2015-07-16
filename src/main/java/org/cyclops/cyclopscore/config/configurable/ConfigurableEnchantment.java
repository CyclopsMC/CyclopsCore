package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * A simple configurable for Enchantments, will auto-register itself after construction.
 * @author rubensworks
 *
 */
public class ConfigurableEnchantment extends Enchantment implements IConfigurable {

    protected ExtendedConfig<EnchantmentConfig> eConfig = null;
    
    /**
     * Make a new Enchantment instance
     * @param eConfig Config for this enchantment.
     * @param weight The weight in which this enchantment should occurd
     * @param type The type of enchantment
     */
    protected ConfigurableEnchantment(ExtendedConfig<EnchantmentConfig> eConfig, int weight,
                                      EnumEnchantmentType type) {
        super(eConfig.downCast().ID, new ResourceLocation(eConfig.getNamedId()), weight, type);
        this.setConfig(eConfig);
        this.setName(eConfig.getUnlocalizedName());
        if(isAllowedOnBooks()) {
            addToBookList(this);
        }
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }
    
    @Override
    public String getTranslatedName(int level) {
        String enchantmentName = L10NHelpers.localize("enchantment." + eConfig.getMod().getModId() + "." + eConfig.downCast().getNamedId());
        return enchantmentName + " " + L10NHelpers.localize("enchantment.level." + level);
    }

}
