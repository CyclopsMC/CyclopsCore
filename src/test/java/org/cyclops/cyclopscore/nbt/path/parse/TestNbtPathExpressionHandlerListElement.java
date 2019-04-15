package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                new NBTTagString("a"),
                new NBTTagString("b"),
                new NBTTagString("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagNoMatch() {
        NBTTagList tag = new NBTTagList();
        tag.appendTag(new NBTTagString("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        NBTTagList tag1 = new NBTTagList();
        tag1.appendTag(new NBTTagString("a0"));

        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("b0"));

        NBTTagList tag3 = new NBTTagList();
        tag3.appendTag(new NBTTagString("c0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagMatch() {
        NBTTagList tag = new NBTTagList();
        tag.appendTag(new NBTTagString("0"));
        tag.appendTag(new NBTTagString("1"));
        tag.appendTag(new NBTTagString("2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagString("1"))));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        NBTTagList tag1 = new NBTTagList();
        tag1.appendTag(new NBTTagString("a0"));
        tag1.appendTag(new NBTTagString("a1"));
        tag1.appendTag(new NBTTagString("a2"));

        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("b0"));
        tag2.appendTag(new NBTTagString("b1"));
        tag2.appendTag(new NBTTagString("b2"));

        NBTTagList tag3 = new NBTTagList();
        tag3.appendTag(new NBTTagString("c0"));
        tag3.appendTag(new NBTTagString("c1"));
        tag3.appendTag(new NBTTagString("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a1"),
                        new NBTTagString("b1"),
                        new NBTTagString("c1")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        NBTTagList tag1 = new NBTTagList();
        tag1.appendTag(new NBTTagString("a0"));
        tag1.appendTag(new NBTTagString("a1"));
        tag1.appendTag(new NBTTagString("a2"));

        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("b0"));
        tag2.appendTag(new NBTTagString("b1"));

        NBTTagList tag3 = new NBTTagList();
        tag3.appendTag(new NBTTagString("c0"));
        tag3.appendTag(new NBTTagString("c1"));
        tag3.appendTag(new NBTTagString("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a2"),
                        new NBTTagString("c2")
                )));
    }

}
