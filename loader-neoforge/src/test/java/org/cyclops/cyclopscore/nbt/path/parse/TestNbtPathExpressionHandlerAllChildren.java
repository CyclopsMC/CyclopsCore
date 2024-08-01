package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
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
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamSingleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        ListTag list = new ListTag();
        list.add(StringTag.valueOf("a"));
        list.add(StringTag.valueOf("b"));
        list.add(StringTag.valueOf("c"));
        assertThat(expression.match(Stream.of(list)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringTag.valueOf("a"), StringTag.valueOf("b"), StringTag.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamSingleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;
        CompoundTag tag = new CompoundTag();
        tag.put("1", StringTag.valueOf("a"));
        tag.put("2", StringTag.valueOf("b"));
        tag.put("3", StringTag.valueOf("c"));
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringTag.valueOf("a"), StringTag.valueOf("b"), StringTag.valueOf("c"))));
    }

    @Test
    public void testExpressionStreamMultipleTagList() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        ListTag list1 = new ListTag();
        list1.add(StringTag.valueOf("a1"));
        list1.add(StringTag.valueOf("b1"));
        list1.add(StringTag.valueOf("c1"));

        ListTag list2 = new ListTag();
        list2.add(StringTag.valueOf("a2"));
        list2.add(StringTag.valueOf("b2"));
        list2.add(StringTag.valueOf("c2"));

        ListTag list3 = new ListTag();
        list3.add(StringTag.valueOf("a3"));
        list3.add(StringTag.valueOf("b3"));
        list3.add(StringTag.valueOf("c3"));

        Stream<Tag> stream = Stream.of(
                list1,
                list2,
                list3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("a1"),
                        StringTag.valueOf("b1"),
                        StringTag.valueOf("c1"),
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("b2"),
                        StringTag.valueOf("c2"),
                        StringTag.valueOf("a3"),
                        StringTag.valueOf("b3"),
                        StringTag.valueOf("c3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagCompound() {
        INbtPathExpression expression = NbtPathExpressionParseHandlerAllChildren.Expression.INSTANCE;

        CompoundTag tag1 = new CompoundTag();
        tag1.put("1a", StringTag.valueOf("a1"));
        tag1.put("2a", StringTag.valueOf("b1"));
        tag1.put("3a", StringTag.valueOf("c1"));

        CompoundTag tag2 = new CompoundTag();
        tag2.put("1b", StringTag.valueOf("a2"));
        tag2.put("2b", StringTag.valueOf("b2"));
        tag2.put("3b", StringTag.valueOf("c2"));

        CompoundTag tag3 = new CompoundTag();
        tag3.put("1c", StringTag.valueOf("a3"));
        tag3.put("2c", StringTag.valueOf("b3"));
        tag3.put("3c", StringTag.valueOf("c3"));

        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toSet()),
                is(Sets.newHashSet(
                        StringTag.valueOf("a1"),
                        StringTag.valueOf("b1"),
                        StringTag.valueOf("c1"),
                        StringTag.valueOf("a2"),
                        StringTag.valueOf("b2"),
                        StringTag.valueOf("c2"),
                        StringTag.valueOf("a3"),
                        StringTag.valueOf("b3"),
                        StringTag.valueOf("c3")
                )));
    }

}
