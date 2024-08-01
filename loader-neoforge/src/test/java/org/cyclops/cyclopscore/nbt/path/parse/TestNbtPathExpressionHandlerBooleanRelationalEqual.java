package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerBooleanRelationalEqual {

    private NbtPathExpressionParseHandlerBooleanRelationalEqual handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanRelationalEqual();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("==2", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(3));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalEqual.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalEqual.Expression) result.getPrefixExpression()).getTargetDouble(), is(2D));
    }

    @Test
    public void testMatchExpressionDoubleSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(" == 24.23", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(9));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalEqual.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalEqual.Expression) result.getPrefixExpression()).getTargetDouble(), is(24.23D));
    }

    @Test
    public void testExpressionStreamSingleLeafIntValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(3))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafIntInvalid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(DoubleTag.valueOf(3.1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

}
