package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
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
    public void getExtents(Set<Vector3f> set) {

    }

    @Override
    public void submit(ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, int i1, boolean b, int i2) {
        if (this.blockEntity == null) {
            this.blockEntity = this.blockEntitySupplier.get();
            this.blockEntity.setLevel(Minecraft.getInstance().level);
        }
        BlockEntityRenderer<BlockEntity, BlockEntityRenderState> renderer = this.blockEntityRenderDispatcher.getRenderer(this.blockEntity);
        if (renderer != null) {
            BlockEntityRenderState renderState = renderer.createRenderState();
            renderer.extractRenderState(blockEntity, renderState, 0, Vec3.ZERO, null);
            renderer.submit(renderState, poseStack, submitNodeCollector, new CameraRenderState());
        }
    }
}
