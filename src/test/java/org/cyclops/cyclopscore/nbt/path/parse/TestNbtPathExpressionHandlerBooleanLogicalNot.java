package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerBooleanLogicalNot {

    private NbtPathExpressionParseHandlerBooleanLogicalNot handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanLogicalNot();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionSimple() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("!< 10", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(5));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testMatchExpressionWithSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("  !  > 5", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(8));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testExpressionStreamNegateTrue() {
        // Create an expression that evaluates "!< 10"
        INbtPathExpression expression = handler.handlePrefixOf("!< 10", 0).getPrefixExpression();

        // 5 < 10 is true, so !(true) should be false
        assertThat(expression.match(Stream.of(IntTag.valueOf(5))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamNegateFalse() {
        // Create an expression that evaluates "!< 10"
        INbtPathExpression expression = handler.handlePrefixOf("!< 10", 0).getPrefixExpression();

        // 15 < 10 is false, so !(false) should be true
        assertThat(expression.match(Stream.of(IntTag.valueOf(15))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamNegateByteTagTrue() {
        // Create an expression that evaluates "! == 1" (with space to avoid != operator)
        INbtPathExpression expression = handler.handlePrefixOf("! == 1", 0).getPrefixExpression();

        // 1 == 1 is true, so !(true) should be false
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamNegateByteTagFalse() {
        // Create an expression that evaluates "! == 1" (with space to avoid != operator)
        INbtPathExpression expression = handler.handlePrefixOf("! == 1", 0).getPrefixExpression();

        // 0 == 1 is false, so !(false) should be true
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 0))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }
}
