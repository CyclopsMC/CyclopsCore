package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A handler that handles child path expressions for ".length",
 * to retrieve the length of lists and tag compounds.
 */
public class NbtPathExpressionParseHandlerLength implements INbtPathExpressionParseHandler {

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (!nbtPathExpression.substring(pos, Math.min(pos + 7, nbtPathExpression.length())).equals(".length")) {
            return HandleResult.INVALID;
        }

        return new HandleResult(Expression.INSTANCE, 7);
    }

    public static class Expression implements INbtPathExpression {

        public static final NbtPathExpressionParseHandlerLength.Expression INSTANCE = new NbtPathExpressionParseHandlerLength.Expression();

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Tag.TAG_LIST) {
                            ListTag tag = (ListTag) nbt;
                            return new NbtPathExpressionExecutionContext(IntTag.valueOf(tag.size()), executionContext);
                        } else if (nbt.getId() == Tag.TAG_COMPOUND) {
                            CompoundTag tag = (CompoundTag) nbt;
                            return new NbtPathExpressionExecutionContext(IntTag.valueOf(tag.getAllKeys().size()), executionContext);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
