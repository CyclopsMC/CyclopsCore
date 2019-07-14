package org.cyclops.cyclopscore.metadata;

import org.cyclops.cyclopscore.CyclopsCore;

public class RegistryExportables {

    public static IRegistryExportableRegistry REGISTRY = CyclopsCore._instance.getRegistryManager()
            .getRegistry(IRegistryExportableRegistry.class);

    public static void load() {
        REGISTRY.register(new RegistryExportableCraftingRecipe());
        REGISTRY.register(new RegistryExportableItemTranslationKeys());
    }

}
