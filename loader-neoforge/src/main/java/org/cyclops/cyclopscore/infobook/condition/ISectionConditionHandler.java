package org.cyclops.cyclopscore.infobook.condition;

import org.cyclops.cyclopscore.init.ModBase;

/**
 * Handler to check a type of section condition, for when section tags are used in infobooks.
 * @author rubensworks
 *
 */
public interface ISectionConditionHandler {

    /**
     * Check if this condition is satisfied for the given parameter.
     * @param mod The mod that owns the infobook.
     * @param param The condition parameter.
     * @return If this condition is satisfied.
     */
    public boolean isSatisfied(ModBase<?> mod, String param);

}
