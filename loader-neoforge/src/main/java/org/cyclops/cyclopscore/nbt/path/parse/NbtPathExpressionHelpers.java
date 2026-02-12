package org.cyclops.cyclopscore.nbt.path.parse;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.Tag;

/**
 * Utility methods for NBT path expression handling.
 */
public class NbtPathExpressionHelpers {

    /**
     * Determine if a tag is truthy.
     * ByteTag with value 1 is true, 0 is false.
     * Any other non-null tag is considered true.
     * This follows the same logic as {@link org.cyclops.cyclopscore.nbt.path.INbtPathExpression#test(Tag)}.
     *
     * @param tag The tag to check
     * @return true if the tag is truthy, false otherwise
     */
    public static boolean isTruthy(Tag tag) {
        if (tag == null) {
            return false;
        }
        if (tag.getId() == Tag.TAG_BYTE) {
            return ((ByteTag) tag).getAsByte() == (byte) 1;
        }
        // Non-null non-ByteTags are truthy
        return true;
    }
}
