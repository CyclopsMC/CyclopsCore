package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationLeafWildcard;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationLinkWildcard;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A handler that handles follows all child links of a tag via "*".
 */
public class NbtPathExpressionParseHandlerAllChildren implements INbtPathExpressionParseHandler {

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (nbtPathExpression.charAt(pos) != '*') {
            return HandleResult.INVALID;
        }

        return new HandleResult(NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE, 1);
    }

    public static class Expression implements INbtPathExpression {

        public static final NbtPathExpressionParseHandlerAllChildren.Expression INSTANCE = new NbtPathExpressionParseHandlerAllChildren.Expression();

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .flatMap(executionContext -> {
                        Tag nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Tag.TAG_LIST) {
                            ListTag tag = (ListTag) nbt;
                            return StreamSupport.stream(tag.spliterator(), false)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        } else if (nbt.getId() == Tag.TAG_COMPOUND) {
                            CompoundTag tag = (CompoundTag) nbt;
                            return tag.getAllKeys().stream()
                                    .map((key) -> new NbtPathExpressionExecutionContext(tag.get(key), executionContext));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

        @Override
        public INbtPathNavigation asNavigation(@Nullable INbtPathNavigation child) {
            return child == null ? NbtPathNavigationLeafWildcard.INSTANCE : new NbtPathNavigationLinkWildcard(child);
        }
    }
}
