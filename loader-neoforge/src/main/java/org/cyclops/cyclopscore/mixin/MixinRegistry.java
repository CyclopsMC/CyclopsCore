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
 * <p>NeoForge already patches {@code Registry#safeCastToReference} to call
 * {@code holder.getDelegate()} via {@code IHolderExtension}. This mixin replaces that
 * call with {@link IHolderCommon#getDelegate(Holder)} so that all loaders use the
 * same modloader-agnostic unwrapping path.</p>
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
        }
    }
}
