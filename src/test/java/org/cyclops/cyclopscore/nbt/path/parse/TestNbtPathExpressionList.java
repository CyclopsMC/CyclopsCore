package org.cyclops.cyclopscore.nbt.path.parse;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
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
        Stream<Tag> stream = Stream.of(StringTag.valueOf("a"));
        assertThat(list.match(stream).getMatches().collect(Collectors.toList()),
                is(Lists.newArrayList(StringTag.valueOf("a"))));
    }

    @Test
    public void testList3() {
        NbtPathExpressionList list = new NbtPathExpressionList(
                new NbtPathExpressionParseHandlerChild.Expression("a"),
                new NbtPathExpressionParseHandlerChild.Expression("b"),
                new NbtPathExpressionParseHandlerChild.Expression("c")
        );

        CompoundTag tag1 = new CompoundTag();
        CompoundTag tag2 = new CompoundTag();
        CompoundTag tag3 = new CompoundTag();
        tag1.put("a", tag2);
        tag2.put("b", tag3);
        tag3.putString("c", "x");

        Stream<Tag> stream = Stream.of(tag1);
        assertThat(list.match(stream).getMatches().collect(Collectors.toList()), is(Lists.newArrayList(
                StringTag.valueOf("x")
        )));
    }

}
