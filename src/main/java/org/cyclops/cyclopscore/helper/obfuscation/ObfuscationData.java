package org.cyclops.cyclopscore.helper.obfuscation;

import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Entries used for getting private fields and methods by using it in
 * {@link ReflectionHelper#getPrivateValue(Class, Object, String...)}.
 * These MCP mappings should be updated with every MC update!
 * @author rubensworks *
 */
public class ObfuscationData {

    /**
     * Field from net.minecraft.potion.Potion.
     */
    public static final String[] POTION_POTIONTYPES = new String[] { "potionTypes", "field_76425_a", "a" };

    /**
     * Field from net.minecraftforge.oredict.ShapedOreRecipe.
     */
    public static final String[] SHAPEDORERECIPE_WIDTH = new String[] { "width" };
    /**
     * Field from net.minecraftforge.oredict.ShapedOreRecipe.
     */
    public static final String[] SHAPEDORERECIPE_HEIGHT = new String[] { "height" };
	
}
