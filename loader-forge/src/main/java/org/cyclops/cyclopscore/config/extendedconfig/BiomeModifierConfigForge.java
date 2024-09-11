package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

import java.util.function.Function;

/**
 * Config for biome modifiers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class BiomeModifierConfigForge<T extends BiomeModifier> extends ExtendedConfigCommon<BiomeModifierConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {

    public BiomeModifierConfigForge(ModBaseForge<?> mod, String namedId, Function<BiomeModifierConfigForge<T>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesForge.BIOME_MODIFIER;
    }

    public IForgeRegistry<? super MapCodec<T>> getRegistryForge() {
        return ForgeRegistries.BIOME_MODIFIER_SERIALIZERS.get();
    }
}
