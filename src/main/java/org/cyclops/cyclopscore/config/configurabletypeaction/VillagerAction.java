package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link VillagerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class VillagerAction extends ConfigurableTypeAction<VillagerConfig>{

    @Override
    public void preRun(VillagerConfig eConfig, Configuration config, boolean startup) {
        //if(startup && !eConfig.isEnabled()) eConfig.setId(0);
    }

    @Override
    public void postRun(VillagerConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register
        String name = eConfig.getMod()+ ":" + eConfig.getNamedId();
        String resource = eConfig.getMod() + ":" + eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_SKINS) + eConfig.getNamedId() + ".png";
        VillagerRegistry.VillagerProfession profession = new VillagerRegistry.VillagerProfession(name, resource);
        VillagerRegistry.instance().register(profession);

        // TODO: This is still being written in Forge!!!
        
        // Add trades
        //VillagerRegistry.instance().registerVillageTradeHandler(eConfig.getId(), (ConfigurableVillager) eConfig.getSubInstance());
    }

}
