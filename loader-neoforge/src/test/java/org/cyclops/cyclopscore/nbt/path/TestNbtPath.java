package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPath {

    @Test(expected = NbtParseException.class)
    public void testParseInvalidUnknownChar() throws NbtParseException {
        NbtPath.parse("!");
    }

    @Test(expected = NbtParseException.class)
    public void testParseInvalidTooShort() throws NbtParseException {
        NbtPath.parse(".");
    }

    @Test(expected = NbtParseException.class)
    public void testParseInvalidNoNestedChildName() throws NbtParseException {
        NbtPath.parse("$.abc.def.");
    }

    @Test
    public void testParseSelf() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("");

        StringTag tag1 = StringTag.valueOf("a");
        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag1
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag1
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseRoot() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$");

        StringTag tag1 = StringTag.valueOf("a");
        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag1
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag1
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseChild() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.b");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        tag2.put("a", tag3);
        tag3.put("b", tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseChildBrackets() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$[\"a*\"].b");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        tag2.put("a*", tag3);
        tag3.put("b", tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRootAfterChild() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.b$");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        tag2.put("a", tag3);
        tag3.put("b", tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag2
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag2
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRootAfterChildEmpty() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.c$");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        tag2.put("a", tag3);
        tag3.put("b", tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseParentAfterChild() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.b..");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        tag2.put("a", tag3);
        tag3.put("b", tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag3
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag3
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseAllChildren() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a*");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        StringTag tag5 = StringTag.valueOf("y");
        tag2.put("a", tag3);
        tag3.put("b", tag4);
        tag3.put("c", tag5);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4,
                tag5
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                tag4,
                tag5
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseListElement() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[1]");

        CompoundTag tagFail1 = new CompoundTag();
        ListTag tagFail2 = new ListTag();
        tagFail2.add(StringTag.valueOf("f0"));
        tagFail1.put("a", tagFail2);

        CompoundTag tag1 = new CompoundTag();
        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("a0"));
        tag2.add(StringTag.valueOf("a1"));
        tag2.add(StringTag.valueOf("a2"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a1")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a1")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseUnion() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[0,2]");

        CompoundTag tagFail1 = new CompoundTag();
        CompoundTag tagFail2 = new CompoundTag();
        tagFail2.put("a", StringTag.valueOf("f0"));
        tagFail1.put("a", tagFail2);

        CompoundTag tag1 = new CompoundTag();
        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("a0"));
        tag2.add(StringTag.valueOf("a1"));
        tag2.add(StringTag.valueOf("a2"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a0"),
                StringTag.valueOf("a2")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a0"),
                StringTag.valueOf("a2")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseSlice() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[1:3:2]");

        CompoundTag tagFail1 = new CompoundTag();
        CompoundTag tagFail2 = new CompoundTag();
        tagFail2.put("a", StringTag.valueOf("f0"));
        tagFail1.put("a", tagFail2);

        CompoundTag tag1 = new CompoundTag();
        ListTag tag2 = new ListTag();
        tag2.add(StringTag.valueOf("a0"));
        tag2.add(StringTag.valueOf("a1"));
        tag2.add(StringTag.valueOf("a2"));
        tag2.add(StringTag.valueOf("a3"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a1"),
                StringTag.valueOf("a3")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                StringTag.valueOf("a1"),
                StringTag.valueOf("a3")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseLength() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.length");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        StringTag tag4 = StringTag.valueOf("x");
        StringTag tag5 = StringTag.valueOf("y");
        tag2.put("a", tag3);
        tag3.put("b", tag4);
        tag3.put("c", tag5);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                IntTag.valueOf(2)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                IntTag.valueOf(2)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseFilterExpression() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[?(@.b)]");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        ListTag tag3 = new ListTag();
        CompoundTag tag4 = new CompoundTag();
        CompoundTag tag5 = new CompoundTag();
        tag2.put("a", tag3);
        tag3.add(tag4);
        tag3.add(tag5);
        tag4.putString("b", "B");
        tag4.putString("a", "A");

        ListTag tag3Filtered = new ListTag();
        tag3Filtered.add(tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        List<Tag> expected = Lists.newArrayList();
        expected.add(tag3Filtered);
        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalLessThan() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a < 2");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalLessThanOrEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a <= 2");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalGreaterThan() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a > 2");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseRelationalGreaterThanOrEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a >= 2");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseRelationalEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a == 2");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseStringEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a == \"b\"");

        StringTag tag1 = StringTag.valueOf("a");
        CompoundTag tag2 = new CompoundTag();
        tag2.putString("a", "b");

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                ByteTag.valueOf((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseFilterExpressionComplex() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[?(@.c == \"B\")]");

        CompoundTag tag1 = new CompoundTag();
        ListTag tag1_1 = new ListTag();
        CompoundTag tag1_1_1 = new CompoundTag();
        CompoundTag tag1_1_2 = new CompoundTag();
        CompoundTag tag1_1_3 = new CompoundTag();
        tag1.put("a", tag1_1);
        tag1_1.add(tag1_1_1);
        tag1_1_1.putString("notC", "b");
        tag1_1.add(tag1_1_2);
        tag1_1_2.putString("c", "B");
        tag1_1.add(tag1_1_3);
        tag1_1_3.putString("c", "C");

        ListTag tag1_1Filtered = new ListTag();
        tag1_1Filtered.add(tag1_1_2);

        List<Tag> expected = Lists.newArrayList();
        expected.add(tag1_1Filtered);
        assertThat(expression.match(Stream.of(
                tag1
        )).getMatches().collect(Collectors.toList()), equalTo(expected));
    }

}
