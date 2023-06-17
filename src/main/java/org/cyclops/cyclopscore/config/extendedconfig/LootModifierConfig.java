package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for loot modifiers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class LootModifierConfig<T extends IGlobalLootModifier> extends ExtendedConfigForge<LootModifierConfig<T>, Codec<T>>{

    public LootModifierConfig(ModBase mod, String namedId, Function<LootModifierConfig<T>, Codec<T>> elementConstructor) {
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
    public IForgeRegistry<Codec<? extends IGlobalLootModifier>> getRegistry() {
        return ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get();
    }

}
