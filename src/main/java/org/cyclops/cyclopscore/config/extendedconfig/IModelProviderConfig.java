package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.item.ItemStack;

/**
 * Interface for indicating a config that can provide models.
 * @author rubensworks
 */
public interface IModelProviderConfig {

    /**
     * Get the model name for the given itemstack.
     * @param itemStack The item stack
     * @return The model name.
     */
    public String getModelName(ItemStack itemStack);

}
