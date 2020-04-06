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
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamFromFirstTagMatch() {
        ListNBT tag = new ListNBT();
        tag.add(StringNBT.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[0:]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("0")
                )));
    }

    @Test
    public void testExpressionStreamAfterFirstTagNoMatch() {
        ListNBT tag = new ListNBT();
        tag.add(StringNBT.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:]", 2).getPrefixExpression();
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
        tag.add(StringNBT.valueOf("0"));
        tag.add(StringNBT.valueOf("1"));
        tag.add(StringNBT.valueOf("2"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("1"),
                        StringNBT.valueOf("2")
                )));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("b1"),
                        StringNBT.valueOf("b2"),
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("c2")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatchStep() {
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[0::2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a0"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("b0"),
                        StringNBT.valueOf("b2"),
                        StringNBT.valueOf("c0"),
                        StringNBT.valueOf("c2")
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("b1"),
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("c2")
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
