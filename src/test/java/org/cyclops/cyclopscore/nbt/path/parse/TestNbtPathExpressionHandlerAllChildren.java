package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationLeafWildcard;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationLinkWildcard;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
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
    public void testMatchNavigation() throws NbtParseException {
        INbtPathExpression expression = handler.handlePrefixOf("*", 0).getPrefixExpression();
        assertThat(expression.asNavigation(null), is(NbtPathNavigationLeafWildcard.INSTANCE));
        assertThat(expression.asNavigation(NbtPathNavigationLeafWildcard.INSTANCE), instanceOf(NbtPathNavigationLinkWildcard.class));
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
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        ListNBT list = new ListNBT();
        list.add(StringNBT.valueOf("a"));
        list.add(StringNBT.valueOf("b"));
        list.add(StringNBT.valueOf("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("a"), StringNBT.valueOf("b"), StringNBT.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        CompoundNBT tag = new CompoundNBT();
        tag.put("1", StringNBT.valueOf("a"));
        tag.put("2", StringNBT.valueOf("b"));
        tag.put("3", StringNBT.valueOf("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("a"), StringNBT.valueOf("b"), StringNBT.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        ListNBT list1 = new ListNBT();
        list1.add(StringNBT.valueOf("a1"));
        list1.add(StringNBT.valueOf("b1"));
        list1.add(StringNBT.valueOf("c1"));

        ListNBT list2 = new ListNBT();
        list2.add(StringNBT.valueOf("a2"));
        list2.add(StringNBT.valueOf("b2"));
        list2.add(StringNBT.valueOf("c2"));

        ListNBT list3 = new ListNBT();
        list3.add(StringNBT.valueOf("a3"));
        list3.add(StringNBT.valueOf("b3"));
        list3.add(StringNBT.valueOf("c3"));

        Stream<INBT> stream = Stream.of(
                list1,
                list2,
                list3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("b1"),
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("b2"),
                        StringNBT.valueOf("c2"),
                        StringNBT.valueOf("a3"),
                        StringNBT.valueOf("b3"),
                        StringNBT.valueOf("c3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        CompoundNBT tag1 = new CompoundNBT();
        tag1.put("1a", StringNBT.valueOf("a1"));
        tag1.put("2a", StringNBT.valueOf("b1"));
        tag1.put("3a", StringNBT.valueOf("c1"));

        CompoundNBT tag2 = new CompoundNBT();
        tag2.put("1b", StringNBT.valueOf("a2"));
        tag2.put("2b", StringNBT.valueOf("b2"));
        tag2.put("3b", StringNBT.valueOf("c2"));

        CompoundNBT tag3 = new CompoundNBT();
        tag3.put("1c", StringNBT.valueOf("a3"));
        tag3.put("2c", StringNBT.valueOf("b3"));
        tag3.put("3c", StringNBT.valueOf("c3"));

        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("b1"),
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("b2"),
                        StringNBT.valueOf("c2"),
                        StringNBT.valueOf("a3"),
                        StringNBT.valueOf("b3"),
                        StringNBT.valueOf("c3")
                )));
    }

}
