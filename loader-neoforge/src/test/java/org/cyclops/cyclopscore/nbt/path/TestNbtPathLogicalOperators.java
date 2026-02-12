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

    @Test
    public void testParseLogicalWithFilterExpression() throws NbtParseException {
        // Test filter expression with OR: [?(@.value < 5 || > 10)]
        INbtPathExpression expression = NbtPath.parse("$.items[?(@.value < 5 || > 10)]");

        CompoundTag root = new CompoundTag();
        ListTag items = new ListTag();
        
        CompoundTag item1 = new CompoundTag();
        item1.putInt("value", 3);  // 3 < 5 is true, should match
        
        CompoundTag item2 = new CompoundTag();
        item2.putInt("value", 7);  // 7 < 5 is false, 7 > 10 is false, should not match
        
        CompoundTag item3 = new CompoundTag();
        item3.putInt("value", 15);  // 15 < 5 is false, 15 > 10 is true, should match
        
        items.add(item1);
        items.add(item2);
        items.add(item3);
        root.put("items", items);
        
        ListTag expectedFiltered = new ListTag();
        expectedFiltered.add(item1);
        expectedFiltered.add(item3);
        
        List<Tag> expected = Lists.newArrayList(expectedFiltered);
        assertThat(expression.match(Stream.of(root)).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.test(root), is(true));
    }

    @Test
    public void testParseLogicalWithFilterExpressionAnd() throws NbtParseException {
        // Test filter expression with AND: [?(@.min < 10 && > 5)]
        INbtPathExpression expression = NbtPath.parse("$.items[?(@.min < 10 && > 5)]");

        CompoundTag root = new CompoundTag();
        ListTag items = new ListTag();
        
        CompoundTag item1 = new CompoundTag();
        item1.putInt("min", 7);  // 7 < 10 is true, 7 > 5 is true, should match
        
        CompoundTag item2 = new CompoundTag();
        item2.putInt("min", 3);  // 3 < 10 is true, 3 > 5 is false, should not match
        
        CompoundTag item3 = new CompoundTag();
        item3.putInt("min", 12);  // 12 < 10 is false, should not match
        
        items.add(item1);
        items.add(item2);
        items.add(item3);
        root.put("items", items);
        
        ListTag expectedFiltered = new ListTag();
        expectedFiltered.add(item1);
        
        List<Tag> expected = Lists.newArrayList(expectedFiltered);
        assertThat(expression.match(Stream.of(root)).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.test(root), is(true));
    }

    @Test
    public void testParseLogicalNotWithFilterExpression() throws NbtParseException {
        // Test filter expression with NOT: [?(!(@.active == 1))]
        INbtPathExpression expression = NbtPath.parse("$.items[?(!(@.active == 1))]");

        CompoundTag root = new CompoundTag();
        ListTag items = new ListTag();
        
        CompoundTag item1 = new CompoundTag();
        item1.putInt("active", 0);  // 0 == 1 is false, !(false) is true, should match
        
        CompoundTag item2 = new CompoundTag();
        item2.putInt("active", 1);  // 1 == 1 is true, !(true) is false, should not match
        
        CompoundTag item3 = new CompoundTag();
        item3.putInt("active", 0);  // 0 == 1 is false, !(false) is true, should match
        
        items.add(item1);
        items.add(item2);
        items.add(item3);
        root.put("items", items);
        
        ListTag expectedFiltered = new ListTag();
        expectedFiltered.add(item1);
        expectedFiltered.add(item3);
        
        List<Tag> expected = Lists.newArrayList(expectedFiltered);
        assertThat(expression.match(Stream.of(root)).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.test(root), is(true));
    }
}
