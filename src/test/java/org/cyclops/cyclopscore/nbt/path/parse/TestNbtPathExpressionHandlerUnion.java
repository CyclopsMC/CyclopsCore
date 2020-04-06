package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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
        assertThat(expression.match(Stream.of(StringNBT.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));
        ListNBT list = new ListNBT();
        list.add(StringNBT.valueOf("a"));
        list.add(StringNBT.valueOf("b"));
        list.add(StringNBT.valueOf("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("a"), StringNBT.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));
        CompoundNBT tag = new CompoundNBT();
        tag.put("1", StringNBT.valueOf("a"));
        tag.put("2", StringNBT.valueOf("b"));
        tag.put("3", StringNBT.valueOf("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringNBT.valueOf("a"), StringNBT.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));

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
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("c2"),
                        StringNBT.valueOf("a3"),
                        StringNBT.valueOf("c3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = new NbtPathExpressionParseHandlerUnion.Expression(Lists.newArrayList("1", "3"), Lists.newArrayList(0, 2));

        CompoundNBT tag1 = new CompoundNBT();
        tag1.put("1", StringNBT.valueOf("a1"));
        tag1.put("2", StringNBT.valueOf("b1"));
        tag1.put("3", StringNBT.valueOf("c1"));

        CompoundNBT tag2 = new CompoundNBT();
        tag2.put("1", StringNBT.valueOf("a2"));
        tag2.put("2", StringNBT.valueOf("b2"));
        tag2.put("3", StringNBT.valueOf("c2"));

        CompoundNBT tag3 = new CompoundNBT();
        tag3.put("1", StringNBT.valueOf("a3"));
        tag3.put("2", StringNBT.valueOf("b3"));
        tag3.put("3", StringNBT.valueOf("c3"));

        Stream<INBT> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        StringNBT.valueOf("a1"),
                        StringNBT.valueOf("c1"),
                        StringNBT.valueOf("a2"),
                        StringNBT.valueOf("c2"),
                        StringNBT.valueOf("a3"),
                        StringNBT.valueOf("c3")
                )));
    }

}
