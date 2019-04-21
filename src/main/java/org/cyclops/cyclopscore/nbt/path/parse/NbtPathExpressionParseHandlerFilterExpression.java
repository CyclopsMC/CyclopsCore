package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.NbtPath;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionMatches;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A handler that handles filter expressions in the form of "[?(expression)]", such as "[?(@.childName)]" or "[?(@.childName < 10)]".
 */
public class NbtPathExpressionParseHandlerFilterExpression implements INbtPathExpressionParseHandler {

    private static final Pattern REGEX_EXPRESSION = Pattern.compile("\\[\\?\\(([^\\)^\\(]+)\\)\\]");

    @Nullable
    @Override
    public HandleResult handlePrefixOf(String nbtPathExpression, int pos) {
        Matcher matcher = REGEX_EXPRESSION
                .matcher(nbtPathExpression)
                .region(pos, nbtPathExpression.length());
        if (!matcher.find()) {
            return HandleResult.INVALID;
        }

        String expressionString = matcher.group(1);
        try {
            INbtPathExpression expression = NbtPath.parse(expressionString);
            return new HandleResult(new Expression(expression),
                    5 + expressionString.length());
        } catch (NbtParseException e) {
            return HandleResult.INVALID;
        }
    }

    public static class Expression implements INbtPathExpression {

        private final INbtPathExpression expression;

        public Expression(INbtPathExpression expression) {
            this.expression = expression;
        }

        public INbtPathExpression getExpression() {
            return expression;
        }

        @Override
        public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts) {
            return new NbtPathExpressionMatches(executionContexts
                    .filter(executionContext -> getExpression().test(executionContext.getCurrentTag()))
            );
        }

    }
}
