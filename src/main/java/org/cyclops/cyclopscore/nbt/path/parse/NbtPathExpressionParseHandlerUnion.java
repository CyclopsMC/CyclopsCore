package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

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

    private static final Pattern REGEX_BRACKETS = Pattern.compile("\\[([^\\]]+(,[^\\]]+)+)]");

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
                        NBTBase nbt = executionContext.getCurrentTag();
                        if (!getChildIndexes().isEmpty() && nbt.getId() == Constants.NBT.TAG_LIST) {
                            NBTTagList tag = (NBTTagList) nbt;
                            return getChildIndexes()
                                    .stream()
                                    .map(tag::get)
                                    .filter((subTag) -> subTag.getId() != 0)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        } else if (!getChildNames().isEmpty() && nbt.getId() == Constants.NBT.TAG_COMPOUND) {
                            NBTTagCompound tag = (NBTTagCompound) nbt;
                            return getChildNames()
                                    .stream()
                                    .map(tag::getTag)
                                    .filter(Objects::nonNull)
                                    .map((subTag) -> new NbtPathExpressionExecutionContext(subTag, executionContext));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

    }
}
