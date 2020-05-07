package org.cyclops.cyclopscore.metadata;

import net.minecraft.item.crafting.IRecipeType;
import org.cyclops.cyclopscore.CyclopsCore;

public class RegistryExportables {

    public static IRegistryExportableRegistry REGISTRY = CyclopsCore._instance.getRegistryManager()
            .getRegistry(IRegistryExportableRegistry.class);

    public static void load() {
        REGISTRY.register(new RegistryExportableCraftingRecipe());
        REGISTRY.register(new RegistryExportableAbstractCookingRecipe<>(() -> IRecipeType.SMELTING));
        REGISTRY.register(new RegistryExportableAbstractCookingRecipe<>(() -> IRecipeType.BLASTING));
        REGISTRY.register(new RegistryExportableAbstractCookingRecipe<>(() -> IRecipeType.SMOKING));
        REGISTRY.register(new RegistryExportableAbstractCookingRecipe<>(() -> IRecipeType.CAMPFIRE_COOKING));
        REGISTRY.register(new RegistryExportableItemTranslationKeys());
        REGISTRY.register(new RegistryExportableFluidTranslationKeys());
    }

}
