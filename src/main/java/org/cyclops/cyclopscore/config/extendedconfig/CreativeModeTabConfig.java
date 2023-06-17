package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for creative mode tabs.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class CreativeModeTabConfig extends ExtendedConfig<CreativeModeTabConfig, CreativeModeTab>{

    public CreativeModeTabConfig(ModBase mod, String namedId, Function<CreativeModeTabConfig, CreativeModeTab> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "creativemodetab." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.CREATIVE_MODE_TAB;
    }

}
