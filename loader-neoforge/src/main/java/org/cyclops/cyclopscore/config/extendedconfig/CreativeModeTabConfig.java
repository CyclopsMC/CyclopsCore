package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for creative mode tabs.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class CreativeModeTabConfig extends ExtendedConfigForge<CreativeModeTabConfig, CreativeModeTab>{
    public CreativeModeTabConfig(ModBase<?> mod, String namedId, Function<CreativeModeTabConfig, CreativeModeTab> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "creativemodetab." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_CREATIVE_MODE_TAB;
    }

    @Override
    public Registry<? super CreativeModeTab> getRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }
}
