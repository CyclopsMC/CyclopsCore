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

    protected ExtendedConfig<VillagerConfig> eConfig = null;
    
    /**
     * Make a new instance of a villager.
     * @param eConfig The config for this villager.
     */
    @SuppressWarnings("rawtypes")
    protected ConfigurableVillager(ExtendedConfig eConfig) {
        super(eConfig.getMod()+ ":" + eConfig.getNamedId(),
                eConfig.getMod() + ":" + eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_SKINS) + eConfig.getNamedId() + ".png",
                eConfig.getMod() + ":" + eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_SKINS) + eConfig.getNamedId() + "-zombie.png");
        this.setConfig(eConfig);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return eConfig;
    }

}
