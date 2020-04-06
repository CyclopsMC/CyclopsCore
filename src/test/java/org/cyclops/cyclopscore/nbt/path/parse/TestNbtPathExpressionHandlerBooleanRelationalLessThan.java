package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.IntNBT;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerBooleanRelationalLessThan {

    private NbtPathExpressionParseHandlerBooleanRelationalLessThan handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanRelationalLessThan();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("<2", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(2));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalLessThan.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalLessThan.Expression) result.getPrefixExpression()).getTargetDouble(), is(2D));
    }

    @Test
    public void testMatchExpressionDoubleSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(" < 24.23", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(8));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalLessThan.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalLessThan.Expression) result.getPrefixExpression()).getTargetDouble(), is(24.23D));
    }

    @Test
    public void testExpressionStreamSingleLeafIntValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntNBT.valueOf(2))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteNBT.valueOf((byte) 1))));
    }

}
