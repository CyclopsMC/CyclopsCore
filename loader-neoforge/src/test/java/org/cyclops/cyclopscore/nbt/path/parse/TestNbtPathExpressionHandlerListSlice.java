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
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamFromFirstTagMatch() {
        ListTag tag = new ListTag();
        tag.add(StringTag.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[0:]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("0")
                )));
    }

    @Test
    public void testExpressionStreamAfterFirstTagNoMatch() {
        ListTag tag = new ListTag();
        tag.add(StringTag.valueOf("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("1"),
                        StringTag.valueOf("2")
                )));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a1"),
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("b1"),
                        StringTag.valueOf("b2"),
                        StringTag.valueOf("c1"),
                        StringTag.valueOf("c2")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatchStep() {
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[0::2]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a0"),
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("b0"),
                        StringTag.valueOf("b2"),
                        StringTag.valueOf("c0"),
                        StringTag.valueOf("c2")
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a1"),
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("b1"),
                        StringTag.valueOf("c1"),
                        StringTag.valueOf("c2")
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
