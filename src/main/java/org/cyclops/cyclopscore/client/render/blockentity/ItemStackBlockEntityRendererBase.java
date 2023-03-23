package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A supplier-based {@link BlockEntityWithoutLevelRenderer} that caches the internal block entity.
 * Don't use this if the block entity should be recreated at every render-tick.
 * @author rubensworks
 */
public class ItemStackBlockEntityRendererBase extends BlockEntityWithoutLevelRenderer {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final Supplier<BlockEntity> blockEntitySupplier;
    @Nullable
    private BlockEntity blockEntity;

    public ItemStackBlockEntityRendererBase(Supplier<BlockEntity> blockEntitySupplier) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        this.blockEntitySupplier = blockEntitySupplier;
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext itemDisplayContext, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (this.blockEntity == null) {
            this.blockEntity = this.blockEntitySupplier.get();
        }
        this.blockEntityRenderDispatcher.renderItem(this.blockEntity, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
