package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerListElement {

    private NbtPathExpressionParseHandlerListElement handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerListElement();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchOnlyOpen() {
        assertThat(handler.handlePrefixOf("[", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchOnlyClose() {
        assertThat(handler.handlePrefixOf("]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoValue() {
        assertThat(handler.handlePrefixOf("[]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoInt() {
        assertThat(handler.handlePrefixOf("[a]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[1]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(3));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListElement.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListElement.Expression) result.getPrefixExpression()).getChildIndex(), equalTo(1));
    }

    @Test
    public void testMatchLong() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[987]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(5));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListElement.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListElement.Expression) result.getPrefixExpression()).getChildIndex(), equalTo(987));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa[2]", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(3));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListElement.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListElement.Expression) result.getPrefixExpression()).getChildIndex(), equalTo(2));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[10]$aa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListElement.Expression.class));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                StringNBT.valueOf("a"),
                StringNBT.valueOf("b"),
                StringNBT.valueOf("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagNoMatch() {
        ListNBT tag = new ListNBT();
        tag.add(StringNBT.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(StringNBT.valueOf("a0"));

        ListNBT tag2 = new ListNBT();
        tag2.add(StringNBT.valueOf("b0"));

        ListNBT tag3 = new ListNBT();
        tag3.add(StringNBT.valueOf("c0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagMatch() {
        ListNBT tag = new ListNBT();
        tag.add(StringNBT.valueOf("0"));
        tag.add(StringNBT.valueOf("1"));
        tag.add(StringNBT.valueOf("2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("1"))));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(StringNBT.valueOf("a0"));
        tag1.add(StringNBT.valueOf("a1"));
        tag1.add(StringNBT.valueOf("a2"));

        ListNBT tag2 = new ListNBT();
        tag2.add(StringNBT.valueOf("b0"));
        tag2.add(StringNBT.valueOf("b1"));
        tag2.add(StringNBT.valueOf("b2"));

        ListNBT tag3 = new ListNBT();
        tag3.add(StringNBT.valueOf("c0"));
        tag3.add(StringNBT.valueOf("c1"));
        tag3.add(StringNBT.valueOf("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("b1"),
                        StringNBT.valueOf("c1")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(StringNBT.valueOf("a0"));
        tag1.add(StringNBT.valueOf("a1"));
        tag1.add(StringNBT.valueOf("a2"));

        ListNBT tag2 = new ListNBT();
        tag2.add(StringNBT.valueOf("b0"));
        tag2.add(StringNBT.valueOf("b1"));

        ListNBT tag3 = new ListNBT();
        tag3.add(StringNBT.valueOf("c0"));
        tag3.add(StringNBT.valueOf("c1"));
        tag3.add(StringNBT.valueOf("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("c2")
                )));
    }

}
