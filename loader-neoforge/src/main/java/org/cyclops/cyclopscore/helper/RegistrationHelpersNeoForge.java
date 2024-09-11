package org.cyclops.cyclopscore.helper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public class RegistrationHelpersNeoForge implements IRegistrationHelpers {
    @Override
    public Item createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Item.Properties props) {
        return new DeferredSpawnEggItem(type, backgroundColor, highlightColor, props);
    }
}
