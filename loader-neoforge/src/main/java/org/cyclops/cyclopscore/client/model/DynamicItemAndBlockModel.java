package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A dynamic model that can be used for items and blocks.
 * @author rubensworks
 */
public abstract class DynamicItemAndBlockModel extends DynamicBaseModel implements ResolvedModel {

    private final boolean factory;
    private final boolean item;

    private Direction renderingSide;

    public DynamicItemAndBlockModel(boolean factory, boolean item) {
        this.factory = factory;
        this.item = item;
    }

    protected boolean isItemStack() {
        return item;
    }

    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    public abstract List<BakedQuad> handleItemState(@Nullable ItemStack stack, @Nullable Level world,
                                                    @Nullable ItemOwner entity);

    public ModelRenderProperties getModelRenderProperties() {
        return new ModelRenderProperties(false, particleMaterial(), getTopTransforms());
    }

}
