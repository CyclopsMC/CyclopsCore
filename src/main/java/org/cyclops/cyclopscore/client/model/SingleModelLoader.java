package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * Custom model loader for a single model.
 * @author rubensworks
 */
public class SingleModelLoader implements ICustomModelLoader {

    private final String modId;
    private final String location;
    private final IModel model;

    public SingleModelLoader(String modId, String location, IModel model) {
        this.modId = modId;
        this.location = location;
        this.model = model;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(modId)
               && modelLocation.getResourcePath().startsWith(location);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return model;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
