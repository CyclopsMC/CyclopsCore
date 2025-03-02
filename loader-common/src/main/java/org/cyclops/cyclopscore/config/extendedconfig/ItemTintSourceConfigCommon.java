package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for item tint sources.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class ItemTintSourceConfigCommon<T extends ItemTintSource, M extends IModBase> extends ExtendedConfigCommon<ItemTintSourceConfigCommon<T, M>, MapCodec<T>, M> {

    public ItemTintSourceConfigCommon(M mod, String namedId, Function<ItemTintSourceConfigCommon<T, M>, MapCodec<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "itemtintsource." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.ITEM_TINT_SOURCE;
    }
}
