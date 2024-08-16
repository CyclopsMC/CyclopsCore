package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for enchantment entity effects.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class EnchantmentEntityEffectConfig extends ExtendedConfigForge<EnchantmentEntityEffectConfig, MapCodec<? extends EnchantmentEntityEffect>>{
    public EnchantmentEntityEffectConfig(ModBase<?> mod, String namedId, Function<EnchantmentEntityEffectConfig, MapCodec<? extends EnchantmentEntityEffect>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "enchantmententityeffect." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_ENCHANTMENT_ENTITY_EFFECT;
    }

    @Override
    public Registry<MapCodec<? extends EnchantmentEntityEffect>> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE;
    }
}
