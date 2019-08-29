package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class VillagerAction extends ConfigurableTypeAction<VillagerConfig, VillagerProfession> {

    @Override
    public void onRegisterModInit(VillagerConfig eConfig) {
        register(eConfig.getInstance(), eConfig);
    }

}
