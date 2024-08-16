package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.entity.npc.VillagerProfession;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class VillagerConfig extends VillagerConfigCommon<ModBase<?>> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public VillagerConfig(ModBase<?> mod, String namedId, Function<VillagerConfigCommon<ModBase<?>>, ? extends VillagerProfession> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
