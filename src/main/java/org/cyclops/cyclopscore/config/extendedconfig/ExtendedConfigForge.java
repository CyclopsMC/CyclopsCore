package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * An extended config for instances that are to be registered in a Forge registry.
 * @param <C> Class of the extension of ExtendedConfig
 * @param <I> The instance corresponding to this config.
 */
public abstract class ExtendedConfigForge<C extends ExtendedConfig<C, I>, I extends IForgeRegistryEntry<? super I>>
        extends ExtendedConfig<C, I> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId            A unique name id
     * @param comment            A comment that can be added to the config file line
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfigForge(ModBase mod, boolean enabledDefault, String namedId, String comment,
                               Function<C, ? extends I> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
    }

    /**
     * @return The registry in which this should be registered.
     */
    public abstract IForgeRegistry<? super I> getRegistry();

}
