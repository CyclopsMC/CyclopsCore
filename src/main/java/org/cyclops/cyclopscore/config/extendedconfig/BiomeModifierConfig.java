package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for biome modifiers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BiomeModifierConfig<T extends BiomeModifier> extends ExtendedConfigForge<BiomeModifierConfig<T>, Codec<T>>{

    public BiomeModifierConfig(ModBase mod, String namedId, Function<BiomeModifierConfig<T>, Codec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.BIOME_MODIFIER;
    }

    @Override
    public Registry<? super Codec<T>> getRegistry() {
        return NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS;
    }
}
