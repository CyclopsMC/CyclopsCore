package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.NBTBase;

import java.util.stream.Stream;

/**
 * A data object representing the result of executing
 * an NBT path expression against an NBT tag.
 */
public class NbtPathExpressionMatches {

    public static NbtPathExpressionMatches EMPTY = NbtPathExpressionMatches.forAll();

    private final Stream<NBTBase> matches;

    public NbtPathExpressionMatches(Stream<NBTBase> matches) {
        this.matches = matches;
    }

    public Stream<NBTBase> getMatches() {
        return matches;
    }

    public static NbtPathExpressionMatches forAll(NBTBase... nbts) {
        return new NbtPathExpressionMatches(Stream.of(nbts));
    }

}
