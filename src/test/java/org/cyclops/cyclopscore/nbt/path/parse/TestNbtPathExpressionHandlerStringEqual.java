package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerStringEqual {

    private NbtPathExpressionParseHandlerStringEqual handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerStringEqual();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchGreaterThan() {
        assertThat(handler.handlePrefixOf("==", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchTargetNumber() {
        assertThat(handler.handlePrefixOf("== 0", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionStringEmpty() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("==\"\"", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerStringEqual.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerStringEqual.Expression) result.getPrefixExpression()).getTargetString(), is(""));
    }

    @Test
    public void testMatchExpressionStringAbc() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("==\"abc\"", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerStringEqual.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerStringEqual.Expression) result.getPrefixExpression()).getTargetString(), is("abc"));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == \"abc\"", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeafStringInvalid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == \"abc\"", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagByte((byte) 0))));
    }

    @Test
    public void testExpressionStreamSingleLeafStringValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == \"abc\"", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(new NBTTagString("abc"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagByte((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafInt() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == \"abc\"", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(new NBTTagInt(2))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagByte((byte) 0))));
    }

    @Test
    public void testExpressionStreamMultipleLeafString() {
        INbtPathExpression expression = handler.handlePrefixOf("aa == \"b\"", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                new NBTTagString("a"),
                new NBTTagString("b"),
                new NBTTagString("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagByte((byte) 0),
                        new NBTTagByte((byte) 1),
                        new NBTTagByte((byte) 0)
                )));
    }

}
