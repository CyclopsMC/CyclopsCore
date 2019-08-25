package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
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
        Stream<INBT> stream = Stream.of(new StringNBT("a"));
        assertThat(list.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(new StringNBT("a"))));
    }

    @Test
    public void testList3() {
        NbtPathExpressionList list = new NbtPathExpressionList(
                new NbtPathExpressionParseHandlerChild.Expression("a"),
                new NbtPathExpressionParseHandlerChild.Expression("b"),
                new NbtPathExpressionParseHandlerChild.Expression("c")
        );

        CompoundNBT tag1 = new CompoundNBT();
        CompoundNBT tag2 = new CompoundNBT();
        CompoundNBT tag3 = new CompoundNBT();
        tag1.put("a", tag2);
        tag2.put("b", tag3);
        tag3.putString("c", "x");

        Stream<INBT> stream = Stream.of(tag1);
        assertThat(list.match(stream).getMatches().collect(Collectors.toList()), is(Lists.newArrayList(
                new StringNBT("x")
        )));
    }

}
