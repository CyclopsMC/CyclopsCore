package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.function.Function;

/**
 * Config for biome modifiers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class BiomeModifierConfigNeoForge<T extends BiomeModifier> extends ExtendedConfigRegistry<BiomeModifierConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

    public BiomeModifierConfigNeoForge(ModBaseNeoForge<?> mod, String namedId, Function<BiomeModifierConfigNeoForge<T>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesNeoForge.BIOME_MODIFIER;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS;
    }
}
