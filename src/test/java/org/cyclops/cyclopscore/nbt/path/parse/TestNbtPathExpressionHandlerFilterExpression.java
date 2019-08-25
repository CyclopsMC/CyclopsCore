package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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
        assertThat(expression.match(Stream.of(new StringNBT("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                new StringNBT("a"),
                new StringNBT("b"),
                new StringNBT("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
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
        ListNBT tagRoot = new ListNBT();
        ListNBT tagRootFiltered = new ListNBT();
        CompoundNBT tag1 = new CompoundNBT();
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        tag1.put("a", tag2);
        tag2.put("b", new StringNBT("c"));
        tag3.putString("a", "x");
        tagRoot.add(tag1);
        tagRoot.add(tag2);
        tagRootFiltered.add(tag1);
        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@.a.b)]", 2).getPrefixExpression();
        ArrayList<INBT> expected = Lists.newArrayList();
        expected.add(tagRootFiltered);
        assertThat(expression.match(tagRoot).getMatches().collect(Collectors.toList()), is(expected));
    }

    @Test
    public void testExpressionStreamMultipleTagCompoundsMatch() {
        CompoundNBT tag1 = new CompoundNBT();
        tag1.put("a", new StringNBT("a0"));
        tag1.put("b", new StringNBT("a1"));
        tag1.put("c", new StringNBT("a2"));

        CompoundNBT tag2 = new CompoundNBT();
        tag2.put("a", new StringNBT("b0"));
        tag2.put("b", new StringNBT("b1"));
        tag2.put("c", new StringNBT("b2"));

        CompoundNBT tag3 = new CompoundNBT();
        tag3.put("a", new StringNBT("c0"));
        tag3.put("b", new StringNBT("c1"));
        tag3.put("c", new StringNBT("c2"));

        ListNBT list1 = new ListNBT();
        list1.add(new StringNBT("a0"));
        list1.add(new StringNBT("a1"));
        list1.add(new StringNBT("a2"));

        ListNBT list2 = new ListNBT();
        list2.add(new StringNBT("b0"));
        list2.add(new StringNBT("b1"));
        list2.add(new StringNBT("b2"));

        ListNBT list3 = new ListNBT();
        list3.add(new StringNBT("c0"));
        list3.add(new StringNBT("c1"));
        list3.add(new StringNBT("c2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[?(@)]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
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
