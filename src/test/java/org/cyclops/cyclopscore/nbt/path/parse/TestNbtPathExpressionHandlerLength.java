package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerLength {

    private NbtPathExpressionParseHandlerLength handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerLength();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf(".", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".length", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerLength.Expression.INSTANCE));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa.length", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerLength.Expression.INSTANCE));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".length.aaa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerLength.Expression.INSTANCE));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleNoChildren() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;
        assertThat(expression.match(Stream.of(new StringNBT("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;
        ListNBT list = new ListNBT();
        list.add(new StringNBT("a"));
        list.add(new StringNBT("b"));
        list.add(new StringNBT("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new IntNBT(3))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;
        CompoundNBT tag = new CompoundNBT();
        tag.put("1", new StringNBT("a"));
        tag.put("2", new StringNBT("b"));
        tag.put("3", new StringNBT("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new IntNBT(3))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;

        ListNBT list1 = new ListNBT();
        list1.add(new StringNBT("a1"));
        list1.add(new StringNBT("b1"));
        list1.add(new StringNBT("c1"));

        ListNBT list2 = new ListNBT();
        list2.add(new StringNBT("a2"));
        list2.add(new StringNBT("b2"));

        ListNBT list3 = new ListNBT();
        list3.add(new StringNBT("a3"));
        list3.add(new StringNBT("b3"));
        list3.add(new StringNBT("c3"));

        Stream<INBT> stream = Stream.of(
                list1,
                list2,
                list3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new IntNBT(3),
                        new IntNBT(2),
                        new IntNBT(3)
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerLength.Expression.INSTANCE;

        CompoundNBT tag1 = new CompoundNBT();
        tag1.put("1a", new StringNBT("a1"));
        tag1.put("2a", new StringNBT("b1"));
        tag1.put("3a", new StringNBT("c1"));

        CompoundNBT tag2 = new CompoundNBT();
        tag2.put("1b", new StringNBT("a2"));
        tag2.put("2b", new StringNBT("b2"));

        CompoundNBT tag3 = new CompoundNBT();
        tag3.put("1c", new StringNBT("a3"));
        tag3.put("2c", new StringNBT("b3"));
        tag3.put("3c", new StringNBT("c3"));

        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        new IntNBT(3),
                        new IntNBT(2),
                        new IntNBT(3)
                )));
    }

}
