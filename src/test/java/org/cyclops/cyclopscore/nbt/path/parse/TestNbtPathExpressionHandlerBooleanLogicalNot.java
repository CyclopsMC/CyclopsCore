package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
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
    public void testNonMatchPartialExpression() {
        // Should not match partial expressions like "!< 10"
        assertThat(handler.handlePrefixOf("!< 10", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionWithParentheses() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("!(@.a < 10)", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testMatchExpressionWithPath() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("!@.a == 5", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testMatchExpressionWithSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("  !  (@.a > 5)", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testExpressionStreamNegateTrue() {
        // Create an expression that evaluates "!(@.a < 10)"
        INbtPathExpression expression = handler.handlePrefixOf("!(@.a < 10)", 0).getPrefixExpression();

        CompoundTag tag = new CompoundTag();
        tag.putInt("a", 5);
        // 5 < 10 is true, so !(true) should be false
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamNegateFalse() {
        // Create an expression that evaluates "!(@.a < 10)"
        INbtPathExpression expression = handler.handlePrefixOf("!(@.a < 10)", 0).getPrefixExpression();

        CompoundTag tag = new CompoundTag();
        tag.putInt("a", 15);
        // 15 < 10 is false, so !(false) should be true
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamNegatePathTrue() {
        // Create an expression that evaluates "!@.a == 1"
        INbtPathExpression expression = handler.handlePrefixOf("!@.a == 1", 0).getPrefixExpression();

        CompoundTag tag = new CompoundTag();
        tag.putInt("a", 1);
        // @.a == 1 evaluates to: get a (=1), then == 1, result is true, so !(true) should be false
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamNegatePathFalse() {
        // Create an expression that evaluates "!@.a == 1"
        INbtPathExpression expression = handler.handlePrefixOf("!@.a == 1", 0).getPrefixExpression();

        CompoundTag tag = new CompoundTag();
        tag.putInt("a", 0);
        // @.a == 1 evaluates to: get a (=0), then == 1, result is false, so !(false) should be true
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }
}
