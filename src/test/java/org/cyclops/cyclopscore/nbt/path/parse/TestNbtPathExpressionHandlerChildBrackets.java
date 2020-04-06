package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
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

public class TestNbtPathExpressionHandlerChildBrackets {

    private NbtPathExpressionParseHandlerChildBrackets handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerChildBrackets();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchOnlyOpenBrackets() {
        assertThat(handler.handlePrefixOf("[", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchOnlyCloseBrackets() {
        assertThat(handler.handlePrefixOf("]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoContentsInBrackets() {
        assertThat(handler.handlePrefixOf("[]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchOnlyOneQuote() {
        assertThat(handler.handlePrefixOf("[\"]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchEmpty() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo(""));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"abc\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc"));
    }

    @Test
    public void testMatchSingleMixedCasing() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"aBc\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("aBc"));
    }

    @Test
    public void testMatchSingleNumbers() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"abc012\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(10));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc012"));
    }

    @Test
    public void testMatchSingleUnderscores() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"a_b_c\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(9));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("a_b_c"));
    }

    @Test
    public void testMatchSingleUnderscoresSpecialCharacters() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"&*()%^$#:.\"]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(14));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("&*()%^$#:."));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa[\"abc\"]", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc"));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[\"abc\"]$aa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
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
        CompoundNBT tag = new CompoundNBT();
        tag.putString("def", "123");

        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        CompoundNBT tag1 = new CompoundNBT();
        tag1.putString("def", "1");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putString("def", "2");
        CompoundNBT tag3 = new CompoundNBT();
        tag3.putString("def", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
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
        CompoundNBT tag = new CompoundNBT();
        tag.putString("abc", "123");

        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("123")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        CompoundNBT tag1 = new CompoundNBT();
        tag1.putString("abc", "1");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putString("abc", "2");
        CompoundNBT tag3 = new CompoundNBT();
        tag3.putString("abc", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("1"),
                        StringNBT.valueOf("2"),
                        StringNBT.valueOf("3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        CompoundNBT tag1 = new CompoundNBT();
        tag1.putString("abc", "1");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putString("def", "2");
        CompoundNBT tag3 = new CompoundNBT();
        tag3.putString("abc", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa[\"abc\"]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("1"),
                        StringNBT.valueOf("3")
                )));
    }

}
