package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles child path expressions in the form of "\.[a-zA-Z]+",
 * where the matched string represents the child name that should be navigated in.
 */
public class NbtPathExpressionParseHandlerChild implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_CHILDNAME = Pattern.compile("[a-zA-Z]+");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (nbtPathExpression.charAt(pos) != '.' || nbtPathExpression.length() <= pos + 1) {
            return HandleResult.INVALID;
        }

        Matcher matcher = REGEX_CHILDNAME
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String childName = matcher.group();
        return new HandleResult(new Expression(childName), 1 + childName.length());
    }

    public static class Expression implements INbtPathExpression {

        private final String childName;

        public Expression(String childName) {
            this.childName = childName;
        }

        String getChildName() {
            return childName;
        }

        @Override
        public NbtPathExpressionMatches match(Stream<NBTBase> nbt) {
            return new NbtPathExpressionMatches(nbt.flatMap(subNbt -> {
                if (subNbt.getId() == 10) {
                    NBTTagCompound tag = (NBTTagCompound) subNbt;
                    NBTBase childTag = tag.getTag(childName);
                    if (childTag != null) {
                        return Stream.of(childTag);
                    }
                }
                return Stream.empty();
            }));
        }
    }
}