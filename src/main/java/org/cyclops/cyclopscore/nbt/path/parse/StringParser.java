package org.cyclops.cyclopscore.nbt.path.parse;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class StringParser {
    @AllArgsConstructor
    public static class StringParseResult {
        private static StringParseResult FAIL = new StringParseResult(false, 0, "");
        @Getter
        private final boolean success;
        @Getter
        private final int consumed;
        @Getter
        private final String result;

        private static StringParseResult success(int consumed, String result) {
            return new StringParseResult(true, consumed, result);
        }

        private static StringParseResult fail() {
            return FAIL;
        }
    }

    /**
     * Parse a string that starts and ends with doubles quotes and; Can handle escape sequences
     * within that string.
     * @param source The source string
     * @param pos Where to start parse; The index of the opening double quote
     * @return Parse result
     */
    public static StringParseResult parse(String source, int pos) {
        if (pos >= source.length() || source.charAt(pos) != '"') {
            return StringParseResult.fail();
        }
        StringBuilder resultBuilder = new StringBuilder();
        int currentPos = pos + 1; // Skip the first double quote
        // This loop is terminated by finding another unescaped double quote.
        while (true) {
            if (currentPos >= source.length()) {
                return StringParseResult.fail();
            }
            char character = source.charAt(currentPos);
            currentPos++;
            switch (character) {
                case '\\': {
                    // Escape
                    if (currentPos >= source.length()) {
                        return StringParseResult.fail();
                    }
                    char escapeName = source.charAt(currentPos);
                    currentPos++;
                    switch (escapeName) {
                        case '\\': // For \\
                        case '"':  // For \"
                            resultBuilder.append(escapeName);
                            continue;
                        default:
                            return StringParseResult.fail();
                    }
                }
                case '"':
                    // End string
                    return StringParseResult.success(currentPos - pos, resultBuilder.toString());
                default:
                    resultBuilder.append(character);
            }
        }
    }
}
