package org.cyclops.cyclopscore.client.model;

import lombok.Getter;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

/**
 * A model that can be used for {@link net.minecraftforge.common.property.IExtendedBlockState}s and items.
 * @author rubensworks
 */
public abstract class DynamicModel extends DynamicBaseModel implements ISmartBlockModel, ISmartItemModel {

    @Getter private final IExtendedBlockState state;
    @Getter private final boolean isItemStack;

    public DynamicModel(IExtendedBlockState state, boolean isItemStack) {
        this.state = state;
        this.isItemStack = isItemStack;
    }

    public DynamicModel() {
        this(null, false);
    }

    @Override
    public VertexFormat getFormat() {
        return Attributes.DEFAULT_BAKED_FORMAT;
    }
}
