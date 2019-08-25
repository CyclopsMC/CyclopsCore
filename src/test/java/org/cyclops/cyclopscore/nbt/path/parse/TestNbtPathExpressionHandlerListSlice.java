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

public class TestNbtPathExpressionHandlerListSlice {

    private NbtPathExpressionParseHandlerListSlice handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerListSlice();
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
        assertThat(handler.handlePrefixOf("[a:]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoColon() {
        assertThat(handler.handlePrefixOf("[0]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchZeroStep() {
        assertThat(handler.handlePrefixOf("[0:1:0]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchIntColon() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[0:]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(0));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(-1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(1));
    }

    @Test
    public void testMatchIntColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[0:1]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(5));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(0));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(1));
    }

    @Test
    public void testMatchColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[:1]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(0));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(1));
    }

    @Test
    public void testMatchColonColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[::2]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(5));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(0));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(-1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(2));
    }

    @Test
    public void testMatchColonIntColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[:1:2]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(0));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(2));
    }

    @Test
    public void testMatchIntColonIntColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[1:1:2]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(2));
    }

    @Test
    public void testMatchIntColonColonInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[1::2]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(-1));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(2));
    }

    @Test
    public void testMatchLong() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[123:456:789]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(13));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(123));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(456));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(789));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa[123:456:789]", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(13));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerListSlice.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStart(), equalTo(123));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getEnd(), equalTo(456));
        assertThat(((NbtPathExpressionParseHandlerListSlice.Expression) result.getPrefixExpression()).getStep(), equalTo(789));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(new StringNBT("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamFromFirstTagMatch() {
        ListNBT tag = new ListNBT();
        tag.add(new StringNBT("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[0:]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("0")
                )));
    }

    @Test
    public void testExpressionStreamAfterFirstTagNoMatch() {
        ListNBT tag = new ListNBT();
        tag.add(new StringNBT("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("1"),
                        new StringNBT("2")
                )));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("a1"),
                        new StringNBT("a2"),
                        new StringNBT("b1"),
                        new StringNBT("b2"),
                        new StringNBT("c1"),
                        new StringNBT("c2")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatchStep() {
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[0::2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("a0"),
                        new StringNBT("a2"),
                        new StringNBT("b0"),
                        new StringNBT("b2"),
                        new StringNBT("c0"),
                        new StringNBT("c2")
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new StringNBT("a1"),
                        new StringNBT("a2"),
                        new StringNBT("b1"),
                        new StringNBT("c1"),
                        new StringNBT("c2")
                )));
    }

    @Test
    public void testStream0_0_1() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(0, 0, 1)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(0)));
    }

    @Test
    public void testStream0_4_1() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(0, 4, 1)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(0, 1, 2, 3, 4)));
    }

    @Test
    public void testStream2_4_1() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(2, 4, 1)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(2, 3, 4)));
    }

    @Test
    public void testStream0_4_2() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(0, 4, 2)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(0, 2, 4)));
    }

    @Test
    public void testStream0_10_3() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(0, 10, 3)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(0, 3, 6, 9)));
    }

    @Test
    public void testStream1_10_3() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(1, 10, 3)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(1, 4, 7, 10)));
    }

    @Test
    public void testStream2_10_3() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(2, 10, 3)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(2, 5, 8)));
    }

    @Test
    public void testStream3_10_3() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(3, 10, 3)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(3, 6, 9)));
    }

    @Test
    public void testStream100_1000_99() {
        assertThat(NbtPathExpressionParseHandlerListSlice.newStartEndStepStream(100, 1000, 99)
                        .mapToObj(Integer::valueOf)
                        .collect(Collectors.toList()),
                is(Lists.newArrayList(100, 199, 298, 397, 496, 595, 694, 793, 892, 991)));
    }

}
