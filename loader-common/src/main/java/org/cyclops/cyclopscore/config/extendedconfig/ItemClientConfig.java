package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.IModBase;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 * @param <M> The mod type
 */
public class ItemClientConfig<M extends IModBase> {

    private final ItemConfigCommon<M> itemConfig;

    public ModelResourceLocation dynamicItemVariantLocation;

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
    public ModelResourceLocation registerDynamicModel() {
        ResourceLocation itemName = ResourceLocation.fromNamespaceAndPath(getItemConfig().getMod().getModId(), getItemConfig().getNamedId());
        return new ModelResourceLocation(itemName, "inventory");
    }

    /**
     * @return An optional color handler for the block instance.
     */
    @Nullable
    public ItemColor getItemColorHandler() {
        return null;
    }
}
