package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * An {@link ICriterionInstance} that can be tested with a given type.
 * @author rubensworks
 */
public interface ICriterionInstanceTestable<D> extends ICriterionInstance {

    public boolean test(ServerPlayerEntity player, D criterionData);

}
