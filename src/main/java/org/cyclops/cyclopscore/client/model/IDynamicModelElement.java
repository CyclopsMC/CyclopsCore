package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for blocks and items which can have a dynamic model.
 * @author rubensworks
 */
public interface IDynamicModelElement {

    /**
     * @return If this block has a dynamic model.
     */
    public boolean hasDynamicModel();

    /**
     * Should return not null if {@link IDynamicModelElement#hasDynamicModel()} is true.
     * This will only be called once.
     * @return A dynamic model instance.
     */
    @SideOnly(Side.CLIENT)
    public IBakedModel createDynamicModel();

}
