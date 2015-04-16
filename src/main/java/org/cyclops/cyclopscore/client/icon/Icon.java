package org.cyclops.cyclopscore.client.icon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation that can be added to fields of type {@link net.minecraft.client.renderer.texture.TextureAtlasSprite}
 * to automatically populate the field after texture stitching.
 *
 * Register objects that use these annotations to your mod's {@link org.cyclops.cyclopscore.client.icon.IconProvider}.
 *
 * @author rubensworks
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Icon {

    String location();

}
