package org.cyclops.cyclopscore.mixin;

import net.minecraft.core.Holder;
import org.cyclops.cyclopscore.holder.IHolderCommon;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Injects {@link IHolderCommon} into {@link Holder} so that all Holder implementations
 * can be used with {@link IHolderCommon#getDelegate(Holder)} at runtime.
 *
 * <p>The default implementation of {@link IHolderCommon#getDelegate()} returns {@code this},
 * which is the correct behaviour for plain {@link Holder.Reference} and {@link Holder.Direct}
 * instances. {@link org.cyclops.cyclopscore.config.DeferredHolderCommon} overrides this to
 * return the bound inner holder.</p>
 *
 * @author rubensworks
 */
@Mixin(Holder.class)
@SuppressWarnings("rawtypes")
public interface MixinHolder extends IHolderCommon {
}
