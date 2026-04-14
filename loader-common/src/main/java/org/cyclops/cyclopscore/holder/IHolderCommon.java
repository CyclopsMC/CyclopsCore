package org.cyclops.cyclopscore.holder;

import net.minecraft.core.Holder;

/**
 * A modloader-agnostic variant of NeoForge's IHolderExtension.
 *
 * <p>Provides access to the delegate holder for wrapping holders like
 * {@link org.cyclops.cyclopscore.config.DeferredHolderCommon}.</p>
 *
 * <p>This interface is injected onto {@link Holder} via a mixin in each loader
 * so that {@link Holder} instances can be cast to {@link IHolderCommon} at runtime.</p>
 *
 * @param <T> The type held by the holder.
 * @author rubensworks
 */
public interface IHolderCommon<T> {

    /**
     * {@return the holder that this holder wraps, or {@code this} if it does not wrap another holder}
     *
     * <p>Used by {@code Registry#safeCastToReference} to resolve the underlying
     * {@link Holder.Reference} for delegating holders such as
     * {@link org.cyclops.cyclopscore.config.DeferredHolderCommon}.</p>
     */
    @SuppressWarnings("unchecked")
    default Holder<T> getDelegate() {
        return (Holder<T>) this;
    }

    /**
     * Helper that resolves the delegate of any {@link Holder}.
     *
     * <p>If {@code holder} implements {@link IHolderCommon}, {@link #getDelegate()} is called.
     * Otherwise {@code holder} itself is returned unchanged.</p>
     *
     * @param holder The holder to unwrap.
     * @param <T>    The element type.
     * @return The delegate holder, or {@code holder} itself when no delegation is present.
     */
    @SuppressWarnings("unchecked")
    static <T> Holder<T> getDelegate(Holder<T> holder) {
        if (holder instanceof IHolderCommon) {
            return ((IHolderCommon<T>) holder).getDelegate();
        }
        return holder;
    }
}
