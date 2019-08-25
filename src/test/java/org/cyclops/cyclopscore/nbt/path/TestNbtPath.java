package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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

        StringNBT tag1 = new StringNBT("a");
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

        StringNBT tag1 = new StringNBT("a");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
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

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
        StringNBT tag5 = new StringNBT("y");
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

        CompoundNBT tagFail1 = new CompoundNBT();
        ListNBT tagFail2 = new ListNBT();
        tagFail2.add(new StringNBT("f0"));
        tagFail1.put("a", tagFail2);

        CompoundNBT tag1 = new CompoundNBT();
        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("a0"));
        tag2.add(new StringNBT("a1"));
        tag2.add(new StringNBT("a2"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a1")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a1")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseUnion() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[0,2]");

        CompoundNBT tagFail1 = new CompoundNBT();
        CompoundNBT tagFail2 = new CompoundNBT();
        tagFail2.put("a", new StringNBT("f0"));
        tagFail1.put("a", tagFail2);

        CompoundNBT tag1 = new CompoundNBT();
        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("a0"));
        tag2.add(new StringNBT("a1"));
        tag2.add(new StringNBT("a2"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a0"),
                new StringNBT("a2")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a0"),
                new StringNBT("a2")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseSlice() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[1:3:2]");

        CompoundNBT tagFail1 = new CompoundNBT();
        CompoundNBT tagFail2 = new CompoundNBT();
        tagFail2.put("a", new StringNBT("f0"));
        tagFail1.put("a", tagFail2);

        CompoundNBT tag1 = new CompoundNBT();
        ListNBT tag2 = new ListNBT();
        tag2.add(new StringNBT("a0"));
        tag2.add(new StringNBT("a1"));
        tag2.add(new StringNBT("a2"));
        tag2.add(new StringNBT("a3"));
        tag1.put("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a1"),
                new StringNBT("a3")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new StringNBT("a1"),
                new StringNBT("a3")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseLength() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a.length");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        StringNBT tag4 = new StringNBT("x");
        StringNBT tag5 = new StringNBT("y");
        tag2.put("a", tag3);
        tag3.put("b", tag4);
        tag3.put("c", tag5);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new IntNBT(2)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new IntNBT(2)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseFilterExpression() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[?(@.b)]");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        ListNBT tag3 = new ListNBT();
        CompoundNBT tag4 = new CompoundNBT();
        CompoundNBT tag5 = new CompoundNBT();
        tag2.put("a", tag3);
        tag3.add(tag4);
        tag3.add(tag5);
        tag4.putString("b", "B");
        tag4.putString("a", "A");

        ListNBT tag3Filtered = new ListNBT();
        tag3Filtered.add(tag4);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        List<INBT> expected = Lists.newArrayList();
        expected.add(tag3Filtered);
        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(expected));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalLessThan() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a < 2");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalLessThanOrEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a <= 2");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseRelationalGreaterThan() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a > 2");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseRelationalGreaterThanOrEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a >= 2");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseRelationalEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a == 2");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putInt("a", 1);

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 0)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(false));
        assertThat(expression.test(tag2), is(false));
    }

    @Test
    public void testParseStringEqual() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a == \"b\"");

        StringNBT tag1 = new StringNBT("a");
        CompoundNBT tag2 = new CompoundNBT();
        tag2.putString("a", "b");

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tag1)), is(false));
        assertThat(expression.test(tag1), is(false));

        assertThat(expression.match(Stream.of(tag2)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.match(tag2).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new ByteNBT((byte) 1)
        )));
        assertThat(expression.test(Stream.of(tag2)), is(true));
        assertThat(expression.test(tag2), is(true));
    }

    @Test
    public void testParseFilterExpressionComplex() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[?(@.c == \"B\")]");

        CompoundNBT tag1 = new CompoundNBT();
        ListNBT tag1_1 = new ListNBT();
        CompoundNBT tag1_1_1 = new CompoundNBT();
        CompoundNBT tag1_1_2 = new CompoundNBT();
        CompoundNBT tag1_1_3 = new CompoundNBT();
        tag1.put("a", tag1_1);
        tag1_1.add(tag1_1_1);
        tag1_1_1.putString("notC", "b");
        tag1_1.add(tag1_1_2);
        tag1_1_2.putString("c", "B");
        tag1_1.add(tag1_1_3);
        tag1_1_3.putString("c", "C");

        ListNBT tag1_1Filtered = new ListNBT();
        tag1_1Filtered.add(tag1_1_2);

        List<INBT> expected = Lists.newArrayList();
        expected.add(tag1_1Filtered);
        assertThat(expression.match(Stream.of(
                tag1
        )).getMatches().collect(Collectors.toList()), equalTo(expected));
    }

}
