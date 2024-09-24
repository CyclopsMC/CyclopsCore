package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

import java.util.function.Function;

/**
 * Config for loot modifiers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class LootModifierConfigForge<T extends IGlobalLootModifier> extends ExtendedConfigCommon<LootModifierConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {

    public LootModifierConfigForge(ModBaseForge<?> mod, String namedId, Function<LootModifierConfigForge<T>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "biomemodifier." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesForge.LOOT_MODIFIER;
    }

    public IForgeRegistry<? super MapCodec<T>> getRegistryForge() {
        return ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get();
    }
}
