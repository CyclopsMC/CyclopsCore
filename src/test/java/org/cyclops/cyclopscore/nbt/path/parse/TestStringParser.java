package org.cyclops.cyclopscore.nbt.path.parse;

import org.cyclops.cyclopscore.nbt.path.parse.StringParser.StringParseResult;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestStringParser {
    @Test
    public void testFailEmpty() {
        StringParseResult parseResult = StringParser.parse("", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailNoOpeningQuote() {
        StringParseResult parseResult = StringParser.parse("wow\"", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailNoClosingQuote() {
        StringParseResult parseResult = StringParser.parse("\"wow", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailUnfinishedEscape() {
        StringParseResult parseResult = StringParser.parse("\"wow\\", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testFailInvalidEscape() {
        StringParseResult parseResult = StringParser.parse("\"wo\\w\"", 0);
        assertThat(parseResult.isSuccess(), is(false));
    }

    @Test
    public void testRegular() {
        StringParseResult parseResult = StringParser.parse("\"wow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(5));
        assertThat(parseResult.getResult(), is("wow"));
    }

    @Test
    public void testPadding() {
        StringParseResult parseResult = StringParser.parse("pad\"wow\"pad", 3);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(5));
        assertThat(parseResult.getResult(), is("wow"));
    }

    @Test
    public void testEscapeBackslash() {
        StringParseResult parseResult = StringParser.parse("\"w\\\\ow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(7));
        assertThat(parseResult.getResult(), is("w\\ow"));
    }

    @Test
    public void testEscapeQuote() {
        StringParseResult parseResult = StringParser.parse("\"w\\\"ow\"", 0);
        assertThat(parseResult.isSuccess(), is(true));
        assertThat(parseResult.getConsumed(), is(7));
        assertThat(parseResult.getResult(), is("w\"ow"));
    }
}
