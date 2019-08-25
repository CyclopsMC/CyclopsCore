package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;
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
                        INBT nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Constants.NBT.TAG_LIST) {
                            ListNBT tag = (ListNBT) nbt;
                            return new NbtPathExpressionExecutionContext(new IntNBT(tag.size()), executionContext);
                        } else if (nbt.getId() == Constants.NBT.TAG_COMPOUND) {
                            CompoundNBT tag = (CompoundNBT) nbt;
                            return new NbtPathExpressionExecutionContext(new IntNBT(tag.keySet().size()), executionContext);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
