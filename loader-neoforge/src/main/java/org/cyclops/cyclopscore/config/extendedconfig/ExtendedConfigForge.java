package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public abstract class ExtendedConfigForge<C extends ExtendedConfig<C, I>, I> extends ExtendedConfig<C, I> {
    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfigForge(ModBase<?> mod, String namedId, Function<C, ? extends I> elementConstructor) {
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
