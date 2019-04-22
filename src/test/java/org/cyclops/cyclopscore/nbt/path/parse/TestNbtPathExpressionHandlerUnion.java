package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.cyclops.cyclopscore.nbt.path.NbtParseException;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationAdapter;
import org.cyclops.cyclopscore.nbt.path.navigate.NbtPathNavigationLeafWildcard;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerUnion {

    private NbtPathExpressionParseHandlerUnion handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerUnion();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf(".", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchFew() {
        assertThat(handler.handlePrefixOf("[0]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchInvalid() {
        assertThat(handler.handlePrefixOf("[,]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNoMatchMixed() {
        assertThat(handler.handlePrefixOf("[aa,0]", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchTwoInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[0,1]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(5));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerUnion.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildIndexes(), equalTo(Lists.newArrayList(0, 1)));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildNames(), equalTo(Lists.newArrayList()));
    }

    @Test
    public void testMatchThreeInt() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[0,1,2]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerUnion.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildIndexes(), equalTo(Lists.newArrayList(0, 1, 2)));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildNames(), equalTo(Lists.newArrayList()));
    }

    @Test
    public void testMatchTwoString() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[aa,bb]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerUnion.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildIndexes(), equalTo(Lists.newArrayList()));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildNames(), equalTo(Lists.newArrayList("aa", "bb")));
    }

    @Test
    public void testMatchThreeString() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("[aa,bb,cc]", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(10));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerUnion.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildIndexes(), equalTo(Lists.newArrayList()));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildNames(), equalTo(Lists.newArrayList("aa", "bb", "cc")));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa[0,1,2]", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerUnion.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildIndexes(), equalTo(Lists.newArrayList(0, 1, 2)));
        assertThat(((NbtPathExpressionParseHandlerUnion.Expression) result.getPrefixExpression()).getChildNames(), equalTo(Lists.newArrayList()));
    }

    @Test
    public void testMatchNavigation() throws NbtParseException {
        INbtPathExpression expression = handler.handlePrefixOf("[a,b]", 0).getPrefixExpression();
        assertThat(expression.asNavigation(NbtPathNavigationLeafWildcard.INSTANCE), instanceOf(NbtPathNavigationAdapter.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMatchNavigationList() throws NbtParseException {
        INbtPathExpression expression = handler.handlePrefixOf("[0,1]", 0).getPrefixExpression();
        expression.asNavigation(NbtPathNavigationLeafWildcard.INSTANCE);
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList(), Lists.newArrayList());
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleNoChildren() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));
        assertThat(expression.match(Stream.of(new NBTTagString("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));
        NBTTagList list = new NBTTagList();
        list.appendTag(new NBTTagString("a"));
        list.appendTag(new NBTTagString("b"));
        list.appendTag(new NBTTagString("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagString("a"), new NBTTagString("c"))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("1", new NBTTagString("a"));
        tag.setTag("2", new NBTTagString("b"));
        tag.setTag("3", new NBTTagString("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new NBTTagString("a"), new NBTTagString("c"))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));

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
                        new NBTTagString("c1"),
                        new NBTTagString("a2"),
                        new NBTTagString("c2"),
                        new NBTTagString("a3"),
                        new NBTTagString("c3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));

        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setTag("1", new NBTTagString("a1"));
        tag1.setTag("2", new NBTTagString("b1"));
        tag1.setTag("3", new NBTTagString("c1"));

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setTag("1", new NBTTagString("a2"));
        tag2.setTag("2", new NBTTagString("b2"));
        tag2.setTag("3", new NBTTagString("c2"));

        NBTTagCompound tag3 = new NBTTagCompound();
        tag3.setTag("1", new NBTTagString("a3"));
        tag3.setTag("2", new NBTTagString("b3"));
        tag3.setTag("3", new NBTTagString("c3"));

        Stream<NBTBase> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        new NBTTagString("a1"),
                        new NBTTagString("c1"),
                        new NBTTagString("a2"),
                        new NBTTagString("c2"),
                        new NBTTagString("a3"),
                        new NBTTagString("c3")
                )));
    }

}
