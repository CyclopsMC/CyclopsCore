package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * An abstract handler that handles boolean relational expressions.
 */
public abstract class NbtPathExpressionParseHandlerBooleanRelationalAdapter implements INbtPathExpressionParseHandler {

    private final Pattern regex;

    protected NbtPathExpressionParseHandlerBooleanRelationalAdapter(String relation) {
        this.regex = Pattern.compile("^ *" + relation + " *([0-9]+(.[0-9]+)?)");
    }

    protected abstract boolean getRelationalValue(double left, double right);

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = this.regex
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String targetDoubleString = matcher.group(1);
        double targetDouble = Double.parseDouble(targetDoubleString);
        return new HandleResult(new Expression(targetDouble, this), matcher.group().length());
    }

    public static class Expression implements INbtPathExpression {

        private final double targetDouble;
        private final NbtPathExpressionParseHandlerBooleanRelationalAdapter handler;

        public Expression(double targetDouble, NbtPathExpressionParseHandlerBooleanRelationalAdapter handler) {
            this.targetDouble = targetDouble;
            this.handler = handler;
        }

        double getTargetDouble() {
            return targetDouble;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        NBTBase nbt = executionContext.getCurrentTag();
                        if (nbt instanceof NBTPrimitive) {
                            NBTPrimitive tag = (NBTPrimitive) nbt;
                            return new NbtPathExpressionExecutionContext(
                                    new NBTTagByte(this.handler.getRelationalValue(tag.getDouble(), getTargetDouble())
                                            ? (byte) 1 : (byte) 0), executionContext);
                        }
                        return new NbtPathExpressionExecutionContext(new NBTTagByte((byte) 0), executionContext);
                    })
            );
        }

    }
}
