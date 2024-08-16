package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for enchantment entity effects.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class EnchantmentEntityEffectConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<EnchantmentEntityEffectConfigCommon<M>, MapCodec<? extends EnchantmentEntityEffect>, M> {

    public EnchantmentEntityEffectConfigCommon(M mod, String namedId, Function<EnchantmentEntityEffectConfigCommon<M>, MapCodec<? extends EnchantmentEntityEffect>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "enchantmententityeffect." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.ENCHANTMENT_ENTITY_EFFECT;
    }

    @Override
    public Registry<MapCodec<? extends EnchantmentEntityEffect>> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE;
    }
}
