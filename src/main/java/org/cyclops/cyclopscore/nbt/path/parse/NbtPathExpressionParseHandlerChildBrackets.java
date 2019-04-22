package org.cyclops.cyclopscore.nbt.path.parse;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A handler that handles child path expressions in the form of "["childName"]",
 * where the matched string represents the child name that should be navigated in.
 * This works just like {@link NbtPathExpressionParseHandlerChild},
 * but allows special characters to be used.
 */
public class NbtPathExpressionParseHandlerChildBrackets implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_CHILDNAME = Pattern.compile("^\\[\"([^\"]*)\"\\]");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_CHILDNAME
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String childName = matcher.group(1);
        return new HandleResult(new NbtPathExpressionParseHandlerChild.Expression(childName), 4 + childName.length());
    }
}
