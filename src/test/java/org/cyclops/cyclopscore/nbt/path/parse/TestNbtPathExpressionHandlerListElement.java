package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
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
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                StringTag.valueOf("a"),
                StringTag.valueOf("b"),
                StringTag.valueOf("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagNoMatch() {
        ListTag tag = new ListTag();
        tag.add(StringTag.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        ListTag tag1 = new ListTag();
        tag1.add(StringTag.valueOf("a0"));

        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("b0"));

        ListTag tag3 = new ListTag();
        tag3.add(StringTag.valueOf("c0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagMatch() {
        ListTag tag = new ListTag();
        tag.add(StringTag.valueOf("0"));
        tag.add(StringTag.valueOf("1"));
        tag.add(StringTag.valueOf("2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringTag.valueOf("1"))));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        ListTag tag1 = new ListTag();
        tag1.add(StringTag.valueOf("a0"));
        tag1.add(StringTag.valueOf("a1"));
        tag1.add(StringTag.valueOf("a2"));

        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("b0"));
        tag2.add(StringTag.valueOf("b1"));
        tag2.add(StringTag.valueOf("b2"));

        ListTag tag3 = new ListTag();
        tag3.add(StringTag.valueOf("c0"));
        tag3.add(StringTag.valueOf("c1"));
        tag3.add(StringTag.valueOf("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a1"),
                        StringTag.valueOf("b1"),
                        StringTag.valueOf("c1")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        ListTag tag1 = new ListTag();
        tag1.add(StringTag.valueOf("a0"));
        tag1.add(StringTag.valueOf("a1"));
        tag1.add(StringTag.valueOf("a2"));

        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("b0"));
        tag2.add(StringTag.valueOf("b1"));

        ListTag tag3 = new ListTag();
        tag3.add(StringTag.valueOf("c0"));
        tag3.add(StringTag.valueOf("c1"));
        tag3.add(StringTag.valueOf("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[2]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("c2")
                )));
    }

}
