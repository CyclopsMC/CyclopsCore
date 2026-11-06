package org.cyclops.cyclopscore.network.packet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rubensworks
 */
public class TestRequestRecipeDisplaysRegexPacket {

    @Test
    public void testAllowedRegexes() {
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("integrateddynamics:world_reader"));
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("integrateddynamics:squeezer/ore/.*"));
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("evilcraft:crafting/broom_part_rod_.*"));
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:acacia_.*"));
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:.*"));
        assertTrue(RequestRecipeDisplaysRegexPacket.isAllowedRegex("my-mod.x:a.b/c-d_e"));
    }

    @Test
    public void testDisallowedRegexes() {
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex(null));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex(""));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex(".*"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:.*.*"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:(a|aa)*b"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:a+b"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:[a-z]*"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("Minecraft:stone"));
        assertFalse(RequestRecipeDisplaysRegexPacket.isAllowedRegex("minecraft:" + "a".repeat(RequestRecipeDisplaysRegexPacket.MAX_REGEX_LENGTH)));
    }

}
