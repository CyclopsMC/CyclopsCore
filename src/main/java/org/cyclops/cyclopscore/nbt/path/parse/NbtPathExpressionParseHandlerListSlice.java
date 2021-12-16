package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A handler that handles list slice expressions in the form of "[start:end:step]" such as "[2:3:2]".
 */
public class NbtPathExpressionParseHandlerListSlice implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_RANGE = Pattern.compile("^\\[([0-9]*):([0-9]*)(:([0-9]+))?\\]");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_RANGE
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String startString = matcher.group(1);
        String endString = matcher.group(2);
        String stepString = matcher.groupCount() >= 4 ? matcher.group(4) : null;
        int start = !startString.isEmpty() ? Integer.parseInt(startString) : 0;
        int end = !endString.isEmpty() ? Integer.parseInt(endString) : -1;
        int step = stepString != null ? Integer.parseInt(matcher.group(4)) : 1;
        if (step == 0) {
            return HandleResult.INVALID;
        }

        return new HandleResult(new Expression(start, end, step),
                3 + startString.length() + endString.length() + (stepString == null ? 0 : 1 + stepString.length()));
    }

    public static IntStream newStartEndStepStream(int start, int end, int step) {
        end -= start - 1;
        int endScaled = end / step;
        int endMod = (end % step) > 0 ? 1 : 0;
        return IntStream.range(0, endScaled + endMod).map(i -> i * step + start);
    }

    public static class Expression implements INbtPathExpression {

        private final int start;
        private final int end;
        private final int step;

        public Expression(int start, int end, int step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getStep() {
            return step;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .flatMap(executionContext -> {
                        Tag nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Tag.TAG_LIST) {
                            ListTag tag = (ListTag) nbt;
                            int start = getStart();
                            int actualEnd = getEnd() > -1 ? Math.min(tag.size() - 1, getEnd()) : tag.size() - 1;
                            int step = getStep();
                            return NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(start, actualEnd, step)
                                    .mapToObj(i -> new NbtPathExpressionExecutionContext(tag.get(i), executionContext));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
