package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.resources.Identifier;
import org.cyclops.cyclopscore.client.model.IDynamicModelElementCommon;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 * @param <M> The mod type
 */
public class ItemClientConfig<M extends IModBase> {

    private final ItemConfigCommon<M> itemConfig;

    public Identifier dynamicItemVariantLocation;

    public ItemClientConfig(ItemConfigCommon<M> itemConfig) {
        this.itemConfig = itemConfig;
    }

    public ItemConfigCommon<M> getItemConfig() {
        return itemConfig;
    }

    /**
     * Register default block and item models for this block.
     * This should only be used when registering dynamic models.
     * @return The pair of block resource location and item resource location.
     */
    public Identifier registerDynamicModel() {
        return Identifier.fromNamespaceAndPath(getItemConfig().getMod().getModId(), getItemConfig().getNamedId());
    }

    /**
     * @return An optional dynamic model element
     */
    @Nullable
    public IDynamicModelElementCommon getDynamicModelElement() {
        return null;
    }
}
