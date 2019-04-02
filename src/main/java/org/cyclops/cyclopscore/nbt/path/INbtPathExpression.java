package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.NBTBase;

import java.util.stream.Stream;

/**
 * A parsed NBT path expression.
 */
public interface INbtPathExpression {

    /**
     * Find all matches for the given stream of NBT tags.
     * @param nbts A stream of NBT tags.
     * @return The matches.
     */
    public NbtPathExpressionMatches match(Stream<NBTBase> nbts);

    /**
     * Find all matches for the given NBT tag.
     * @param nbt An NBT tag.
     * @return The matches.
     */
    public default NbtPathExpressionMatches match(NBTBase nbt) {
        return match(Stream.of(nbt));
    }

    /**
     * Test if any of the given NBT tags in the given stream match with the expression.
     * @param nbts A stream of NBT tags.
     * @return True if there is at least one match.
     */
    public default boolean test(Stream<NBTBase> nbts) {
        return this.match(nbts.limit(1))
                .getMatches()
                .findAny()
                .isPresent();
    }

    /**
     * Test if the given NBT tag matches with the expression.
     * @param nbt An NBT tag.
     * @return True if there is at least one match.
     */
    public default boolean test(NBTBase nbt) {
        return test(Stream.of(nbt));
    }

}
