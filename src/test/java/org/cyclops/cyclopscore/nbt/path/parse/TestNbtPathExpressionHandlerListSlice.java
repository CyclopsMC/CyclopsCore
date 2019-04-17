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
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamFromFirstTagMatch() {
        NBTTagList tag = new NBTTagList();
        tag.appendTag(new NBTTagString("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[0:]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("0")
                )));
    }

    @Test
    public void testExpressionStreamAfterFirstTagNoMatch() {
        NBTTagList tag = new NBTTagList();
        tag.appendTag(new NBTTagString("0"));

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("1"),
                        new NBTTagString("2")
                )));
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a1"),
                        new NBTTagString("a2"),
                        new NBTTagString("b1"),
                        new NBTTagString("b2"),
                        new NBTTagString("c1"),
                        new NBTTagString("c2")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatchStep() {
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[0::2]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a0"),
                        new NBTTagString("a2"),
                        new NBTTagString("b0"),
                        new NBTTagString("b2"),
                        new NBTTagString("c0"),
                        new NBTTagString("c2")
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

        INbtPathExpression expression = handler.handlePrefixOf("aa[1:2]", 2).getPrefixExpression();
        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a1"),
                        new NBTTagString("a2"),
                        new NBTTagString("b1"),
                        new NBTTagString("c1"),
                        new NBTTagString("c2")
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
