package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * A simple configurable for Enchantments, will auto-register itself after construction.
 * @author rubensworks
 *
 */
public class ConfigurableEnchantment extends Enchantment implements IConfigurable {

    protected ExtendedConfig<EnchantmentConfig, Enchantment> eConfig = null;
    
    /**
     * Make a new Enchantment instance
     * @param eConfig Config for this enchantment.
     * @param rarity The rarity.
     * @param type The type of enchantment
     * @param slots The equipment types on which the enchantment can occur.
     */
    protected ConfigurableEnchantment(ExtendedConfig<EnchantmentConfig, Enchantment> eConfig, Enchantment.Rarity rarity,
                                      EnumEnchantmentType type, EntityEquipmentSlot[] slots) {
        super(rarity, type, slots);
        this.setConfig(eConfig);
        this.setName(eConfig.getUnlocalizedName());
    }
    
    private void setConfig(ExtendedConfig<EnchantmentConfig, Enchantment> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<EnchantmentConfig, Enchantment> getConfig() {
        return eConfig;
    }
    
    @Override
    public String getTranslatedName(int level) {
        String enchantmentName = L10NHelpers.localize("enchantment." + eConfig.getMod().getModId() + "." + eConfig.downCast().getNamedId());
        return enchantmentName + " " + L10NHelpers.localize("enchantment.level." + level);
    }

}
