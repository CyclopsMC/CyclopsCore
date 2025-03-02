package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ItemTintSourceConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * The action used for {@link ItemConfigCommon}.
 * @author rubensworks
 * @param <M> The mod type
 * @see ConfigurableTypeActionCommon
 */
public class ItemTintSourceActionCommon<T extends ItemTintSource, M extends IModBase> extends ConfigurableTypeActionCommon<ItemTintSourceConfigCommon<T, M>, MapCodec<T>, M> {
    @Override
    public void onRegistriesFilled(ItemTintSourceConfigCommon<T, M> eConfig) {
        super.onRegistriesFilled(eConfig);
        ItemTintSources.ID_MAPPER.put(ConfigHandlerCommon.getConfigId(eConfig), eConfig.getInstance());
    }
}
