package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A handler that handles follows all child links of a tag.
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
                        NBTBase nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == 9) {
                            NBTTagList tag = (NBTTagList) nbt;
                            return StreamSupport.stream(tag.spliterator(), false)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        } else if (nbt.getId() == 10) {
                            NBTTagCompound tag = (NBTTagCompound) nbt;
                            return tag.getKeySet().stream()
                                    .map((key) -> new NbtPathExpressionExecutionContext(tag.getTag(key), executionContext));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
