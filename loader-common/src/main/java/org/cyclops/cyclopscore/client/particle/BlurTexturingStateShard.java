package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.Reference;

/**
 * @author rubensworks
 */
final class BlurTexturingStateShard extends RenderStateShard.TexturingStateShard {
    public BlurTexturingStateShard(ResourceLocation textureLocation) {
        super(Reference.MOD_ID + ":blur_texturing", () -> {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);
            texture.setFilter(true, false);
        }, () -> {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);
            texture.setFilter(false, false);
        });
    }
}
