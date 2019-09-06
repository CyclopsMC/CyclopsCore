package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.enchantment.Enchantment;
import org.cyclops.cyclopscore.config.extendedconfig.EnchantmentConfig;

/**
 * The action used for {@link EnchantmentConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EnchantmentAction extends ConfigurableTypeAction<EnchantmentConfig, Enchantment> {

    @Override
    public void onRegisterForge(EnchantmentConfig eConfig) {
        register(eConfig.getInstance(), eConfig);
    }

}
