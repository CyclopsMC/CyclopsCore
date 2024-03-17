package org.cyclops.cyclopscore.infobook.condition;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Condition handler for checking if an item has been registered.
 * @author rubensworks
 *
 */
public class ItemSectionConditionHandler implements ISectionConditionHandler {

    @Override
    public boolean isSatisfied(ModBase<?> mod, String param) {
        return BuiltInRegistries.ITEM.containsKey(new ResourceLocation(param));
    }

}
