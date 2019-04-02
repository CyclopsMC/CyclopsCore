package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathExpressionMatches {

    private Stream<NBTBase> stream;
    private NbtPathExpressionMatches matches;

    @Before
    public void beforeEach() {
        stream = Stream.of(
                new NBTTagString("a"),
                new NBTTagString("b"),
                new NBTTagString("c")
        );
        matches = new NbtPathExpressionMatches(stream);
    }

    @Test
    public void testGetMatches() {
        assertThat(matches.getMatches(), is(stream));
    }

    @Test
    public void testForAll() {
        matches = NbtPathExpressionMatches.forAll(new NBTTagString("a"), new NBTTagString("b"), new NBTTagString("c"));
        assertThat(matches.getMatches().collect(Collectors.toList()), equalTo(stream.collect(Collectors.toList())));
    }

}
