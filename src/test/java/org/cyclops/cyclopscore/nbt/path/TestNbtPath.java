package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.junit.Test;

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

        NBTTagString tag1 = new NBTTagString("a");
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

        NBTTagString tag1 = new NBTTagString("a");
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

        NBTTagString tag1 = new NBTTagString("a");
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        NBTTagString tag4 = new NBTTagString("x");
        tag2.setTag("a", tag3);
        tag3.setTag("b", tag4);

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

        NBTTagString tag1 = new NBTTagString("a");
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        NBTTagString tag4 = new NBTTagString("x");
        tag2.setTag("a", tag3);
        tag3.setTag("b", tag4);

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

        NBTTagString tag1 = new NBTTagString("a");
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        NBTTagString tag4 = new NBTTagString("x");
        tag2.setTag("a", tag3);
        tag3.setTag("b", tag4);

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

        NBTTagString tag1 = new NBTTagString("a");
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        NBTTagString tag4 = new NBTTagString("x");
        tag2.setTag("a", tag3);
        tag3.setTag("b", tag4);

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

        NBTTagString tag1 = new NBTTagString("a");
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        NBTTagString tag4 = new NBTTagString("x");
        NBTTagString tag5 = new NBTTagString("y");
        tag2.setTag("a", tag3);
        tag3.setTag("b", tag4);
        tag3.setTag("c", tag5);

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

        NBTTagCompound tagFail1 = new NBTTagCompound();
        NBTTagList tagFail2 = new NBTTagList();
        tagFail2.appendTag(new NBTTagString("f0"));
        tagFail1.setTag("a", tagFail2);

        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("a0"));
        tag2.appendTag(new NBTTagString("a1"));
        tag2.appendTag(new NBTTagString("a2"));
        tag1.setTag("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a1")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a1")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseUnion() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[0,2]");

        NBTTagCompound tagFail1 = new NBTTagCompound();
        NBTTagCompound tagFail2 = new NBTTagCompound();
        tagFail2.setTag("a", new NBTTagString("f0"));
        tagFail1.setTag("a", tagFail2);

        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("a0"));
        tag2.appendTag(new NBTTagString("a1"));
        tag2.appendTag(new NBTTagString("a2"));
        tag1.setTag("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a0"),
                new NBTTagString("a2")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a0"),
                new NBTTagString("a2")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

    @Test
    public void testParseSlice() throws NbtParseException {
        INbtPathExpression expression = NbtPath.parse("$.a[1:3:2]");

        NBTTagCompound tagFail1 = new NBTTagCompound();
        NBTTagCompound tagFail2 = new NBTTagCompound();
        tagFail2.setTag("a", new NBTTagString("f0"));
        tagFail1.setTag("a", tagFail2);

        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagList tag2 = new NBTTagList();
        tag2.appendTag(new NBTTagString("a0"));
        tag2.appendTag(new NBTTagString("a1"));
        tag2.appendTag(new NBTTagString("a2"));
        tag2.appendTag(new NBTTagString("a3"));
        tag1.setTag("a", tag2);

        assertThat(expression.match(Stream.of(tagFail1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.match(tagFail1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList()));
        assertThat(expression.test(Stream.of(tagFail1)), is(false));
        assertThat(expression.test(tagFail1), is(false));

        assertThat(expression.match(Stream.of(tag1)).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a1"),
                new NBTTagString("a3")
        )));
        assertThat(expression.match(tag1).getMatches().collect(Collectors.toList()), equalTo(Lists.newArrayList(
                new NBTTagString("a1"),
                new NBTTagString("a3")
        )));
        assertThat(expression.test(Stream.of(tag1)), is(true));
        assertThat(expression.test(tag1), is(true));
    }

}
