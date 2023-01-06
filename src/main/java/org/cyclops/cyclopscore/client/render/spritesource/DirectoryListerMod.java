package org.cyclops.cyclopscore.client.render.spritesource;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

/**
 * @author rubensworks
 */
public class DirectoryListerMod implements SpriteSource {

    public static final Codec<DirectoryListerMod> CODEC = RecordCodecBuilder.create((record) -> record.group(
            Codec.STRING.fieldOf("mod").forGetter((lister) -> lister.mod),
            Codec.STRING.fieldOf("source").forGetter((lister) -> lister.sourcePath),
            Codec.STRING.fieldOf("prefix").forGetter((lister) -> lister.idPrefix)
    ).apply(record, DirectoryListerMod::new));
    public static final SpriteSourceType DIRECTORY_MOD = SpriteSources.register("cyclopscore:directory_mod", DirectoryListerMod.CODEC);

    private final String mod;
    private final String sourcePath;
    private final String idPrefix;

    public DirectoryListerMod(String mod, String p_261886_, String p_261776_) {
        this.mod = mod;
        this.sourcePath = p_261886_;
        this.idPrefix = p_261776_;
    }

    @Override
    public void run(ResourceManager p_261582_, SpriteSource.Output p_261898_) {
        FileToIdConverter filetoidconverter = new FileToIdConverter( this.mod + ":" + "textures/" + this.sourcePath, ".png");
        filetoidconverter.listMatchingResources(p_261582_).forEach((p_261906_, p_261635_) -> {
            ResourceLocation resourcelocation = filetoidconverter.fileToId(p_261906_).withPrefix(this.idPrefix);
            p_261898_.add(resourcelocation, p_261635_);
        });
    }

    @Override
    public SpriteSourceType type() {
        return DIRECTORY_MOD;
    }

}
