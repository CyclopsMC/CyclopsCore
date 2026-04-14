package org.cyclops.cyclopscore.mixin;

import com.mojang.serialization.DataResult;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.cyclops.cyclopscore.holder.IHolderCommon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Injects into {@link Registry#safeCastToReference} to unwrap delegating holders
 * (such as {@link org.cyclops.cyclopscore.config.DeferredHolderCommon}) via
 * {@link IHolderCommon#getDelegate(Holder)} before the {@link Holder.Reference} cast.
 *
 * <p>Without this injection, a delegating holder would never pass the
 * {@code instanceof Holder.Reference} check and serialisation would always fail for
 * objects registered through a {@code DeferredHolderCommon}.</p>
 *
 * @author rubensworks
 */
@Mixin(Registry.class)
public interface MixinRegistry<T> {

    @Shadow
    ResourceKey<? extends Registry<T>> key();

    @SuppressWarnings("unchecked")
    @Inject(method = "safeCastToReference", at = @At("HEAD"), cancellable = true)
    private void cyclopscore$safeCastToReference(
            Holder<T> holder,
            CallbackInfoReturnable<DataResult<Holder.Reference<T>>> cir) {
        Holder<T> delegate = IHolderCommon.getDelegate(holder);
        if (delegate instanceof Holder.Reference) {
            cir.setReturnValue(DataResult.success((Holder.Reference<T>) delegate));
        } else {
            cir.setReturnValue(DataResult.error(() -> "Unregistered holder in " + this.key() + ": " + holder));
        }
    }
}
