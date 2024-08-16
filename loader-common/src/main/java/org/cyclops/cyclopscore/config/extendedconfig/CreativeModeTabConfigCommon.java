package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for creative mode tabs.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class CreativeModeTabConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<CreativeModeTabConfigCommon<M>, CreativeModeTab, M> {

    public CreativeModeTabConfigCommon(M mod, String namedId, Function<CreativeModeTabConfigCommon<M>, CreativeModeTab> elementConstructor) {
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

    @Override
    public Registry<? super CreativeModeTab> getRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }
}
