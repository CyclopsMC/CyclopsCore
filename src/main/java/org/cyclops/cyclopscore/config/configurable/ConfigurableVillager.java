package org.cyclops.cyclopscore.config.configurable;

import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Villager that can hold ExtendedConfigs
 * @author rubensworks
 */
public class ConfigurableVillager extends VillagerRegistry.VillagerProfession implements IConfigurable {

    protected ExtendedConfig<VillagerConfig, ConfigurableVillager> eConfig = null;
    
    /**
     * Make a new instance of a villager.
     * @param eConfig The config for this villager.
     */
    protected ConfigurableVillager(ExtendedConfig<VillagerConfig, ConfigurableVillager> eConfig) {
        super(eConfig.getMod()+ ":" + eConfig.getNamedId(),
                eConfig.getMod() + ":" + eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_SKINS) + eConfig.getNamedId() + ".png",
                eConfig.getMod() + ":" + eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_SKINS) + eConfig.getNamedId() + "-zombie.png");
        this.setConfig(eConfig);
    }
    
    private void setConfig(ExtendedConfig<VillagerConfig, ConfigurableVillager> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<VillagerConfig, ConfigurableVillager> getConfig() {
        return eConfig;
    }

}
