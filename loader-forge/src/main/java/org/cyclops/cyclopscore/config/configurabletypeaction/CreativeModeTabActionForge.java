package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.cyclops.cyclopscore.config.extendedconfig.CreativeModeTabConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class CreativeModeTabActionForge<M extends ModBaseForge<M>> extends ConfigurableTypeActionForge<CreativeModeTabConfigCommon<M>, CreativeModeTab, M> {
    @Override
    public void onRegisterForgeFilled(CreativeModeTabConfigCommon<M> eConfig) {
        super.onRegisterForgeFilled(eConfig);
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()), eConfig.getInstance());
    }
}
