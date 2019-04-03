package org.cyclops.cyclopscore.nbt.path;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
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

}
