package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles list element expressions in the form of "[10]",
 * where the matched string represents the list element index that should be navigated in.
 */
public class NbtPathExpressionParseHandlerListElement implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_ELEMENTINDEX = Pattern.compile("^\\[([0-9]+)\\]");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_ELEMENTINDEX
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String childIndexString = matcher.group(1);
        int childIndex = Integer.parseInt(childIndexString);
        return new HandleResult(new Expression(childIndex), 2 + childIndexString.length());
    }

    public static class Expression implements INbtPathExpression {

        private final int childIndex;

        public Expression(int childIndex) {
            this.childIndex = childIndex;
        }

        int getChildIndex() {
            return childIndex;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        Tag nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Tag.TAG_LIST) {
                            ListTag tag = (ListTag) nbt;
                            if (childIndex < tag.size()) {
                                Tag childTag = tag.get(getChildIndex());
                                return new NbtPathExpressionExecutionContext(childTag, executionContext);
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
