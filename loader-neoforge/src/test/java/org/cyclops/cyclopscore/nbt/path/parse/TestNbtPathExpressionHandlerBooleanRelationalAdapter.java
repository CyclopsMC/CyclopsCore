package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import org.cyclops.cyclopscore.nbt.path.INbtPathExpression;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionHandlerBooleanRelationalAdapter {

    private NbtPathExpressionParseHandlerBooleanRelationalAdapter handler;

    @Before
    public void beforeEach() {
        handler = new NbtPathExpressionParseHandlerBooleanRelationalAdapter("<") {
            @Override
            protected boolean getRelationalValue(double left, double right) {
                return left < right;
            }
        };
    }

    @Test
    public void testNonMatch() {
        assertThat(handler.handlePrefixOf("$", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchGreaterThan() {
        assertThat(handler.handlePrefixOf(">", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testNonMatchTargetString() {
        assertThat(handler.handlePrefixOf("< a", 0),
                is(INbtPathExpressionParseHandler.HandleResult.INVALID));
    }

    @Test
    public void testMatchExpressionIntNoSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("<2", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(2));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(2D));
    }

    @Test
    public void testMatchExpressionIntSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(" < 2", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(4));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(2D));
    }

    @Test
    public void testMatchExpressionIntManySpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("    <         2", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(15));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(2D));
    }

    @Test
    public void testMatchExpressionDoubleNoSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("<24.23", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(6));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(24.23D));
    }

    @Test
    public void testMatchExpressionDoubleSpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf(" < 24.23", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(8));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(24.23D));
    }

    @Test
    public void testMatchExpressionDoubleManySpaces() {
        INbtPathExpressionParseHandler.HandleResult result = handler.handlePrefixOf("    <           24.23", 0);
        assertThat(result.isValid(), is(true));
        assertThat(result.getConsumedExpressionLength(), is(21));
        assertThat(result.getPrefixExpression(), instanceOf(NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression.class));
        assertThat(((NbtPathExpressionParseHandlerBooleanRelationalAdapter.Expression) result.getPrefixExpression()).getTargetDouble(), is(24.23D));
    }

    @Test
    public void testExpressionStreamEmpty() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.empty()).getMatches().collect(Collectors.toList()),
                is(Collections.emptyList()));
    }

    @Test
    public void testExpressionStreamSingleLeafString() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(StringTag.valueOf("a"))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamSingleLeafIntValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(2))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafIntInvalid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(IntTag.valueOf(3))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 0))));
    }

    @Test
    public void testExpressionStreamSingleLeafDoubleValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3.3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(DoubleTag.valueOf(2.2))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafFloatValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3.3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(FloatTag.valueOf(2.2f))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafShortValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3.3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(ShortTag.valueOf((short) 2))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamSingleLeafByteValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3.3", 2).getPrefixExpression();
        assertThat(expression.match(Stream.of(ShortTag.valueOf((byte) 1))).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(ByteTag.valueOf((byte) 1))));
    }

    @Test
    public void testExpressionStreamMultipleLeafString() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                StringTag.valueOf("a"),
                StringTag.valueOf("b"),
                StringTag.valueOf("c")
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        ByteTag.valueOf((byte) 0),
                        ByteTag.valueOf((byte) 0),
                        ByteTag.valueOf((byte) 0)
                )));
    }

    @Test
    public void testExpressionStreamMultipleLeafIntValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                IntTag.valueOf(2),
                IntTag.valueOf(1),
                IntTag.valueOf(0)
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        ByteTag.valueOf((byte) 1),
                        ByteTag.valueOf((byte) 1),
                        ByteTag.valueOf((byte) 1)
                )));
    }

    @Test
    public void testExpressionStreamMultipleLeafIntPartialValid() {
        INbtPathExpression expression = handler.handlePrefixOf("aa < 3", 2).getPrefixExpression();
        Stream<Tag> stream = Stream.of(
                IntTag.valueOf(2),
                IntTag.valueOf(3),
                IntTag.valueOf(4)
        );
        assertThat(expression.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(
                        ByteTag.valueOf((byte) 1),
                        ByteTag.valueOf((byte) 0),
                        ByteTag.valueOf((byte) 0)
                )));
    }

}
