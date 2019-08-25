package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;

/**
 * Custom model loader for a single model.
 * @author rubensworks
 */
public class SingleModelLoader implements ICustomModelLoader {

    private final String modId;
    private final String location;
    private final IUnbakedModel model;

    public SingleModelLoader(String modId, String location, IUnbakedModel model) {
        this.modId = modId;
        this.location = location;
        this.model = model;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getNamespace().equals(modId)
               && modelLocation.getPath().startsWith(location);
    }

    @Override
    public IUnbakedModel loadModel(ResourceLocation modelLocation) {
        return model;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
