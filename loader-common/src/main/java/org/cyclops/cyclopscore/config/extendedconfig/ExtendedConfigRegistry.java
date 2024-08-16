package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * An extended config for instances that are to be registered in a Forge registry.
 * @param <C> Class of the extension of ExtendedConfig
 * @param <I> The instance corresponding to this config.
 * @param <M> The mod type
 */
public abstract class ExtendedConfigRegistry<C extends ExtendedConfig<C, I, M>, I, M extends IModBase>
        extends ExtendedConfig<C, I, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfigRegistry(M mod, String namedId, Function<C, ? extends I> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    /**
     * @return The registry in which this should be registered.
     */
    public abstract Registry<? super I> getRegistry();

    public ResourceKey<? super I> getResourceKey() {
        return ResourceKey.create(getRegistry().key(),
                ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId()));
    }

}
