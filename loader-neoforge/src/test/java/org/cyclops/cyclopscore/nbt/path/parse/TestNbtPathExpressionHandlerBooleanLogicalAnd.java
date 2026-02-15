package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerBooleanLogicalAnd {

    private NbtPathExpressionParseHandlerBooleanLogicalAnd handler;

    @BeforeEach
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanLogicalAnd();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchSingleAmpersand() {
        assertThat(handler.handlePrefixOf("&", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionSimple() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("&& < 10", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalAnd.Expression.class));
    }

    @Test
    public void testMatchExpressionWithSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("  &&  > 5", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(9));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalAnd.Expression.class));
    }

    @Test
    public void testExpressionStreamBothTrue() {
        // Create an expression that evaluates "< 10" (the right side of &&)
        INbtPathExpression expression = handler.handlePrefixOf("&& < 10", 0).getPrefixExpression();

        // Create execution context with ByteTag(1) as current (left side = true)
        // and IntTag(5) as parent (which will be used to evaluate right side: 5 < 10 = true)
        NbtPathExpressionExecutionContext parentContext = new NbtPathExpressionExecutionContext(IntTag.valueOf(5));
        NbtPathExpressionExecutionContext context = new NbtPathExpressionExecutionContext(ByteTag.valueOf((byte) 1), parentContext);

        // true && true should be true
        assertThat(expression.match(Stream.of(context.getCurrentTag()))
                        .getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamLeftFalse() {
        // Create an expression that evaluates "< 10"
        INbtPathExpression expression = handler.handlePrefixOf("&& < 10", 0).getPrefixExpression();

        // Create execution context with ByteTag(0) as current (left side = false)
        // and IntTag(5) as parent (right side would be: 5 < 10 = true, but left is false)
        NbtPathExpressionExecutionContext parentContext = new NbtPathExpressionExecutionContext(IntTag.valueOf(5));
        NbtPathExpressionExecutionContext context = new NbtPathExpressionExecutionContext(ByteTag.valueOf((byte) 0), parentContext);

        // false && true should be false
        assertThat(expression.match(Stream.of(context.getCurrentTag()))
                        .getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamRightFalse() {
        // Create an expression that evaluates "> 10"
        INbtPathExpression expression = handler.handlePrefixOf("&& > 10", 0).getPrefixExpression();

        // Create execution context with ByteTag(1) as current (left side = true)
        // and IntTag(5) as parent (right side: 5 > 10 = false)
        NbtPathExpressionExecutionContext parentContext = new NbtPathExpressionExecutionContext(IntTag.valueOf(5));
        NbtPathExpressionExecutionContext context = new NbtPathExpressionExecutionContext(ByteTag.valueOf((byte) 1), parentContext);

        // true && false should be false
        assertThat(expression.match(Stream.of(context.getCurrentTag()))
                        .getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }
}
