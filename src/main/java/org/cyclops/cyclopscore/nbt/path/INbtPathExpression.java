package org.cyclops.cyclopscore.nbt.path;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.Constants;
import org.cyclops.cyclopscore.nbt.path.navigate.INbtPathNavigation;
import org.cyclops.cyclopscore.nbt.path.parse.NbtPathExpressionExecutionContext;

import javax.annotation.Nullable;
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
    public default NbtPathExpressionMatches match(Stream<INBT> nbts) {
        return matchContexts(nbts.map(NbtPathExpressionExecutionContext::new));
    }

    /**
     * Find all matches for the given NBT tag.
     * @param nbt An NBT tag.
     * @return The matches.
     */
    public default NbtPathExpressionMatches match(INBT nbt) {
        return match(Stream.of(nbt));
    }

    /**
     * Test if any of the given NBT tags in the given stream match with the expression.
     * @param nbts A stream of NBT tags.
     * @return True if there is at least one match.
     */
    public default boolean test(Stream<INBT> nbts) {
        return this.match(nbts.limit(1))
                .getMatches()
                .findAny()
                .filter(tag -> tag.getId() != Constants.NBT.TAG_BYTE || ((ByteNBT) tag).getAsByte() == (byte) 1) // Filter truthy values
                .isPresent();
    }

    /**
     * Test if the given NBT tag matches with the expression.
     * @param nbt An NBT tag.
     * @return True if there is at least one match.
     */
    public default boolean test(INBT nbt) {
        return test(Stream.of(nbt));
    }

    /**
     * Find all matches for the given stream of NBT tags.
     * @param executionContexts A stream of NBT execution contexts.
     * @return The matches.
     */
    public NbtPathExpressionMatches matchContexts(Stream<NbtPathExpressionExecutionContext> executionContexts);

    /**
     * Create a navigation for this expression with the given navigation as child.
     * If no child is passed, the created navigation is a leaf.
     * @param child An option child.
     * @return A navigation path.
     * @throws NbtParseException If this expression can not be expressed as a navigation.
     */
    default INbtPathNavigation asNavigation(@Nullable INbtPathNavigation child) throws NbtParseException {
        throw new NbtParseException("This NBT Path expression has no navigation keys.");
    }

    /**
     * Create a navigation for this expression.
     * @return A navigation path.
     * @throws NbtParseException If this expression can not be expressed as a navigation.
     */
    public default INbtPathNavigation asNavigation() throws NbtParseException {
        return this.asNavigation(null);
    }

}
