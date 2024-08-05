package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for enchantment entity effects.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class EnchantmentEntityEffectConfig extends EnchantmentEntityEffectConfigCommon<ModBase<?>>{
    public EnchantmentEntityEffectConfig(ModBase<?> mod, String namedId, Function<EnchantmentEntityEffectConfigCommon<ModBase<?>>, MapCodec<? extends EnchantmentEntityEffect>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
