package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
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

public class TestNbtPathExpressionHandlerChild {

    private NbtPathExpressionParseHandlerChild handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerChild();
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchNoChildName() {
        assertThat(handler.handlePrefixOf(".", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchInvalidChildName() {
        assertThat(handler.handlePrefixOf(".)", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchSingle() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".abc", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc"));
    }

    @Test
    public void testMatchSingleMixedCasing() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".aBc", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("aBc"));
    }

    @Test
    public void testMatchSingleNumbers() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".abc012", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(7));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc012"));
    }

    @Test
    public void testMatchSingleUnderscores() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".a_b_c", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("a_b_c"));
    }

    @Test
    public void testMatchSingleLater() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("aa.abc", 2);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerChild.Expression) result.getPrefixExpression()).getChildName(), equalTo("abc"));
    }

    @Test
    public void testNonMatchNoChildNameLater() {
        assertThat(handler.handlePrefixOf("aa.", 2),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchLonger() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(".aaa$aa", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerChild.Expression.class));
    }

    @Test
    public void testMatchNavigation() throws NbtParseException {
        INbtPathExpression expression = handler.handlePrefixOf(".abc", 0).getPrefixExpression();
        assertThat(expression.asNavigation(NbtPathNavigationLeafWildcard.INSTANCE), instanceOf(NbtPathNavigationAdapter.class));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleLeaf() {
        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
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
        CompoundTag tag = new CompoundTag();
        tag.putString("def", "123");

        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList()));
    }

    @Test
    public void testExpressionStreamMultipleTagsNoMatch() {
        CompoundTag tag1 = new CompoundTag();
        tag1.putString("def", "1");
        CompoundTag tag2 = new CompoundTag();
        tag2.putString("def", "2");
        CompoundTag tag3 = new CompoundTag();
        tag3.putString("def", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
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
        CompoundTag tag = new CompoundTag();
        tag.putString("abc", "123");

        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(tag)).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("123")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsMatch() {
        CompoundTag tag1 = new CompoundTag();
        tag1.putString("abc", "1");
        CompoundTag tag2 = new CompoundTag();
        tag2.putString("abc", "2");
        CompoundTag tag3 = new CompoundTag();
        tag3.putString("abc", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("1"),
                        StringTag.valueOf("2"),
                        StringTag.valueOf("3")
                )));
    }

    @Test
    public void testExpressionStreamMultipleTagsPartialMatch() {
        CompoundTag tag1 = new CompoundTag();
        tag1.putString("abc", "1");
        CompoundTag tag2 = new CompoundTag();
        tag2.putString("def", "2");
        CompoundTag tag3 = new CompoundTag();
        tag3.putString("abc", "3");

        INbtPathExpression expression = handler.handlePrefixOf("aa.abc", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                tag1,
                tag2,
                tag3
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        StringTag.valueOf("1"),
                        StringTag.valueOf("3")
                )));
    }

}
