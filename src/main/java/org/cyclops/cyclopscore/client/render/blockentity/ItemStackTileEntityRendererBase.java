package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A supplier-based {@link BlockEntityWithoutLevelRenderer} that caches the internal tile entity.
 * Don't use this if the tile entity should be recreated at every render-tick.
 * @author rubensworks
 */
public class ItemStackTileEntityRendererBase extends BlockEntityWithoutLevelRenderer {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final Supplier<BlockEntity> tileEntitySupplier;
    @Nullable
    private BlockEntity tileEntity;

    public ItemStackTileEntityRendererBase(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet, Supplier<BlockEntity> tileEntitySupplier) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        this.tileEntitySupplier = tileEntitySupplier;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemTransforms.TransformType transformType, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (this.tileEntity == null) {
            this.tileEntity = this.tileEntitySupplier.get();
        }
        this.blockEntityRenderDispatcher.renderItem(this.tileEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
