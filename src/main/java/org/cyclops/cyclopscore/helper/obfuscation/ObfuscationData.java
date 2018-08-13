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

    /**
     * Field from net.minecraft.world.storage.loot.LootTable.
     */
    public static final String[] LOOTTABLE_POOLS = new String[] { "pools", "field_186466_c", "c" };

    /**
     * Field from net.minecraft.client.particle.EffectRenderer.
     */
    public static final String[] PARTICLE_TEXTURES = new String[] { "PARTICLE_TEXTURES", "field_110737_b", "b" };

    /**
     * Field from {@link net.minecraft.entity.Entity}.
     */
    public static final String[] ENTITY_CAPABILITIES = new String[] { "capabilities" };

    /**
     * Field from net.minecraft.advancements.CriteriaTriggers
     */
    public static final String[] CRITERIATRIGGERS_REGISTER = new String[] { "register", "func_192118_a" };

    /**
     * Field from net.minecraft.client.settings.KeyBinding
     */
    public static final String[] KEYBINDING_KEYBIND_ARRAY = new String[] { "KEYBIND_ARRAY", "field_74516_a" };
	
}
