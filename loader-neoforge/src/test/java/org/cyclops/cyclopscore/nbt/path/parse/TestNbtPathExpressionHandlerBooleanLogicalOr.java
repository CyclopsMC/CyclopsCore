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

public class TestNbtPathExpressionHandlerBooleanLogicalOr {

    private NbtPathExpressionParseHandlerBooleanLogicalOr handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanLogicalOr();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchSinglePipe() {
        assertThat(handler.handlePrefixOf("|", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoRightExpression() {
        assertThat(handler.handlePrefixOf("||", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionSimple() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("|| @", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalOr.Expression.class));
    }

    @Test
    public void testMatchExpressionWithSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(" ||  @", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalOr.Expression.class));
    }

    @Test
    public void testExpressionStreamTrueTrueResult() {
        // Both sides are true, result should be true
        INbtPathExpression expression = handler.handlePrefixOf("|| @", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamTrueFalseResult() {
        // Left is true, right is false, result should be true
        INbtPathExpression expression = handler.handlePrefixOf("|| == 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamFalseTrueResult() {
        // Left is false, right is true, result should be true
        INbtPathExpression expression = handler.handlePrefixOf("|| @", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(5))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamFalseFalseResult() {
        // Both sides are false, result should be false
        INbtPathExpression expression = handler.handlePrefixOf("|| == 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 0))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamWithNumericComparison() {
        // False (0) || (5 == 5) should be true
        INbtPathExpression expression = handler.handlePrefixOf("|| == 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(5))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }
}
