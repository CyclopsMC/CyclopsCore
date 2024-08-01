package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationAdapter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles union expressions in the form of "[10,12]" or "[10,]" or "[,12]",
 * where the matches indicate the children or list elements that should be matched.
 */
public class NbtPathExpressionParseHandlerUnion implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_BRACKETS = Pattern.compile("^\\[([^\\]]+(,[^\\]]+)+)]");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_BRACKETS
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String[] contents = matcher.group(1).split(",");

        List<String> childNames = Lists.newArrayList();
        List<Integer> childIndexes = Lists.newArrayList();
        int expressionLength = 1;
        for (String match : contents) {
            expressionLength += 1 + match.length();
            try {
                childIndexes.add(Integer.parseInt(match));
                if (!childNames.isEmpty()) {
                    return HandleResult.INVALID;
                }
            } catch (NumberFormatException e) {
                childNames.add(match);
                if (!childIndexes.isEmpty()) {
                    return HandleResult.INVALID;
                }
            }
        }
        return new HandleResult(new Expression(childNames, childIndexes), expressionLength);
    }

    public static class Expression implements INbtPathExpression {

        private final List<String> childNames;
        private final List<Integer> childIndexes;

        public Expression(List<String> childNames, List<Integer> childIndexes) {
            this.childNames = childNames;
            this.childIndexes = childIndexes;
        }

        public List<String> getChildNames() {
            return childNames;
        }

        public List<Integer> getChildIndexes() {
            return childIndexes;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .flatMap(executionContext -> {
                        Tag nbt = executionContext.getCurrentTag();
                        if (!getChildIndexes().isEmpty() && nbt.getId() == Tag.TAG_LIST) {
                            ListTag tag = (ListTag) nbt;
                            return getChildIndexes()
                                    .stream()
                                    .map(tag::get)
                                    .filter((subTag) -> subTag.getId() != 0)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        } else if (!getChildNames().isEmpty() && nbt.getId() == Tag.TAG_COMPOUND) {
                            CompoundTag tag = (CompoundTag) nbt;
                            return getChildNames()
                                    .stream()
                                    .map(tag::get)
                                    .filter(Objects::nonNull)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

        @Override
        public INbtPathNavigation asNavigation(@Nullable INbtPathNavigation child) {
            if (!getChildNames().isEmpty()) {
                return new NbtPathNavigationAdapter(getChildNames(), child);
            } else {
                throw new UnsupportedOperationException("NbtPathExpressionParseHandlerUnion.Expression#asNavigation is not implemented for lists");
            }
        }
    }
}
