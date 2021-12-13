package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
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
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfigForge(ModBase mod, String namedId, Function<C, ? extends I> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    /**
     * @return The registry in which this should be registered.
     */
    public abstract IForgeRegistry<? super I> getRegistry();

    public RegistryKey<I> getRegistryKey() {
        return RegistryKey.create(RegistryKey.createRegistryKey(getRegistry().getRegistryName()),
                new ResourceLocation(getMod().getModId(), getNamedId()));
    }

}
