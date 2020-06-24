package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.parse.NbtPathStringParser.StringParseResult;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestNbtPathStringParser {
    @Test
    public void testFailEmpty() {
        StringParseResult parseResult = NbtPathStringParser.parse("", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailNoOpeningQuote() {
        StringParseResult parseResult = NbtPathStringParser.parse("wow\"", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailNoClosingQuote() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"wow", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailUnfinishedEscape() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"wow\\", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailInvalidEscape() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"wo\\w\"", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testRegular() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"wow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(5));
        assertThat(parseResult.getResult(), is("wow"));
    }

    @Test
    public void testPadding() {
        StringParseResult parseResult = NbtPathStringParser.parse("pad\"wow\"pad", 3);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(5));
        assertThat(parseResult.getResult(), is("wow"));
    }

    @Test
    public void testEscapeBackslash() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"w\\\\ow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(7));
        assertThat(parseResult.getResult(), is("w\\ow"));
    }

    @Test
    public void testEscapeQuote() {
        StringParseResult parseResult = NbtPathStringParser.parse("\"w\\\"ow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(7));
        assertThat(parseResult.getResult(), is("w\"ow"));
    }
}
