package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for biome modifiers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeModifierConfig<T extends BiomeModifier, M extends ModBase> extends ExtendedConfigForge<BiomeModifierConfig<T, M>, MapCodec<T>, M> {

    public BiomeModifierConfig(M mod, String namedId, Function<BiomeModifierConfig<T, M>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.BIOME_MODIFIER;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS;
    }
}
