package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerAllChildren {

    private NbtPathExpressionParseHandlerAllChildren handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerAllChildren();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf(".", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("*", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa*", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("*aaa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(1));
        assertThat(result.getPrefixExpression(), is(NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleNoChildren() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        NBTTagList list = new NBTTagList();
        list.appendTag(new NBTTagString("a"));
        list.appendTag(new NBTTagString("b"));
        list.appendTag(new NBTTagString("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagString("a"), new NBTTagString("b"), new NBTTagString("c"))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("1", new NBTTagString("a"));
        tag.setTag("2", new NBTTagString("b"));
        tag.setTag("3", new NBTTagString("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagString("a"), new NBTTagString("b"), new NBTTagString("c"))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        NBTTagList list1 = new NBTTagList();
        list1.appendTag(new NBTTagString("a1"));
        list1.appendTag(new NBTTagString("b1"));
        list1.appendTag(new NBTTagString("c1"));

        NBTTagList list2 = new NBTTagList();
        list2.appendTag(new NBTTagString("a2"));
        list2.appendTag(new NBTTagString("b2"));
        list2.appendTag(new NBTTagString("c2"));

        NBTTagList list3 = new NBTTagList();
        list3.appendTag(new NBTTagString("a3"));
        list3.appendTag(new NBTTagString("b3"));
        list3.appendTag(new NBTTagString("c3"));

        Stream<NBTBase> stream = Stream.of(
                list1,
                list2,
                list3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        new NBTTagString("a1"),
                        new NBTTagString("b1"),
                        new NBTTagString("c1"),
                        new NBTTagString("a2"),
                        new NBTTagString("b2"),
                        new NBTTagString("c2"),
                        new NBTTagString("a3"),
                        new NBTTagString("b3"),
                        new NBTTagString("c3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setTag("1a", new NBTTagString("a1"));
        tag1.setTag("2a", new NBTTagString("b1"));
        tag1.setTag("3a", new NBTTagString("c1"));

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setTag("1b", new NBTTagString("a2"));
        tag2.setTag("2b", new NBTTagString("b2"));
        tag2.setTag("3b", new NBTTagString("c2"));

        NBTTagCompound tag3 = new NBTTagCompound();
        tag3.setTag("1c", new NBTTagString("a3"));
        tag3.setTag("2c", new NBTTagString("b3"));
        tag3.setTag("3c", new NBTTagString("c3"));

        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        new NBTTagString("a1"),
                        new NBTTagString("b1"),
                        new NBTTagString("c1"),
                        new NBTTagString("a2"),
                        new NBTTagString("b2"),
                        new NBTTagString("c2"),
                        new NBTTagString("a3"),
                        new NBTTagString("b3"),
                        new NBTTagString("c3")
                )));
    }

}
