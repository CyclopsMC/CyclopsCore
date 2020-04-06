package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerCurrent {

    private NbtPathExpressionParseHandlerCurrent handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerCurrent();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf(".", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("@", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa@", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("@aaa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE;
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingle() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE;
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("a"))));
    }

    @Test
    public void testExpressionStreamMultiple() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE;
        Stream<INBT> stream = Stream.of(
                StringNBT.valueOf("a"),
                StringNBT.valueOf("b"),
                StringNBT.valueOf("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a"),
                        StringNBT.valueOf("b"),
                        StringNBT.valueOf("c")
                )));
    }

    @Test
    public void testExpressionNotStartFromRoot() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerCurrent.Expression.INSTANCE;
        Stream<NbtPathExpressionExecutionContext> stream = Stream.of(
                new NbtPathExpressionExecutionContext(StringNBT.valueOf("a"), new NbtPathExpressionExecutionContext(
                        StringNBT.valueOf("b"), new NbtPathExpressionExecutionContext(StringNBT.valueOf("c"))
                ))
        );
        assertThat(expression.matchContexts(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a")
                )));
    }

}
