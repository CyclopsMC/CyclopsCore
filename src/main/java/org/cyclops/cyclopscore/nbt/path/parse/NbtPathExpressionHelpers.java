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

    /**
     * Find the end position of an expression, stopping at logical operators or closing parenthesis.
     * This method is shared by logical operator handlers to identify expression boundaries.
     *
     * @param expression The full expression string
     * @param start The starting position to search from
     * @return The position where the expression ends
     */
    public static int findExpressionEnd(String expression, int start) {
        int depth = 0;
        for (int i = start; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                if (depth == 0) {
                    return i;
                }
                depth--;
            } else if (depth == 0) {
                // Check for logical operators at top level
                if (i + 1 < expression.length()) {
                    String twoChar = expression.substring(i, i + 2);
                    if (twoChar.equals("&&") || twoChar.equals("||")) {
                        return i;
                    }
                }
                // Check for NOT operator (but not != which is handled differently)
                if (c == '!' && (i + 1 >= expression.length() || expression.charAt(i + 1) != '=')) {
                    return i;
                }
            }
        }
        return expression.length();
    }
}
