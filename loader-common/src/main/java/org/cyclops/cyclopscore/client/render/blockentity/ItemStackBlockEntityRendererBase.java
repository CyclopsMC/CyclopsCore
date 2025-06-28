package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A supplier-based {@link NoDataSpecialModelRenderer} that caches the internal block entity.
 * Don't use this if the block entity should be recreated at every render-tick.
 * @author rubensworks
 */
public class ItemStackBlockEntityRendererBase implements NoDataSpecialModelRenderer {

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final Supplier<BlockEntity> blockEntitySupplier;
    @Nullable
    private BlockEntity blockEntity;

    public ItemStackBlockEntityRendererBase(Supplier<BlockEntity> blockEntitySupplier) {
        this.blockEntityRenderDispatcher = Minecraft.getInstance().getBlockEntityRenderDispatcher();
        this.blockEntitySupplier = blockEntitySupplier;
    }

    @Override
    public void render(ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLightIn, int combinedOverlayIn, boolean b) {
        if (this.blockEntity == null) {
            this.blockEntity = this.blockEntitySupplier.get();
            this.blockEntity.setLevel(Minecraft.getInstance().level);
        }
        this.blockEntityRenderDispatcher.render(this.blockEntity, 0, poseStack, multiBufferSource);
    }

    @Override
    public void getExtents(Set<Vector3f> set) {

    }
}
