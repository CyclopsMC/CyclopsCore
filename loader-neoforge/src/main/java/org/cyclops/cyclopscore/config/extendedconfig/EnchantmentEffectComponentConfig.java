package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.component.DataComponentType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.UnaryOperator;

/**
 * Config for enchantment effect components.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class EnchantmentEffectComponentConfig<T> extends EnchantmentEffectComponentConfigCommon<T, ModBase<?>>{
    public EnchantmentEffectComponentConfig(ModBase<?> mod, String namedId, UnaryOperator<DataComponentType.Builder<T>> builder) {
        super(mod, namedId, builder);

    }
}
