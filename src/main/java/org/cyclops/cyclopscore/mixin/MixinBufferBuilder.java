package org.cyclops.cyclopscore.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import org.cyclops.cyclopscore.client.particle.BufferBuilderWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BufferBuilder.class)
public class MixinBufferBuilder implements BufferBuilderWrapper {
    @Unique
    private Runnable cc$runnable = null;

    @Override
    public void cc$setRunnableOnBuild(Runnable runnable) {
        this.cc$runnable = runnable;
    }

    @Inject(method = "build", at = @At("RETURN"), cancellable = true)
    private void cc$clearIfRunnableSet(CallbackInfoReturnable<MeshData> cir) {
        if (cc$runnable != null) {
            // Flush the buffer manually, so we can ensure on callback is invoked to apply effects
            MeshData ret = cir.getReturnValue();
            if (ret != null) {
                BufferUploader.drawWithShader(ret);
            }
            this.cc$runnable.run();

            cir.setReturnValue(null);
        }
    }
}
