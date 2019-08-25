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
        assertThat(expression.match(Stream.of(new StringNBT("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                new StringNBT("a"),
                new StringNBT("b"),
                new StringNBT("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagNoMatch() {
        ListNBT tag = new ListNBT();
        tag.add(new StringNBT("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(new StringNBT("a0"));

        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("b0"));

        ListNBT tag3 = new ListNBT();
        tag3.add(new StringNBT("c0"));

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
        tag.add(new StringNBT("0"));
        tag.add(new StringNBT("1"));
        tag.add(new StringNBT("2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new StringNBT("1"))));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(new StringNBT("a0"));
        tag1.add(new StringNBT("a1"));
        tag1.add(new StringNBT("a2"));

        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("b0"));
        tag2.add(new StringNBT("b1"));
        tag2.add(new StringNBT("b2"));

        ListNBT tag3 = new ListNBT();
        tag3.add(new StringNBT("c0"));
        tag3.add(new StringNBT("c1"));
        tag3.add(new StringNBT("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("a1"),
                        new StringNBT("b1"),
                        new StringNBT("c1")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        ListNBT tag1 = new ListNBT();
        tag1.add(new StringNBT("a0"));
        tag1.add(new StringNBT("a1"));
        tag1.add(new StringNBT("a2"));

        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("b0"));
        tag2.add(new StringNBT("b1"));

        ListNBT tag3 = new ListNBT();
        tag3.add(new StringNBT("c0"));
        tag3.add(new StringNBT("c1"));
        tag3.add(new StringNBT("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("a2"),
                        new StringNBT("c2")
                )));
    }

}
