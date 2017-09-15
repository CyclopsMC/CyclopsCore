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
public class ConfigurableEnchantment extends Enchantment implements IConfigurable<EnchantmentConfig> {

    protected EnchantmentConfig eConfig = null;
    
    /**
     * Make a new Enchantment instance
     * @param eConfig Config for this enchantment.
     * @param rarity The rarity.
     * @param type The type of enchantment
     * @param slots The equipment types on which the enchantment can occur.
     */
    protected ConfigurableEnchantment(ExtendedConfig<EnchantmentConfig> eConfig, Enchantment.Rarity rarity,
                                      EnumEnchantmentType type, EntityEquipmentSlot[] slots) {
        super(rarity, type, slots);
        this.setConfig((EnchantmentConfig)eConfig); // TODO change eConfig to just be an EnchantmentConfig
        this.setName(eConfig.getUnlocalizedName());
    }
    
    private void setConfig(EnchantmentConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public EnchantmentConfig getConfig() {
        return eConfig;
    }
    
    @Override
    public String getTranslatedName(int level) {
        String enchantmentName = L10NHelpers.localize("enchantment." + eConfig.getMod().getModId() + "." + eConfig.getNamedId());
        return enchantmentName + " " + L10NHelpers.localize("enchantment.level." + level);
    }

}
