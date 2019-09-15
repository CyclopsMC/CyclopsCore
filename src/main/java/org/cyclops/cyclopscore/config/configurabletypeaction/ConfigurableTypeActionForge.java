package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.registries.IForgeRegistryEntry;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfigForge;

/**
 * The action used for {@link ExtendedConfigForge}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ConfigurableTypeActionForge<C extends ExtendedConfigForge<C, I>, I extends IForgeRegistryEntry<? super I>>
        extends ConfigurableTypeAction<C, I> {

    @Override
    public void onRegisterForge(C eConfig) {
        register(eConfig);
    }
}
