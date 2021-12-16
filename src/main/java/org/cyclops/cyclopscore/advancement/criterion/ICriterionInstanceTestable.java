package org.cyclops.cyclopscore.advancement.criterion;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.server.level.ServerPlayer;

/**
 * An {@link ICriterionInstance} that can be tested with a given type.
 * @author rubensworks
 */
public interface ICriterionInstanceTestable<D> extends CriterionTriggerInstance {

    public boolean test(ServerPlayer player, D criterionData);

}
