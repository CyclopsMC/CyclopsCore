package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.NBTBase;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * A handler that picks the root of the NBT tree.
 */
public class NbtPathExpressionParseHandlerRoot implements INbtPathExpressionParseHandler {

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (nbtPathExpression.charAt(pos) != '$') {
            return HandleResult.INVALID;
        }

        return new HandleResult(Expression.INSTANCE, 1);
    }

    public static class Expression implements INbtPathExpression {

        public static final Expression INSTANCE = new Expression();

        @Override
        public NbtPathExpressionMatches match(Stream<NBTBase> nbt) {
            return new NbtPathExpressionMatches(nbt);
        }
    }
}
