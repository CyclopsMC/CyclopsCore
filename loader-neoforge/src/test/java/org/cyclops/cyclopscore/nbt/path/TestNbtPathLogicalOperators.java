package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathLogicalOperators {

    @Test
    public void testParseLogicalNotSimple() throws NbtParseException {
        // Test: !(@.a == 5)
        INbtPathExpression expression = NbtPath.parse("!@.a == 5");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 3);  // 3 == 5 is false, so !(false) should be true

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseLogicalNotFalse() throws NbtParseException {
        // Test: !(@.a == 5) where a is 5
        INbtPathExpression expression = NbtPath.parse("!@.a == 5");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 5);  // 5 == 5 is true, so !(true) should be false

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(tag1), is(false));
    }

    @Test
    public void testParseLogicalAndBothTrue() throws NbtParseException {
        // Test: (@.a > 2) && (< 10) with value 5
        INbtPathExpression expression = NbtPath.parse("@.a > 2 && < 10");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 5);  // 5 > 2 is true, 5 < 10 is true, so true && true = true

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseLogicalAndLeftFalse() throws NbtParseException {
        // Test: (@.a > 10) && (< 20) with value 5
        INbtPathExpression expression = NbtPath.parse("@.a > 10 && < 20");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 5);  // 5 > 10 is false, 5 < 20 is true, so false && true = false

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(tag1), is(false));
    }

    @Test
    public void testParseLogicalOrBothFalse() throws NbtParseException {
        // Test: (@.a < 2) || (> 10) with value 5
        INbtPathExpression expression = NbtPath.parse("@.a < 2 || > 10");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 5);  // 5 < 2 is false, 5 > 10 is false, so false || false = false

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(tag1), is(false));
    }

    @Test
    public void testParseLogicalOrRightTrue() throws NbtParseException {
        // Test: (@.a < 2) || (> 10) with value 15
        INbtPathExpression expression = NbtPath.parse("@.a < 2 || > 10");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 15);  // 15 < 2 is false, 15 > 10 is true, so false || true = true

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseComplexLogicalExpression() throws NbtParseException {
        // Test: (@.a > 5) && (< 15) || (== 20)
        INbtPathExpression expression = NbtPath.parse("@.a > 5 && < 15 || == 20");

        CompoundTag tag1 = new CompoundTag();
        tag1.putInt("a", 10);  // 10 > 5 is true, 10 < 15 is true, so (true && true) = true, true || false = true

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(tag1), is(true));
    }
}
