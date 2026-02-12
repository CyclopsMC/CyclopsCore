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
    public void testNonMatchAmpersand() {
        assertThat(handler.handlePrefixOf("&", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoExpression() {
        assertThat(handler.handlePrefixOf("!", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionSimple() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("!@", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(2));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testMatchExpressionWithSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("! @", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(3));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanLogicalNot.Expression.class));
    }

    @Test
    public void testExpressionStreamTrueResult() {
        // NOT true should be false
        INbtPathExpression expression = handler.handlePrefixOf("!@", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamFalseResult() {
        // NOT false should be true
        INbtPathExpression expression = handler.handlePrefixOf("!== 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(ByteTag.valueOf((byte) 0))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamWithNumericComparisonTrue() {
        // NOT (5 == 5) should be false
        INbtPathExpression expression = handler.handlePrefixOf("!== 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(5))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamWithNumericComparisonFalse() {
        // NOT (3 == 5) should be true
        INbtPathExpression expression = handler.handlePrefixOf("!== 5", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(3))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamWithNonByteTag() {
        // NOT (non-null tag) should be false (non-null tags are truthy)
        INbtPathExpression expression = handler.handlePrefixOf("!@", 0).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(10))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }
}
