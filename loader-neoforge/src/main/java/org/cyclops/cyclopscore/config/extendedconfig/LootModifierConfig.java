package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for loot modifiers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class LootModifierConfig<T extends IGlobalLootModifier> extends ExtendedConfigForge<LootModifierConfig<T>, MapCodec<T>> {

    public LootModifierConfig(ModBase<?> mod, String namedId, Function<LootModifierConfig<T>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.LOOT_MODIFIER;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS;
    }
}
