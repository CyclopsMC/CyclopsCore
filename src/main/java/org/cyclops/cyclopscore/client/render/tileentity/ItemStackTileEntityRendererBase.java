package org.cyclops.cyclopscore.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A supplier-based {@link ItemStackTileEntityRenderer} that caches the internal tile entity.
 * Don't use this if the tile entity should be recreated at every render-tick.
 * @author rubensworks
 */
public class ItemStackTileEntityRendererBase extends ItemStackTileEntityRenderer {

    private final Supplier<TileEntity> tileEntitySupplier;
    @Nullable
    private TileEntity tileEntity;

    public ItemStackTileEntityRendererBase(Supplier<TileEntity> tileEntitySupplier) {
        this.tileEntitySupplier = tileEntitySupplier;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (this.tileEntity == null) {
            this.tileEntity = this.tileEntitySupplier.get();
        }
        TileEntityRendererDispatcher.instance.renderItem(this.tileEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
