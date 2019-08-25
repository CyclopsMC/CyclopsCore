package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.INBT;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * A context that is passed during the NBT path execution.
 */
public class NbtPathExpressionExecutionContext {

    private final INBT currentTag;
    @Nullable
    private final NbtPathExpressionExecutionContext parentContext;

    public NbtPathExpressionExecutionContext(INBT currentTag,
                                             @Nullable NbtPathExpressionExecutionContext parentContext) {
        this.currentTag = currentTag;
        this.parentContext = parentContext;
    }

    public NbtPathExpressionExecutionContext(INBT currentTag) {
        this(currentTag, null);
    }

    public INBT getCurrentTag() {
        return currentTag;
    }

    @Nullable
    public NbtPathExpressionExecutionContext getParentContext() {
        return parentContext;
    }

    public NbtPathExpressionExecutionContext getRootContext() {
        if (this.getParentContext() == null) {
            return this;
        } else {
            return this.getParentContext().getRootContext();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NbtPathExpressionExecutionContext)) {
            return false;
        }
        NbtPathExpressionExecutionContext that = (NbtPathExpressionExecutionContext) obj;
        return this.getCurrentTag().equals(that.getCurrentTag())
                && Objects.equals(this.getParentContext(), that.getParentContext());
    }

    @Override
    public int hashCode() {
        return getCurrentTag().hashCode() + Objects.hashCode(getParentContext());
    }
}
