package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerFilterExpression {

    private NbtPathExpressionParseHandlerFilterExpression handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerFilterExpression();
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
    public void testNonMatchNoQuestionMark() {
        assertThat(handler.handlePrefixOf("[]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoBrackets() {
        assertThat(handler.handlePrefixOf("[?]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoExpression() {
        assertThat(handler.handlePrefixOf("[?()]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchInvalidExpression1() {
        assertThat(handler.handlePrefixOf("[?(()]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchInvalidExpression2() {
        assertThat(handler.handlePrefixOf("[?(()]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchInvalidExpression3() {
        assertThat(handler.handlePrefixOf("[?(abc)]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionRoot() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[?($)]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerFilterExpression.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerFilterExpression.Expression) result.getPrefixExpression()).getExpression(),
                instanceOf(NbtPathExpressionList.class));
        NbtPathExpressionList expressionList = (NbtPathExpressionList) ((NbtPathExpressionParseHandlerFilterExpression.Expression)
                result.getPrefixExpression()).getExpression();
        assertThat(expressionList.getSubExpressions().length, equalTo(1));
        assertThat(expressionList.getSubExpressions()[0], instanceOf(NbtPathExpressionParseHandlerRoot.Expression.class));
    }

    @Test
    public void testMatchExpressionRootChild() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa[?($.abc)]", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(10));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerFilterExpression.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerFilterExpression.Expression) result.getPrefixExpression()).getExpression(),
                instanceOf(NbtPathExpressionList.class));
        NbtPathExpressionList expressionList = (NbtPathExpressionList) ((NbtPathExpressionParseHandlerFilterExpression.Expression)
                result.getPrefixExpression()).getExpression();
        assertThat(expressionList.getSubExpressions().length, equalTo(2));
        assertThat(expressionList.getSubExpressions()[0], instanceOf(NbtPathExpressionParseHandlerRoot.Expression.class));
        assertThat(expressionList.getSubExpressions()[1], instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) expressionList.getSubExpressions()[1]).getChildName(), is("abc"));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                StringTag.valueOf("a"),
                StringTag.valueOf("b"),
                StringTag.valueOf("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        tag1,
                        tag2,
                        tag3
                )));
    }

    @Test
    public void testExpressionStreamTagNested() {
        ListTag tagRoot = new ListTag();
        ListTag tagRootFiltered = new ListTag();
        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        tag1.put("a", tag2);
        tag2.put("b", StringTag.valueOf("c"));
        tag3.putString("a", "x");
        tagRoot.add(tag1);
        tagRoot.add(tag2);
        tagRootFiltered.add(tag1);
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@.a.b)]", 2).getPrefixExpression();
        ArrayList<Tag> expected = Lists.newArrayList();
        expected.add(tagRootFiltered);
        assertThat(expression.match(tagRoot).getMatches().collect(Collectors.toList()), is(expected));
    }

    @Test
    public void testExpressionStreamMultipleTagCompoundsMatch() {
        CompoundTag tag1 = new CompoundTag();
        tag1.put("a", StringTag.valueOf("a0"));
        tag1.put("b", StringTag.valueOf("a1"));
        tag1.put("c", StringTag.valueOf("a2"));

        CompoundTag tag2 = new CompoundTag();
        tag2.put("a", StringTag.valueOf("b0"));
        tag2.put("b", StringTag.valueOf("b1"));
        tag2.put("c", StringTag.valueOf("b2"));

        CompoundTag tag3 = new CompoundTag();
        tag3.put("a", StringTag.valueOf("c0"));
        tag3.put("b", StringTag.valueOf("c1"));
        tag3.put("c", StringTag.valueOf("c2"));

        ListTag list1 = new ListTag();
        list1.add(StringTag.valueOf("a0"));
        list1.add(StringTag.valueOf("a1"));
        list1.add(StringTag.valueOf("a2"));

        ListTag list2 = new ListTag();
        list2.add(StringTag.valueOf("b0"));
        list2.add(StringTag.valueOf("b1"));
        list2.add(StringTag.valueOf("b2"));

        ListTag list3 = new ListTag();
        list3.add(StringTag.valueOf("c0"));
        list3.add(StringTag.valueOf("c1"));
        list3.add(StringTag.valueOf("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        list1,
                        list2,
                        list3
                )));
    }

}
