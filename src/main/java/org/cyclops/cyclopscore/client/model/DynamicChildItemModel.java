package org.cyclops.cyclopscore.client.model;

import lombok.Getter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;

import java.util.List;

/**
 * A dynamic item model which can have a parent.
 * @author rubensworks
 */
public abstract class DynamicChildItemModel extends DynamicBaseModel implements ISmartItemModel {

    @Getter private final IBakedModel baseModel;

    public DynamicChildItemModel(IBakedModel baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getBaseModel().getParticleTexture();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing enumFacing) {
        return getBaseModel().getFaceQuads(enumFacing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        return getBaseModel().getGeneralQuads();
    }

}
