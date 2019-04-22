package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationAdapter;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles child path expressions in the form of ".childName",
 * where the matched string represents the child name that should be navigated in.
 */
public class NbtPathExpressionParseHandlerChild implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_CHILDNAME = Pattern.compile("^[a-zA-Z_0-9]+");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        if (nbtPathExpression.charAt(pos) != '.' || nbtPathExpression.length() <= pos + 1) {
            return HandleResult.INVALID;
        }

        Matcher matcher = REGEX_CHILDNAME
                .matcher(nbtPathExpression)
                .region(pos + 1, nbtPathExpression.length());
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
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .map(executionContext -> {
                        NBTBase nbt = executionContext.getCurrentTag();
                        if (nbt.getId() == Constants.NBT.TAG_COMPOUND) {
                            NBTTagCompound tag = (NBTTagCompound) nbt;
                            NBTBase childTag = tag.getTag(childName);
                            if (childTag != null) {
                                return new NbtPathExpressionExecutionContext(childTag, executionContext);
                            }
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
            );
        }

        @Override
        public INbtPathNavigation asNavigation(@Nullable INbtPathNavigation child) {
            return new NbtPathNavigationAdapter(getChildName(), child);
        }
    }
}
