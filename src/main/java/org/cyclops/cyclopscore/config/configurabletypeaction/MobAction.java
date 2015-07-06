package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.cyclops.cyclopscore.config.extendedconfig.MobConfig;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * The action used for {@link MobConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class MobAction extends ConfigurableTypeAction<MobConfig>{

    @Override
    public void preRun(MobConfig eConfig, Configuration config, boolean startup) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postRun(MobConfig eConfig, Configuration config) {
        // Save the config inside the correct element
        eConfig.save();
        
        // Register mob
        // TODO: remove global entity id's
        Class<? extends EntityLiving> clazz = (Class<? extends EntityLiving>) eConfig.getElement();
        if (MinecraftHelpers.isClientSide())
            eConfig.getMod().getProxy().registerRenderer(clazz, eConfig.getRender(Minecraft.getMinecraft().getRenderManager()));
        EntityRegistry.registerGlobalEntityID(clazz, eConfig.getNamedId(), EntityRegistry.findGlobalUniqueEntityId(), eConfig.getBackgroundEggColor(), eConfig.getForegroundEggColor());
		EntityRegistry.registerModEntity(clazz, eConfig.getNamedId(), Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY), eConfig.getMod(), 80, 3, true);
    }

}
