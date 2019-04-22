package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                new NBTTagString("a"),
                new NBTTagString("b"),
                new NBTTagString("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
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
        NBTTagList tagRoot = new NBTTagList();
        NBTTagList tagRootFiltered = new NBTTagList();
        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        tag1.setTag("a", tag2);
        tag2.setTag("b", new NBTTagString("c"));
        tag3.setString("a", "x");
        tagRoot.appendTag(tag1);
        tagRoot.appendTag(tag2);
        tagRootFiltered.appendTag(tag1);
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@.a.b)]", 2).getPrefixExpression();
        ArrayList<NBTBase> expected = Lists.newArrayList();
        expected.add(tagRootFiltered);
        assertThat(expression.match(tagRoot).getMatches().collect(Collectors.toList()), is(expected));
    }

    @Test
    public void testExpressionStreamMultipleTagCompoundsMatch() {
        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setTag("a", new NBTTagString("a0"));
        tag1.setTag("b", new NBTTagString("a1"));
        tag1.setTag("c", new NBTTagString("a2"));

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setTag("a", new NBTTagString("b0"));
        tag2.setTag("b", new NBTTagString("b1"));
        tag2.setTag("c", new NBTTagString("b2"));

        NBTTagCompound tag3 = new NBTTagCompound();
        tag3.setTag("a", new NBTTagString("c0"));
        tag3.setTag("b", new NBTTagString("c1"));
        tag3.setTag("c", new NBTTagString("c2"));

        NBTTagList list1 = new NBTTagList();
        list1.appendTag(new NBTTagString("a0"));
        list1.appendTag(new NBTTagString("a1"));
        list1.appendTag(new NBTTagString("a2"));

        NBTTagList list2 = new NBTTagList();
        list2.appendTag(new NBTTagString("b0"));
        list2.appendTag(new NBTTagString("b1"));
        list2.appendTag(new NBTTagString("b2"));

        NBTTagList list3 = new NBTTagList();
        list3.appendTag(new NBTTagString("c0"));
        list3.appendTag(new NBTTagString("c1"));
        list3.appendTag(new NBTTagString("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
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
