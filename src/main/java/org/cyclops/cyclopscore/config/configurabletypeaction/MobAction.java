package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;

/**
 * The action used for {@link MobConfig}.
 * @param <T> The entity type.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class MobAction<T extends Entity> extends ConfigurableTypeAction<MobConfig<T>>{

    @Override
    public void preRun(MobConfig eConfig, Configuration config, boolean startup) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(MobConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register mob
        Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) eConfig.getElement();
        ResourceLocation id = new ResourceLocation(eConfig.getMod().getModId(), eConfig.getSubUniqueName());
        if(eConfig.hasSpawnEgg()) {
            EntityRegistry.registerModEntity(id, clazz, eConfig.getNamedId(), Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY), eConfig.getMod(), 80, 3, true, eConfig.getBackgroundEggColor(), eConfig.getForegroundEggColor());
        } else {
            EntityRegistry.registerModEntity(id, clazz, eConfig.getNamedId(), Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY), eConfig.getMod(), 80, 3, true);
        }
    }

}
