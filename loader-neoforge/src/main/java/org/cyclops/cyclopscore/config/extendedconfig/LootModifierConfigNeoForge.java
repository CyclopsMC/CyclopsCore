package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.function.Function;

/**
 * Config for loot modifiers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class LootModifierConfigNeoForge<T extends IGlobalLootModifier> extends ExtendedConfigRegistry<LootModifierConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

    public LootModifierConfigNeoForge(ModBaseNeoForge<?> mod, String namedId, Function<LootModifierConfigNeoForge<T>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesNeoForge.LOOT_MODIFIER;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS;
    }
}
