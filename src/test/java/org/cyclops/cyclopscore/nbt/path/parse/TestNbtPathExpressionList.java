package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import org.cyclops.cyclopscore.nbt.path.NbtPathExpressionList;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionList {

    @Test
    public void testEmptyList() {
        NbtPathExpressionList list = new NbtPathExpressionList();
        Stream<NBTBase> stream = Stream.of(new NBTTagString("a"));
        assertThat(list.match(stream).getMatches(), is(stream));
    }

    @Test
    public void testList3() {
        NbtPathExpressionList list = new NbtPathExpressionList(
                new NbtPathExpressionParseHandlerChild.Expression("a"),
                new NbtPathExpressionParseHandlerChild.Expression("b"),
                new NbtPathExpressionParseHandlerChild.Expression("c")
        );

        NBTTagCompound tag1 = new NBTTagCompound();
        NBTTagCompound tag2 = new NBTTagCompound();
        NBTTagCompound tag3 = new NBTTagCompound();
        tag1.setTag("a", tag2);
        tag2.setTag("b", tag3);
        tag3.setString("c", "x");

        Stream<NBTBase> stream = Stream.of(tag1);
        assertThat(list.match(stream).getMatches().collect(Collectors.toList()), is(Lists.newArrayList(
                new NBTTagString("x")
        )));
    }

}
