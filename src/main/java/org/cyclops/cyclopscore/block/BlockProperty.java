package org.cyclops.cyclopscore.block;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Apply this annotation to {@link net.minecraft.block.properties.IProperty}s inside a {@link net.minecraft.block.Block}.
 * This will automatically generate the methods {@link net.minecraft.block.Block#getStateFromMeta(int)},
 * {@link net.minecraft.block.Block#getMetaFromState(net.minecraft.block.state.IBlockState)} and
 * {@link net.minecraft.block.Block#createBlockState()}.
 * You should only annotate {@link net.minecraft.block.properties.IProperty} or
 * {@link net.minecraftforge.common.property.IUnlistedProperty} fields.
 * @author rubensworks
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BlockProperty {
	
}
